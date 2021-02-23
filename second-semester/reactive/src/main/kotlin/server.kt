import io.netty.handler.codec.http.HttpResponseStatus
import io.reactivex.netty.protocol.http.server.HttpServer
import rx.Observable

var driver = ReactiveMongoDriver

fun main() = HttpServer
    .newServer(8080)
    .start { req, resp ->
        val (status, response) = route(req.decodedPath, req.queryParameters)
        resp.status = status
        resp.writeString(response)
    }
    .awaitShutdown()

private fun route(
    path: String,
    parameters: Map<String, List<String>>,
): Pair<HttpResponseStatus, Observable<String?>> = when (path) {
    "/register" -> register(parameters) // {id, currency}
    "/users" -> users()                 // {}
    "/add" -> add(parameters)           // {name, value, currency}
    "/list" -> list(parameters)         // {id}
    else -> notFound()
}

private fun register(
    parameters: Map<String, List<String>>,
): Pair<HttpResponseStatus, Observable<String?>> {
    if (
        "id" !in parameters || parameters["id"]?.size != 1 ||
        "currency" !in parameters || parameters["currency"]?.size != 1
    ) return badRequest()

    val id: Int
    val currency: Currency

    try {
        id = parameters["id"]!![0].toInt()
        currency = parameters["currency"]!![0].toCurrency()
    } catch (e: NumberFormatException) {
        return badRequest()
    } catch (e: IllegalArgumentException) {
        return badRequest()
    }

    val user = User(id, currency)
    return HttpResponseStatus.OK to driver.addUser(user)
        .map { "$user inserted with code: $it" }
}

private fun users(): Pair<HttpResponseStatus, Observable<String?>> =
    HttpResponseStatus.OK to driver.getAllUsers().map { "$it\n" }

private fun add(
    parameters: Map<String, List<String>>,
): Pair<HttpResponseStatus, Observable<String?>> {
    val name: String
    val value: Double
    val currency: Currency

    if ("name" !in parameters || parameters["name"]?.size != 1 ||
        "value" !in parameters || parameters["value"]?.size != 1 ||
        "currency" !in parameters || parameters["currency"]?.size != 1
    ) return badRequest()
    try {
        name = parameters["name"]!![0]
        value = parameters["value"]!![0].toDouble()
        currency = parameters["currency"]!![0].toCurrency()
    } catch (e: NumberFormatException) {
        return badRequest()
    } catch (e: IllegalArgumentException) {
        return badRequest()
    }

    val product = Product(name, value, currency)
    return HttpResponseStatus.OK to driver.addProduct(product)
        .map { "$product inserted with code: $it" }
}

private fun list(
    parameters: Map<String, List<String>>,
): Pair<HttpResponseStatus, Observable<String?>> {
    val id: Int

    if ("id" !in parameters || parameters["id"]?.size != 1) return badRequest()
    try {
        id = parameters["id"]!![0].toInt()
    } catch (e: NumberFormatException) {
        return badRequest()
    } catch (e: IllegalArgumentException) {
        return badRequest()
    }

    return HttpResponseStatus.OK to driver
        .getUserById(id)
        .map(User::currency)
        .flatMap { currency: Currency ->
            driver
                .getAllProducts()
                .map { it.convert(currency) }
                .map { "$it\n" }
        }
}

private fun badRequest(): Pair<HttpResponseStatus, Observable<String?>> =
    HttpResponseStatus.BAD_REQUEST to Observable.just(null)

private fun notFound(): Pair<HttpResponseStatus, Observable<String?>> =
    HttpResponseStatus.NOT_FOUND to Observable.just(null)
