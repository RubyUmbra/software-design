package server

import io.netty.buffer.ByteBuf
import io.netty.handler.codec.http.HttpResponseStatus
import io.reactivex.netty.protocol.http.server.HttpServer
import io.reactivex.netty.protocol.http.server.HttpServerRequest
import io.reactivex.netty.protocol.http.server.HttpServerResponse
import model.EventType
import model.ReactiveMongoDriver
import rx.Observable
import storage.EventStorage
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class ReportServer(
    mongoDriver: ReactiveMongoDriver,
    private val eventStorage: EventStorage
) {
    private var sumTime: Long = 0L
    private var numSessions: Long = 0L
    private val eventsByDay: MutableMap<String, Int> = mutableMapOf()

    init {
        for (event in mongoDriver.events) {
            if (event.eventType == EventType.ENTER) {
                sumTime -= event.time.time
                numSessions++
                val calendar = Calendar.getInstance()
                calendar.time = event.time
                val key = SimpleDateFormat("dd-MM-yyyy").format(calendar.time)
                eventsByDay[key] = (eventsByDay[key] ?: 0) + 1
            } else {
                sumTime += event.time.time
            }
        }
    }

    private fun updateEvents() {
        if (eventStorage.size > 0) {
            while (eventStorage.size > 0) {
                val event = eventStorage.peek()
                if (event.eventType == EventType.ENTER) {
                    sumTime -= event.time.time
                    numSessions++
                    val calendar = Calendar.getInstance()
                    calendar.time = event.time
                    val key = SimpleDateFormat("dd-MM-yyyy").format(calendar.time)
                    eventsByDay[key] = (eventsByDay[key] ?: 0) + 1
                } else {
                    sumTime += event.time.time
                }
                eventStorage.pop()
            }
        }
    }

    fun run() = HttpServer.newServer(8082)
        .start { req: HttpServerRequest<ByteBuf?>, resp: HttpServerResponse<ByteBuf?> ->
            val response: Observable<String>
            when (req.decodedPath.substring(1)) {
                "dailyStat" -> {
                    response = Observable.just(stats())
                    resp.setStatus(HttpResponseStatus.OK)
                }
                "medDuration" -> {
                    response = Observable.just(mediumDuration())
                    resp.setStatus(HttpResponseStatus.OK)
                }
                else -> {
                    response = Observable.just("Wrong command")
                    resp.setStatus(HttpResponseStatus.BAD_REQUEST)
                }
            }
            resp.writeString(response)
        }.awaitShutdown()

    fun stats(): String {
        updateEvents()
        if (eventsByDay.isEmpty())
            return "No records"
        return eventsByDay.map { (key, value) -> "$key: $value" }
            .joinToString("\n", postfix = "\n")
    }

    fun mediumDuration(): String {
        updateEvents()
        if (numSessions == 0L)
            return "No records"
        val medium = TimeUnit.MINUTES
            .convert(sumTime / numSessions, TimeUnit.MILLISECONDS)
        return "Medium duration is $medium minutes"
    }
}
