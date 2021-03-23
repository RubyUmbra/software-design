import storage.EventStorage
import server.TurnstileServer
import server.ManagerServer
import server.ReportServer
import model.ReactiveMongoDriver

private const val DATABASE_NAME = "fitness-center"

fun main() {
    val eventStorage = EventStorage()

    val turnstileServerServer = TurnstileServer(ReactiveMongoDriver(DATABASE_NAME, eventStorage))
    val managerServer = ManagerServer(ReactiveMongoDriver(DATABASE_NAME, eventStorage))
    val reportServer = ReportServer(ReactiveMongoDriver(DATABASE_NAME, eventStorage), eventStorage)

    Thread { managerServer.run() }.start()
    Thread { turnstileServerServer.run() }.start()
    Thread { reportServer.run() }.start()
}
