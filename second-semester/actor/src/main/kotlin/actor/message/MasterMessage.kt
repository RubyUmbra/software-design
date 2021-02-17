package actor.message

sealed class MasterMessage {
    data class Request(
        val query: String,
    ) : MasterMessage()

    data class Response(
        val name: String,
        val response: String,
    ) : MasterMessage()

    object Timeout : MasterMessage()
}
