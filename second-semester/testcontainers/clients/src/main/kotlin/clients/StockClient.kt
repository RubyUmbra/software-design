package clients

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

import clients.entities.Share

class StockClient {
    companion object {
        private val HTTP_CLIENT = HttpClient.newBuilder().build()
        private const val HOST_URL = "http://localhost:8080"
    }

    private fun sendRequest(url: String): String {
        val request = HttpRequest.newBuilder().uri(URI.create(url)).build()
        val response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString())
        return response.body()
    }

    fun getShareInfo(id: String): Share? {
        val response = sendRequest("$HOST_URL/getSharesInfo?id=$id")
        if (!response.startsWith("Share")) return null
        val (amount, price) = response
            .removePrefix("Share{")
            .removeSuffix("}")
            .split(", ")
            .map { it.split("=").last() }
        return Share(amount.toInt(), price.toDouble())
    }

    fun buyShares(id: String, count: Int) =
        sendRequest("$HOST_URL/buyShares?id=$id&shares=$count")

    fun sellShares(id: String, count: Int) =
        sendRequest("$HOST_URL/sellShares?id=$id&shares=$count")
}
