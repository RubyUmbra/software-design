package clock

import java.time.Duration
import java.time.Instant

class SettableClock(private var now: Instant) : Clock {
    override fun now(): Instant = now

    fun setNow(now: Instant) {
        this.now = now
    }

    fun addMinutes(minutes: Long) = setNow(now.plus(Duration.ofMinutes(minutes)))
}
