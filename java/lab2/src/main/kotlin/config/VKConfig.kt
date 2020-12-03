package config

class VKConfig(
    val proto: String = "https",
    val host: String = "api.vk.com",
    val port: Int = 443,
    val method: String = "method/newsfeed.search",
    val token: String = "token",
    val version: String = "5.124"
)
