package model

import org.bson.Document
import java.util.Date

data class Event(val ticketId: Int, val time: Date, val eventType: EventType) {
    constructor(document: Document) : this(
        document.getInteger("ticketId"),
        document.getDate("time"),
        EventType.valueOf(document.getString("eventType").toUpperCase())
    )

    fun toDocument(): Document = Document("ticketId", ticketId)
        .append("time", time)
        .append("eventType", eventType.toString())
}
