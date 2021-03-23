package clients

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.FixedHostPortGenericContainer
import org.junit.Test
import org.junit.Before
import org.junit.ClassRule
import org.junit.Assert.assertEquals

import clients.entities.Share

class AppTest {
    companion object {
        @ClassRule
        @JvmField
        var simpleWebServer: GenericContainer<*> =
            FixedHostPortGenericContainer<FixedHostPortGenericContainer<Nothing>>("stock:1.0-SNAPSHOT")
                .withFixedExposedPort(8080, 8080)
                .withExposedPorts(8080)
        private const val STOCK_SOCKET = "http://localhost:8080"
        private val HTTP_CLIENT = HttpClient.newBuilder().build()
        private val USER_SERVER = ClientsServer()
    }

    private fun sendRequest(url: String): String {
        val request = HttpRequest.newBuilder().uri(URI.create(url)).build()
        val response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString())
        return response.body()
    }

    @Before
    fun initiate() {
        sendRequest("$STOCK_SOCKET/addCompany?id=1&price=10&amount=10")
        sendRequest("$STOCK_SOCKET/addCompany?id=2&price=100&amount=100")
        USER_SERVER.run {
            addUser(
                mapOf(
                    "id" to listOf("1"),
                    "balance" to listOf("50"),
                )
            )
            addUser(
                mapOf(
                    "id" to listOf("2"),
                    "balance" to listOf("100000000"),
                )
            )
            buyShares(
                mapOf(
                    "userId" to listOf("2"),
                    "companyId" to listOf("2"),
                    "amount" to listOf("100"),
                )
            )
        }
    }

    @Test
    fun testAddingCompany() {
        assertEquals(
            "ok",
            sendRequest("$STOCK_SOCKET/addCompany?id=3&price=10&amount=0")
        )
        assertEquals(
            "This company is already in stock.",
            sendRequest("$STOCK_SOCKET/addCompany?id=3&price=10&amount=0")
        )
    }

    @Test
    fun testGettingSharesInfo() {
        assertEquals(
            Share(10, 10.0).toString(),
            sendRequest("$STOCK_SOCKET/getSharesInfo?id=1")
        )
        assertEquals(
            "This company is not in stock yet.",
            sendRequest("$STOCK_SOCKET/getSharesInfo?id=10")
        )
    }

    @Test
    fun testBuyingShares() {
        USER_SERVER.run {
            val params = mapOf(
                "userId" to listOf("1"),
                "companyId" to listOf("1"),
                "amount" to listOf("5"),
            )
            buyShares(params)
                .test().assertValue("ok")
            buyShares(params)
                .test().assertValue("User doesn't have enough money for purchase")
            buyShares(
                mapOf(
                    "userId" to listOf("2"),
                    "companyId" to listOf("1"),
                    "amount" to listOf("100"),
                )
            ).test().assertValue("Company doesn't have this amount of shares")
        }
    }

    @Test
    fun testSellingShares() {
        USER_SERVER.run {
            val params = mapOf(
                "userId" to listOf("2"),
                "companyId" to listOf("2"),
                "amount" to listOf("100"),
            )
            sellShares(params)
                .test().assertValue("ok")
            sellShares(params)
                .test().assertValue("User doesn't have this amount of shares")
        }
    }

    @Test
    fun testBalance() {
        USER_SERVER.run {
            getBalance(mapOf("id" to listOf("1")))
                .test().assertValue(50.0.toString())
            getBalance(mapOf("id" to listOf("2")))
                .test().assertValue(100000000.0.toString())
        }
    }
}
