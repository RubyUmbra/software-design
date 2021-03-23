package model

import org.bson.Document
import java.util.Date

data class Ticket(val id: Int, val creationDate: Date, val expirationDate: Date) {
    constructor(document: Document) : this(
        document.getInteger("id"),
        document.getDate("creationDate"),
        document.getDate("expirationDate")
    )

    fun toDocument(): Document = Document("id", id)
        .append("creationDate", creationDate)
        .append("expirationDate", expirationDate)
}
