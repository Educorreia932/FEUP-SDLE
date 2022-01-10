class TestApp {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val typeString = args[0]
            val id = args[1]

            when (typeString) {
                "Sub" -> {
                    val subscriber = Subscriber(id)

                    for (i in 2 until args.size step 2) {
                        val topicName = args[i]
                        val numGets = args[i + 1]

                        subscriber.subscribe(topicName)

                        for (j in 1..Integer.parseInt(numGets)) {
                            val response = subscriber.get(topicName)
                            if (response == null) {
                                println("ERROR: Not subscribed")
                            } else {
                                println("GET $response")

                            }
                        }
                        subscriber.unsubscribe(topicName)
                    }
                }
                "Pub" -> {
                    val publisher = Publisher(id)

                    for (i in 2 until args.size step 2) {

                        val topicName = args[i]
                        val numPuts = args[i + 1]

                        for (j in 1..Integer.parseInt(numPuts)) {
                            publisher.put(topicName, (0..999999999999).random().toString())
                        }
                    }
                }
            }
        }
    }
}
