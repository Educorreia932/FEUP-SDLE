class TestApp {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val typeString = args[0]
            if(typeString == "Sub") {
                val id = args[1]

                val subscriber = Subscriber(id)
                for (i in 2 until args.size step 2) {
                    val topicName = args[i]
                    val numGets = args[i + 1]
                    subscriber.subscribe(topicName)
                    for (j in 1..Integer.parseInt(numGets))
                        subscriber.get(topicName)
                    subscriber.unsubscribe(topicName)
                }
            }
            else if(typeString == "Pub"){
                val id = args[1]

                val publisher = Publisher(id)
                for (i in 2 until args.size step 2) {
                    val topicName = args[i]
                    val numPuts = args[i + 1]
                    for (j in 1..Integer.parseInt(numPuts))
                        publisher.put(topicName, (0..999999999999).random().toString())
                }
            }

        }
    }
}