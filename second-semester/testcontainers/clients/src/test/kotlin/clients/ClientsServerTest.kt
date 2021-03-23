package clients

import org.junit.jupiter.api.Test

class ClientsServerTest {
    companion object {
        private val USER_SERVER = ClientsServer()
    }

    @Test
    fun addUser() {
        USER_SERVER.run {
            val params: Map<String, List<String>> = mapOf(
                "id" to listOf("user"),
                "balance" to listOf("0"),
            )
            addUser(params).test().assertValue("ok")
            addUser(params).test().assertValue("This user is already exists.")
        }
    }

    @Test
    fun depositMoney() {
        USER_SERVER.run {
            addUser(
                mapOf(
                    "id" to listOf("user"),
                    "balance" to listOf("0"),
                )
            )
            depositMoney(
                mapOf(
                    "id" to listOf("user"),
                    "deposit" to listOf("100"),
                )
            ).test().assertValue(100.0.toString())
        }
    }
}
