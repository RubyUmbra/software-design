package server

import com.mongodb.rx.client.Success
import io.netty.buffer.ByteBuf
import io.netty.handler.codec.http.HttpResponseStatus
import io.reactivex.netty.protocol.http.server.HttpServer
import io.reactivex.netty.protocol.http.server.HttpServerRequest
import io.reactivex.netty.protocol.http.server.HttpServerResponse
import model.ReactiveMongoDriver
import model.Ticket
import rx.Observable
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

class ManagerServer(private val mongoDriver: ReactiveMongoDriver) {
    fun run() = HttpServer.newServer(8080)
        .start { req: HttpServerRequest<ByteBuf?>, resp: HttpServerResponse<ByteBuf?> ->
            val response: Observable<String>
            when (req.decodedPath.substring(1)) {
                "getTicketInfo" -> {
                    response = getTicket(req.queryParameters)
                    resp.setStatus(HttpResponseStatus.OK)
                }
                "addTicketInfo" -> {
                    response = handleTicketOperation(req.queryParameters)
                    resp.setStatus(HttpResponseStatus.OK)
                }
                else -> {
                    response = Observable.just("Wrong command")
                    resp.setStatus(HttpResponseStatus.BAD_REQUEST)
                }
            }
            resp.writeString(response)
        }.awaitShutdown()

    fun getTicket(queryParam: Map<String, List<String>>): Observable<String> {
        val id = queryParam["id"]?.get(0)?.toInt()
            ?: return Observable.just("Missed attribute: id")
        val ticket = mongoDriver.getLatestTicketVersion(id)
            ?: return Observable.just("No tickets were found")
        return Observable.just(ticket.toString())
    }

    fun handleTicketOperation(queryParam: Map<String, List<String>>): Observable<String> {
        val id = queryParam["id"]?.get(0)?.toInt()
            ?: return Observable.just("Missed attribute: id")
        val creationDate: Date
        val expirationDate: Date
        try {
            creationDate = SimpleDateFormat("dd-MM-yyyy").parse(
                queryParam["creationDate"]?.get(0)
                    ?: return Observable.just("Missed attribute: creationDate")
            )
            expirationDate = SimpleDateFormat("dd-MM-yyyy").parse(
                queryParam["expirationDate"]?.get(0)
                    ?: return Observable.just("Missed attribute: expirationDate")
            )
        } catch (e: ParseException) {
            return Observable.just("Wrong date format, expected: dd-MM-yyyy")
        }
        return addTicket(id, creationDate, expirationDate)
    }

    fun addTicket(id: Int, creationDate: Date, expirationDate: Date): Observable<String> {
        if (creationDate.after(expirationDate))
            return Observable.just("Creation date is after expiration date")
        if (mongoDriver.addTicket(Ticket(id, creationDate, expirationDate)) == Success.SUCCESS)
            return Observable.just("Ticket was created or updated")
        return Observable.just("Error: Can't add to database")
    }
}
