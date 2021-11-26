import java.io.File
import kotlin.test.AfterTest
import kotlin.test.Test

class Test {
    @Test
    fun testSinglePut() {
        val broker = Broker()
        broker.subscribe("sapo", "sub1")
        broker.put("sapo", "12345")
        broker.context.close()

        assert(broker.topics["sapo"]?.head?.data == "12345")
    }

    @Test
    fun testPutGet() {
        val broker = Broker()
        broker.subscribe("sapo", "sub1")
        broker.put("sapo", "12345")
        val msg = broker.topics["sapo"]?.getMessage("sub1")
        broker.context.close()

        assert(msg == "12345")
        assert(broker.topics["sapo"]?.head == null)
    }

    @Test
    fun test100PutGet() {
        val broker = Broker()
        broker.subscribe("sapo", "sub1")

        for (i in 1..100) {
            broker.put("sapo", i.toString())
            assert(broker.topics["sapo"]?.tail?.data == i.toString())
        }

        for (i in 1..100) {
            val msg = broker.topics["sapo"]?.getMessage("sub1")
            assert(msg == i.toString())
        }
        broker.context.close()

        assert(broker.topics["sapo"]?.tail?.data == null)
    }

    @Test
    fun test10000PutGet() {
        val broker = Broker()
        broker.subscribe("sapo", "sub1")

        for (i in 1..10000) {
            broker.put("sapo", i.toString())
            assert(broker.topics["sapo"]?.tail?.data == i.toString())
        }

        for (i in 1..10000) {
            val msg = broker.topics["sapo"]?.getMessage("sub1")
            assert(msg == i.toString())
        }
        broker.context.close()

        assert(broker.topics["sapo"]?.tail?.data == null)
    }

    @Test
    fun testSaveState() {
        val broker = Broker()
        broker.subscribe("sapo", "sub1")
        broker.put("sapo", "12345")
        broker.put("sapo", "45678")
        broker.put("sapo", "67890")
        broker.saveToFile()
        broker.context.close()

        val newBroker = Broker.loadFromFile().first
        val msg1 = newBroker.topics["sapo"]?.getMessage("sub1")
        assert(msg1 == "12345")
        val msg2 = newBroker.topics["sapo"]?.getMessage("sub1")
        assert(msg2 == "45678")
        val msg3 = newBroker.topics["sapo"]?.getMessage("sub1")
        assert(msg3 == "67890")
        newBroker.context.close()
    }



    @AfterTest
    fun deleteTopicsFile() {
        File(Broker.filePath).delete()
    }
}