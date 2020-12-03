package client

import com.fasterxml.jackson.databind.ObjectMapper
import java.net.http.HttpResponse.*
import java.nio.charset.StandardCharsets

class JsonBodyHandler<W>(private val clazz: Class<W>) : BodyHandler<W> {
    override fun apply(responseInfo: ResponseInfo): BodySubscriber<W> = asJSON(clazz)

    companion object {
        fun <T> parseJson(body: String?, targetType: Class<T>?): T =
            with(ObjectMapper()) {
                readValue(readTree(body)["response"].toString(), targetType)
            }

        fun <T> asJSON(targetType: Class<T>?): BodySubscriber<T> =
            BodySubscribers.mapping(
                BodySubscribers.ofString(StandardCharsets.UTF_8)
            ) { body: String? -> parseJson(body, targetType) }
    }
}
