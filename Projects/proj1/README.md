# Implementation of a Reliable Pub/Sub Service

First project for group **T2G16**.

## Group members

- David Ferreira (up202102686@edu.fe.up.pt)
- Eduardo Correia (up201806433@edu.fe.up.pt)
- João Cardoso (up201806531@edu.fe.up.pt)
- Ricardo Fontão (up201806317@edu.fe.up.pt)

## How to run

1. Install Maven (tested with version 3.6.3)
2. Run `mvn package`
3. Last step generates two jars in the target directory: `testApp.jar` and `broker.jar`

`broker.jar` can be run as is without any arguments:

```
java -jar target/broker.jar
```

The broker will output a `topics.ser` file which contains its internal state. 
On startup the broker will try to read this state. 
If this is not desired the state file must be deleted.

`testApp` can be either a subscriber or a publisher depending on the arguments.

It can be called like:

```
java -jar testApp.jar <Pub|Sub> <id> (<Topic> <Amount>)*
```

The first argument specifies if the `testApp` will work as a publisher or a subscriber.

The second argument specifies an arbitrary string to serve as the `testApp` ID.

The following arguments specify the amount of times the testApp will try to GET or PUT a random string to a specific Topic.

Some examples:

`testApp.jar Pub pub1 sapo 15 batata 5` -> will PUT to sapo 15 times and to batata 5 times

`testApp.jar Sub sub1 sapo 10` -> will GET from sapo 10 times

For subscribers the `testApp` will automatically subscribe to the desired topic at the beginning and unsubscribe at the end.


To stop the program execution please use *CTRL + C*.