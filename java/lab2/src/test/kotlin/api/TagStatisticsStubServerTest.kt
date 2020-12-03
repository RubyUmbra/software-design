package api

import client.UrlReader
import client.VKHttpClient
import com.xebialabs.restito.builder.stub.StubHttp
import com.xebialabs.restito.semantics.Action
import com.xebialabs.restito.semantics.Condition
import com.xebialabs.restito.server.StubServer
import config.VKConfig
import org.glassfish.grizzly.http.Method
import org.glassfish.grizzly.http.util.HttpStatus
import org.junit.Assert
import org.junit.Test
import java.io.IOException

class TagStatisticsStubServerTest {
    companion object {
        private const val PORT = 3535
    }

    @Test
    fun baseTest() = withStubServer() { server: StubServer ->
        StubHttp.whenHttp(server)
            .match(Condition.method(Method.GET))
            .then(
                Action.stringContent(
                    "{\"response\":{\"items\":[],\"count\":0,\"total_count\":0}}"
                )
            )

        val statistics = TagStatistics(
            VKHttpClient(
                VKConfig(proto = "http", host = "localhost", port = PORT)
            )
        )

        val actual = statistics.getStatistic("tag", 1)

        Assert.assertEquals(1, actual.size)
        Assert.assertEquals(0, actual[0])
    }

    @Test(expected = IOException::class)
    fun badRequestTest() = withStubServer() { server: StubServer ->
        StubHttp.whenHttp(server)
            .match(Condition.method(Method.GET))
            .then(Action.status(HttpStatus.BAD_REQUEST_400))

        val urlReader = UrlReader("http://localhost:$PORT/test")
        Assert.assertEquals(urlReader.responseCode.toLong(), 400)
        urlReader.body
    }

    private fun withStubServer(port: Int = PORT, callback: (StubServer) -> Unit) {
        var server: StubServer? = null
        try {
            server = StubServer(port).run()
            callback(server)
        } finally {
            server?.stop()
        }
    }
}
