import java.lang.Thread.sleep
import kotlin.concurrent.thread

class TestApp {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            //val typeString = args[0]
            //val id = args[1]

            val typeString = "Sub"
            val id = "1235"

            when (typeString) {
                "Sub" -> {
                    val subscriber = Subscriber(id)

                    val topicName = "topicasso"
                    val numGets = "1"

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
                "Pub" -> {
                    val publisher = Publisher(id)

                    val topicName = "topicasso"
                    val numPuts = "2"

                    for (j in 1..Integer.parseInt(numPuts)) {
                        publisher.put(topicName, (0..999999999999).random().toString())
                    }
                }
            }
        }
    }
}
