import com.mongodb.rx.client.Success
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import model.ReactiveMongoDriver
import rx.observers.TestSubscriber
import server.ManagerServer
import storage.EventStorage
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class ManagerServerTest {
    companion object {
        private const val DATABASE_NAME = "manager-test"
    }

    private lateinit var mongoDriver: ReactiveMongoDriver
    private lateinit var server: ManagerServer

    @BeforeEach
    fun clearDB() {
        val subscriber = TestSubscriber<Success>()
        ReactiveMongoDriver.client.getDatabase(DATABASE_NAME).drop().subscribe(subscriber)
        subscriber.awaitTerminalEvent()
        mongoDriver = ReactiveMongoDriver(DATABASE_NAME, EventStorage())
        server = ManagerServer(mongoDriver)
    }

    @Test
    fun testNecessaryParams() {
        assertEquals(
            "Missed attribute: id",
            server.handleTicketOperation(mapOf()).toBlocking().first()
        )
        assertEquals(
            "Missed attribute: creationDate",
            server.handleTicketOperation(mapOf("id" to listOf("0"))).toBlocking().first()
        )
    }

    @Test
    fun testAddTicketVersions() {
        val calendar = Calendar.getInstance()
        calendar[2014, Calendar.JUNE, 23, 0, 0] = 0
        val creationDate = calendar.time
        val expirationDate = Date(creationDate.time + TimeUnit.MILLISECONDS.convert(30, TimeUnit.DAYS))
        fillTickets(creationDate, expirationDate)
        val tickets = mongoDriver.getAllTicketVersions(0)
        assertEquals(2, tickets.size.toLong())
        val ticket = tickets[1]
        assertEquals(0, ticket.id.toLong())
        assertEquals(creationDate.toString(), ticket.creationDate.toString())
        assertEquals(expirationDate.toString(), ticket.expirationDate.toString())
    }

    @Test
    fun testAddTicketLatestVersion() {
        val calendar = Calendar.getInstance()
        calendar[2014, Calendar.JUNE, 23, 0, 0] = 0
        val creationDate = calendar.time
        val expirationDate = Date(creationDate.time + TimeUnit.MILLISECONDS.convert(30, TimeUnit.DAYS))
        fillTickets(creationDate, expirationDate)
        val ticket = mongoDriver.getLatestTicketVersion(0)
        assertEquals(0, ticket!!.id.toLong())
        assertEquals(creationDate.toString(), ticket.creationDate.toString())
        assertEquals(expirationDate.toString(), ticket.expirationDate.toString())
    }

    private fun fillTickets(creationDate: Date, expirationDate: Date) {
        val queryParam: MutableMap<String, List<String>> = HashMap()
        queryParam["id"] = listOf("0")
        queryParam["creationDate"] = listOf(
            SimpleDateFormat("dd-MM-yyyy")
                .format(creationDate.time - TimeUnit.MILLISECONDS.convert(10, TimeUnit.DAYS))
        )
        queryParam["expirationDate"] = listOf(
            SimpleDateFormat("dd-MM-yyyy")
                .format(creationDate.time - TimeUnit.MILLISECONDS.convert(10, TimeUnit.DAYS))
        )
        server.handleTicketOperation(queryParam)
        queryParam.replace("id", listOf("0"))
        queryParam.replace("creationDate", listOf(SimpleDateFormat("dd-MM-yyyy").format(creationDate)))
        queryParam.replace("expirationDate", listOf(SimpleDateFormat("dd-MM-yyyy").format(expirationDate)))
        server.handleTicketOperation(queryParam)
    }
}
