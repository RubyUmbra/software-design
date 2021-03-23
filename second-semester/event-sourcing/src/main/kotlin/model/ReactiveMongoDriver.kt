package model

import com.mongodb.client.model.Filters
import com.mongodb.rx.client.MongoClient
import com.mongodb.rx.client.MongoClients
import com.mongodb.rx.client.Success
import storage.EventStorage
import java.util.concurrent.TimeUnit

class ReactiveMongoDriver(
    private val databaseName: String,
    private val eventStorage: EventStorage,
) {
    companion object {
        @JvmField
        val client: MongoClient = MongoClients.create("mongodb://localhost:27017")
    }

    fun addTicket(ticket: Ticket): Success = client
        .getDatabase(databaseName)
        .getCollection("ticket")
        .insertOne(ticket.toDocument())
        .timeout(10, TimeUnit.SECONDS)
        .toBlocking()
        .single()

    fun addEvent(event: Event): Success {
        val result = client
            .getDatabase(databaseName)
            .getCollection("event")
            .insertOne(event.toDocument())
            .timeout(10, TimeUnit.SECONDS)
            .toBlocking()
            .single()
        if (result == Success.SUCCESS) eventStorage.addEvent(event)
        return result
    }

    fun getLatestTicketVersion(id: Int): Ticket? =
        getAllTicketVersions(id).maxByOrNull { it.creationDate }

    fun getAllTicketVersions(id: Int): List<Ticket> {
        val tickets: MutableList<Ticket> = mutableListOf()
        client.getDatabase(databaseName)
            .getCollection("ticket")
            .find(Filters.eq("id", id))
            .maxTime(10, TimeUnit.SECONDS)
            .toObservable()
            .map(::Ticket)
            .toBlocking()
            .subscribe(tickets::add)
        return tickets
    }

    val events: MutableList<Event>
        get() {
            val events: MutableList<Event> = mutableListOf()
            client.getDatabase(databaseName)
                .getCollection("event")
                .find()
                .maxTime(10, TimeUnit.SECONDS)
                .toObservable()
                .map(::Event)
                .toBlocking()
                .subscribe(events::add)
            return events
        }
}
