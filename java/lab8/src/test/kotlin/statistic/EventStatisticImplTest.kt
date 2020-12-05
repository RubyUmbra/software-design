package statistic

import clock.SettableClock
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.time.Instant

class EventStatisticImplTest {
    private lateinit var clock: SettableClock
    private lateinit var stat: EventsStatistic

    @Before
    fun before() {
        clock = SettableClock(Instant.now())
        stat = EventStatisticImpl(clock)
    }

    private fun checkByName(name: String, expected: Double) {
        Assert.assertEquals(expected, stat.getEventStatisticByName(name), DELTA)
    }

    private fun checkAll(expected: Map<String, Double>) {
        val actual = stat.getAllEventStatistic()
        Assert.assertEquals(expected.size, actual.size)
        expected.forEach { (name: String, expectedRpm: Double?) ->
            val actualRpm = actual[name]
            Assert.assertNotNull(actualRpm)
            Assert.assertEquals(expectedRpm, actualRpm!!, DELTA)
        }
    }

    @Test
    fun emptyStatisticTest() {
        checkByName("event", 0.0)
        checkAll(mapOf())
    }

    @Test
    fun baseTest() {
        val name = "event"

        stat.incEvent(name)
        stat.incEvent(name)

        checkByName(name, 2 / 60.0)
        checkAll(mapOf(name to 2 / 60.0))
    }

    @Test
    fun expiredAfterOneHourTest() {
        val name = "event"

        stat.incEvent(name)
        clock.addMinutes(60)

        checkByName(name, 0.0)
        checkAll(mapOf())
    }

    @Test
    fun expiredTest() {
        val name = "event"

        stat.incEvent(name)
        checkByName(name, 1 / 60.0)

        clock.addMinutes(30)
        stat.incEvent(name)
        checkByName(name, 2 / 60.0)

        clock.addMinutes(30)
        checkByName(name, 1 / 60.0)
    }

    @Test
    fun multipleEventsTest() {
        val name1 = "event 1"
        val name2 = "event 2"
        val name3 = "event 3"

        stat.incEvent(name1)

        stat.incEvent(name2)
        stat.incEvent(name2)

        stat.incEvent(name3)
        stat.incEvent(name3)
        stat.incEvent(name3)

        checkByName(name1, 1 / 60.0)
        checkByName(name2, 2 / 60.0)
        checkByName(name3, 3 / 60.0)
        checkAll(
            mapOf(
                name1 to 1 / 60.0,
                name2 to 2 / 60.0,
                name3 to 3 / 60.0
            )
        )
    }

    companion object {
        private const val DELTA = 1e-8
    }
}
