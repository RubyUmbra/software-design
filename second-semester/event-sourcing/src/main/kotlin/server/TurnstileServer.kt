package server

import com.mongodb.rx.client.Success
import io.netty.buffer.ByteBuf
import io.netty.handler.codec.http.HttpResponseStatus
import io.reactivex.netty.protocol.http.server.HttpServer
import io.reactivex.netty.protocol.http.server.HttpServerRequest
import io.reactivex.netty.protocol.http.server.HttpServerResponse
import model.Event
import model.EventType
import model.ReactiveMongoDriver
import rx.Observable
import java.util.*

class TurnstileServer(private val mongoDriver: ReactiveMongoDriver) {
    fun run() = HttpServer.newServer(8081)
        .start { req: HttpServerRequest<ByteBuf?>, resp: HttpServerResponse<ByteBuf?> ->
            val response: Observable<String>
            when (req.decodedPath.substring(1)) {
                "enter" -> {
                    response = addEnter(req.queryParameters, Date())
                    resp.setStatus(HttpResponseStatus.OK)
                }
                "exit" -> {
                    response = addExit(req.queryParameters, Date())
                    resp.setStatus(HttpResponseStatus.OK)
                }
                else -> {
                    response = Observable.just("Wrong command")
                    resp.setStatus(HttpResponseStatus.BAD_REQUEST)
                }
            }
            resp.writeString(response)
        }.awaitShutdown()

    fun addEnter(queryParam: Map<String, List<String>>, date: Date): Observable<String> {
        val id = queryParam["id"]?.get(0)?.toInt()
            ?: return Observable.just("Missed attribute: id")
        val ticket = mongoDriver.getLatestTicketVersion(id)
            ?: return Observable.just("No tickets were found")
        if (date.after(ticket.expirationDate))
            return Observable.just("Ticket is already expired")
        if (mongoDriver.addEvent(Event(id, date, EventType.ENTER)) == Success.SUCCESS)
            return Observable.just("New enter registered")
        return Observable.just("Error: Can't handle the event")
    }

    fun addExit(queryParam: Map<String, List<String>>, date: Date): Observable<String> {
        val id = queryParam["id"]?.get(0)?.toInt()
            ?: return Observable.just("Missed attribute: id")
        if (mongoDriver.addEvent(Event(id, date, EventType.EXIT)) == Success.SUCCESS)
            return Observable.just("New exit registered")
        return Observable.just("Error: Can't handle the event")
    }
}
