package main

import (
	"fmt"
	"math/rand"

	zmq "github.com/pebbe/zmq4"
)

func main() {
	// Prepare context and publisher
	zctx, _ := zmq.NewContext()

	defer zctx.Term()

	s, _ := zctx.NewSocket(zmq.PUB)

	defer s.Close()

	s.Bind("tcp://*:5556")

	for {
		zipCode := rand.Intn(10000)
		temperature := rand.Intn(215) - 80
		humidity := rand.Intn(50) + 10

		message := fmt.Sprintf("%d %d %d", zipCode, temperature, humidity)

		s.Send(message, 0)
	}
}
