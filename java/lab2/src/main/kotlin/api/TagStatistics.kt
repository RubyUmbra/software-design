package api

import client.HttpClient
import model.VKResponse
import time.TimeUtils.getTimeIntervalsFromNow
import java.time.Duration

class TagStatistics(
    private val client: HttpClient<VKResponse>,
    private val duration: Duration = Duration.ofHours(1)
) {
    fun getStatistic(tag: String, hours: Int): List<Int> {
        require(hours in 1..24) { "'hoursCount' must be between 1 and 24" }
        val timeIntervals = getTimeIntervalsFromNow(duration, hours + 1)
        return (0 until hours)
            .map { client.query(tag, timeIntervals[it], timeIntervals[it + 1]) }
            .map(VKResponse::count)
    }
}
