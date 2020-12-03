import api.TagStatistics
import client.VKHttpClient
import config.VKConfig

fun main() {
    val tag = "cat"
    val hours = 4
    TagStatistics(VKHttpClient(VKConfig())).getStatistic(tag, hours).forEach(::println)
}
