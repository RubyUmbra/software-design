package client

import org.apache.maven.surefire.shade.org.apache.commons.io.IOUtils
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

class UrlReader(query: String) : AutoCloseable {
    private val connection: HttpURLConnection = URL(query).openConnection() as HttpURLConnection
    val responseCode: Int get() = connection.responseCode
    val body: String get() = IOUtils.toString(connection.inputStream, StandardCharsets.UTF_8)
    override fun close() = connection.disconnect()
}
