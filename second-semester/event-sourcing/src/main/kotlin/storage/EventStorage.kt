package storage

import model.Event

class EventStorage {
    private val newEventsQueue: MutableList<Event> = mutableListOf()
    val size: Int get() = newEventsQueue.size
    fun addEvent(event: Event) = newEventsQueue.add(event)
    fun pop(): Event = newEventsQueue.removeFirst()
    fun peek(): Event = newEventsQueue.first()
}
