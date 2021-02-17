package searcher

import java.lang.AutoCloseable
import org.mockserver.integration.ClientAndServer
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import org.mockserver.model.HttpStatusCode
import java.time.Duration

class SearcherStubServer(
    private val name: String,
    port: Int,
    delay: Duration,
) : AutoCloseable {
    private val stubServer = ClientAndServer
        .startClientAndServer(port)
        .apply {
            `when`(
                HttpRequest.request()
                    .withMethod("GET")
                    .withPath("/search")
            ).respond { request ->
                Thread.sleep(delay.toMillis())
                val q = request.getFirstQueryStringParameter("q")
                HttpResponse.response()
                    .withStatusCode(HttpStatusCode.OK_200.code())
                    .withBody(genResponse(q))
            }
        }

    override fun close() = stubServer.close()
    fun genResponse(query: String) = "$name:[${(1..5).joinToString(separator = ",") { "$query$it" }}]"
}
