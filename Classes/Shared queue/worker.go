package main

import (
	"fmt"
	"time"

	zmq "github.com/pebbe/zmq4"
)

func main() {
	// Prepare context and publisher
	context, _ := zmq.NewContext()
	defer context.Term()

	s, _ := context.NewSocket(zmq.REP)
	defer s.Close()

	fmt.Print("Connecting...\n")

	s.Connect("tcp://localhost:5560")

	for {
		// Get request
		str, _ := s.Recv(0)

		fmt.Printf("Received request: [%s]", str)

		time.Sleep(1)

		// Send reply
		s.Send("World", 0)
	}
}
