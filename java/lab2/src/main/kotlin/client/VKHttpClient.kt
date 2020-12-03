package client

import config.VKConfig
import model.VKResponse

class VKHttpClient(private val config: VKConfig) : HttpClient<VKResponse> {
    override fun query(query: String, startTime: Long, endTime: Long): VKResponse =
        JsonBodyHandler.parseJson(
            UrlReader(
                "${config.proto}://" +
                        "${config.host}:" +
                        "${config.port}" +
                        "/${config.method}?" +
                        "q=%%23$query&" +
                        "start_time=$startTime&" +
                        "end_time=$endTime&" +
                        "access_token=${config.token}&" +
                        "v=${config.version}"
            ).body, VKResponse::class.java
        )
}
