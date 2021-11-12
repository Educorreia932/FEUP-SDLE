package main

import (
	"fmt"
	"os"

	zmq "github.com/pebbe/zmq4"
)

func main() {
	// Socket to talk to server
	context, _ := zmq.NewContext()

	subscriberUS, _ := context.NewSocket(zmq.SUB)
	subscriberPT, _ := context.NewSocket(zmq.SUB)

	subscriberUS.Connect("tcp://localhost:5556")
	subscriberPT.Connect("tcp://localhost:5557")

	filter := "4710"

	if len(os.Args) > 1 {
		filter = string(os.Args[1])
	}

	subscriberUS.SetSubscribe(filter)
	subscriberPT.SetSubscribe(filter)

	total_temperature := 0

	poller := zmq.NewPoller()

	poller.Add(subscriberPT, zmq.POLLIN)
	poller.Add(subscriberUS, zmq.POLLIN)

	i := 0

	fmt.Print("Collecting messages from weather publishers...\n")

	// Process 100 messages
	for ; i < 100; i++ {
		sockets, _ := poller.Poll(-1)
		var zipCode, temperature, humidity int

		for _, socket := range sockets {
			s := socket.Socket
			message, _ := s.Recv(0)

			fmt.Sscanf(message, "%d %d %d", &zipCode, &temperature, &humidity)

			total_temperature += temperature

			switch s {
			case subscriberPT:
				fmt.Print("+")
			case subscriberUS:
				fmt.Print("*")
			}
		}
	}

	fmt.Printf("\nAverage temperature for zipcode %s was %d ºF\n", filter, total_temperature/i)
}
