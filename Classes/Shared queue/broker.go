package main

import (
	"fmt"

	zmq "github.com/pebbe/zmq4"
)

func main() {
	// Prepare context and publisher
	context, _ := zmq.NewContext()
	defer context.Term()

	frontend, _ := context.NewSocket(zmq.ROUTER)
	backend, _ := context.NewSocket(zmq.DEALER)

	defer frontend.Close()
	defer backend.Close()

	fmt.Print("Connecting...\n")

	frontend.Bind("tcp://localhost:5559")
	backend.Bind("tcp://localhost:5560")

	poller := zmq.NewPoller()

	poller.Add(frontend, zmq.POLLIN)
	poller.Add(backend, zmq.POLLIN)

	for {
		sockets, _ := poller.Poll(-1)

		for _, socket := range sockets {
			s := socket.Socket
			message, _ := s.Recv(0)

			switch s {
			case frontend:
				for {
					if more, _ := s.GetRcvmore(); more {
						backend.Send(message, zmq.SNDMORE)
					} else {
						backend.Send(message, 0)
						break
					}
				}

			case backend:
				if more, _ := s.GetRcvmore(); more {
					frontend.Send(message, zmq.SNDMORE)
				} else {
					frontend.Send(message, 0)
					break
				}
			}
		}

	}
}
