package main

import (
	"fmt"

	zmq "github.com/pebbe/zmq4"
)

func main() {
	// Prepare context and publisher
	context, _ := zmq.NewContext()
	defer context.Term()

	s, _ := context.NewSocket(zmq.REQ)
	defer s.Close()

	fmt.Print("Connecting...\n")

	s.Connect("tcp://localhost:5559")

	for i := 0; i < 10; i++ {
		s.Send("Hello", 0)

		str, _ := s.Recv(0)

		fmt.Printf("Received reply %d [%s]\n", i, str)
	}
}
