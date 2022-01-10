# Distributed Systems in Large Scale ⛓

## M.EIC - 3<sup>rd</sup> year / 1<sup>st</sup> semester

## Projects

### Implementation of a Reliable Pub/Sub Service

The first project consisted in designing and implementing a reliable publish/subscribe service with exactly-once message delivery in mind.

The implementation was done in Kotlin and since it compiles to Java bytecode, the JeroMQ library (Java's binding for ZeroMQ) was used for message exchanging. As a build tool we used Maven