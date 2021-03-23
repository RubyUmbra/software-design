import com.mongodb.rx.client.Success
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import model.Event
import model.EventType
import model.ReactiveMongoDriver
import model.Ticket
import rx.observers.TestSubscriber
import server.ReportServer
import storage.EventStorage
import java.util.*
import java.util.concurrent.TimeUnit

class ReportServerTest {
    companion object {
        private const val DATABASE_NAME = "report-test"
    }

    private lateinit var mongoDriver: ReactiveMongoDriver
    private lateinit var server: ReportServer

    @BeforeEach
    fun clearDB() {
        val subscriber = TestSubscriber<Success>()
        ReactiveMongoDriver.client.getDatabase(DATABASE_NAME).drop().subscribe(subscriber)
        subscriber.awaitTerminalEvent()
        val eventStorage = EventStorage()
        mongoDriver = ReactiveMongoDriver(DATABASE_NAME, eventStorage)
        server = ReportServer(mongoDriver, eventStorage)
    }

    @Test
    fun emptyStat() {
        assertEquals("No records", server.stats())
        assertEquals("No records", server.mediumDuration())
    }

    @Test
    fun testDailyStats() {
        fillEnters()
        assertEquals(
            """
    24-06-2014: 2
    25-06-2014: 1
    26-06-2014: 1
    27-06-2014: 1
    28-06-2014: 1
    
    """.trimIndent(), server.stats()
        )
    }

    @Test
    fun testMediumDuration() {
        fillEnters()
        assertEquals("Medium duration is 120 minutes", server.mediumDuration())
    }

    private fun fillEnters() {
        val calendar = Calendar.getInstance()
        calendar[2014, Calendar.JUNE] = 23
        val creation = calendar.time
        val expiration = Date(creation.time + TimeUnit.MILLISECONDS.convert(365, TimeUnit.DAYS))
        mongoDriver.addTicket(Ticket(0, creation, expiration))
        for (i in 1..5) {
            val entry = Date(creation.time + TimeUnit.MILLISECONDS.convert(i.toLong(), TimeUnit.DAYS))
            val exit = Date(entry.time + TimeUnit.MILLISECONDS.convert(2, TimeUnit.HOURS))
            mongoDriver.addEvent(Event(0, entry, EventType.ENTER))
            mongoDriver.addEvent(Event(0, exit, EventType.EXIT))
        }
        val entry = Date(creation.time + TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS))
        val exit = Date(entry.time + TimeUnit.MILLISECONDS.convert(2, TimeUnit.HOURS))
        mongoDriver.addEvent(Event(0, entry, EventType.ENTER))
        mongoDriver.addEvent(Event(0, exit, EventType.EXIT))
    }
}
