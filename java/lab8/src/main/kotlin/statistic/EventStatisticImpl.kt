package statistic

import clock.Clock

class EventStatisticImpl(
    private val clock: Clock
) : EventsStatistic {
    private val queue: MutableList<Event> = mutableListOf()
    private val statistic: MutableMap<String, Int> = mutableMapOf()

    private class Event(val name: String, val time: Long)

    private fun update(now: Long = clock.now().toEpochMilli()): EventStatisticImpl {
        while (queue.isNotEmpty()) {
            val first = queue.first()
            if (first.time + 3600000.0 <= now) {
                queue.removeFirst()
                val count = statistic[first.name]!!
                if (count == 1) statistic.remove(first.name)
                else statistic[first.name] = count - 1
            } else break
        }
        return this
    }

    private fun getRpm(count: Int?): Double = (count ?: 0) / 60.0

    override fun incEvent(name: String) {
        val now = clock.now().toEpochMilli()
        update(now)
        queue.add(Event(name, now))
        statistic[name] = (statistic[name] ?: 0) + 1
    }

    override fun getEventStatisticByName(name: String): Double =
        update().getRpm(statistic[name])

    override fun getAllEventStatistic(): Map<String, Double> =
        update().statistic.mapValues { getRpm(it.value) }

    override fun printStatistic() = println(this)
    override fun toString(): String =
        update().statistic.entries
            .joinToString("\n") { "${it.key}: ${it.value}" }
}
