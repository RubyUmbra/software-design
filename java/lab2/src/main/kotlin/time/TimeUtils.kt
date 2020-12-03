package time

import java.time.Duration

object TimeUtils {
    fun getTimeIntervalsFromNow(duration: Duration, count: Int): List<Long> =
        getTimeIntervals(System.currentTimeMillis() / 1000L, duration.toSeconds(), count)

    fun getTimeIntervals(end: Long, duration: Long, count: Int): List<Long> =
        (0 until count).map { end - (count - it) * duration }
}
