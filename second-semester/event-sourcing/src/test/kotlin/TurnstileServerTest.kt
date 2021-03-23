import com.mongodb.rx.client.Success
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import model.Event
import model.EventType
import model.ReactiveMongoDriver
import model.Ticket
import rx.observers.TestSubscriber
import server.TurnstileServer
import storage.EventStorage
import java.util.Date

class TurnstileServerTest {
    companion object {
        private const val DATABASE_NAME = "turnstile-test"
    }

    private lateinit var mongoDriver: ReactiveMongoDriver
    private lateinit var server: TurnstileServer

    @BeforeEach
    fun before() {
        val subscriber = TestSubscriber<Success>()
        ReactiveMongoDriver.client.getDatabase(DATABASE_NAME).drop().subscribe(subscriber)
        subscriber.awaitTerminalEvent()
        mongoDriver = ReactiveMongoDriver(DATABASE_NAME, EventStorage())
        server = TurnstileServer(mongoDriver)
    }

    @Test
    fun testMissed() {
        val subscriber = TestSubscriber<String>()
        server.addEnter(mapOf(), Date()).subscribe(subscriber)
        subscriber.assertValue("Missed attribute: id")
    }

    @Test
    fun testNoTickets() {
        val subscriber = TestSubscriber<String>()
        server.addEnter(mapOf("id" to listOf("0")), Date()).subscribe(subscriber)
        subscriber.assertValues("No tickets were found")
    }

    @Test
    fun testAddEnter() {
        val creation = Date()
        val enter = Date(creation.time + 1000)
        val expiration = Date(creation.time + 5000)
        val subscriber = TestSubscriber<String>()
        mongoDriver.addTicket(Ticket(0, creation, expiration))
        server.addEnter(mapOf("id" to listOf("0")), enter).subscribe(subscriber)
        subscriber.assertValue("New enter registered")
        val events: List<Event> = mongoDriver.events
        assertEquals(1, events.size.toLong())
        val event = events[0]
        assertEquals(enter, event.time)
        assertEquals(EventType.ENTER, event.eventType)
        assertEquals(0, event.ticketId.toLong())
    }

    @Test
    fun testAddExit() {
        val queryParam: MutableMap<String, List<String>> = HashMap()
        queryParam["id"] = listOf("0")
        val creation = Date()
        val enter = Date(creation.time + 1000)
        val expiration = Date(creation.time + 5000)
        val subscriber = TestSubscriber<String>()
        mongoDriver.addTicket(Ticket(0, creation, expiration))
        server.addExit(queryParam, enter).subscribe(subscriber)
        subscriber.assertValue("New exit registered")
        val events: List<Event> = mongoDriver.events
        assertEquals(1, events.size.toLong())
        val event = events[0]
        assertEquals(enter, event.time)
        assertEquals(EventType.EXIT, event.eventType)
        assertEquals(0, event.ticketId.toLong())
    }
}
