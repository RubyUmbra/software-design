package clients

import io.netty.buffer.ByteBuf
import io.netty.handler.codec.http.HttpResponseStatus
import io.reactivex.netty.protocol.http.server.HttpServer
import io.reactivex.netty.protocol.http.server.HttpServerRequest
import io.reactivex.netty.protocol.http.server.HttpServerResponse
import rx.Observable

import clients.entities.UserAccount

class ClientsServer {
    private val stockClient = StockClient()
    private val users: MutableMap<String, UserAccount> = mutableMapOf()

    fun run() = HttpServer.newServer(8081)
        .start { req: HttpServerRequest<ByteBuf?>, resp: HttpServerResponse<ByteBuf?> ->
            var response: Observable<String>
            val queryParam = req.queryParameters
            try {
                when (req.decodedPath.substring(1)) {
                    "addUser" -> {
                        response = addUser(queryParam)
                        resp.setStatus(HttpResponseStatus.OK)
                    }
                    "depositMoney" -> {
                        response = depositMoney(queryParam)
                        resp.setStatus(HttpResponseStatus.OK)
                    }
                    "getSharesInfo" -> {
                        response = getSharesInfo(queryParam)
                        resp.setStatus(HttpResponseStatus.OK)
                    }
                    "getBalance" -> {
                        response = getBalance(queryParam)
                        resp.setStatus(HttpResponseStatus.OK)
                    }
                    "buyShare" -> {
                        response = buyShares(queryParam)
                        resp.setStatus(HttpResponseStatus.OK)
                    }
                    "sellShare" -> {
                        response = sellShares(queryParam)
                        resp.setStatus(HttpResponseStatus.OK)
                    }
                    else -> {
                        response = Observable.just("Wrong command")
                        resp.setStatus(HttpResponseStatus.BAD_REQUEST)
                    }
                }
            } catch (e: Exception) {
                response = Observable.just("Error occurred")
                resp.setStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR)
            }
            resp.writeString(response)
        }.awaitShutdown()

    fun addUser(queryParam: Map<String, List<String>>): Observable<String> {
        val id = queryParam["id"]?.get(0)
            ?: return Observable.just("Missed attribute: id")
        val balance = queryParam["balance"]?.get(0)?.toDouble()
            ?: return Observable.just("Missed attribute: balance")
        if (id in users)
            return Observable.just("This user is already exists.")
        users[id] = UserAccount(balance)
        return Observable.just("ok")
    }

    fun depositMoney(queryParam: Map<String, List<String>>): Observable<String> {
        val id = queryParam["id"]?.get(0)
            ?: return Observable.just("Missed attribute: id")
        val deposit = queryParam["deposit"]?.get(0)?.toDouble()
            ?: return Observable.just("Missed attribute: deposit")
        val user = users[id]
            ?: return Observable.just("This user doesn't exist.")
        user.balance += deposit
        return Observable.just(user.balance.toString())
    }

    fun getSharesInfo(queryParam: Map<String, List<String>>): Observable<String> {
        val id = queryParam["id"]?.get(0)
            ?: return Observable.just("Missed attribute: id")
        val user = users[id]
            ?: return Observable.just("This user doesn't exist.")
        return Observable.just(user.shares
            .map { (key, _) -> stockClient.getShareInfo(key) }
            .joinToString(separator = "\n"))
    }

    fun getBalance(queryParam: Map<String, List<String>>): Observable<String> {
        val id = queryParam["id"]?.get(0)
            ?: return Observable.just("Missed attribute: id")
        val user = users[id]
            ?: return Observable.just("This user doesn't exist.")
        val shares = user.shares
            .map { (key, value) -> value * (stockClient.getShareInfo(key)?.price ?: 0.0) }
            .sum()
        val total = user.balance + shares
        return Observable.just(total.toString())
    }

    fun buyShares(queryParam: Map<String, List<String>>): Observable<String> {
        val userId = queryParam["userId"]?.get(0)
            ?: return Observable.just("Missed attribute: userId")
        val companyId = queryParam["companyId"]?.get(0)
            ?: return Observable.just("Missed attribute: companyId")
        val amount = queryParam["amount"]?.get(0)?.toInt()
            ?: return Observable.just("Missed attribute: amount")
        val user = users[userId]
            ?: return Observable.just("This user doesn't exist.")
        val shareInfo = stockClient.getShareInfo(companyId)
            ?: return Observable.just("This company is not in stock yet")
        if (shareInfo.price * amount > user.balance)
            return Observable.just("User doesn't have enough money for purchase")
        if (amount > shareInfo.amount)
            return Observable.just("Company doesn't have this amount of shares")
        stockClient.buyShares(companyId, amount)
        user.balance -= shareInfo.price * amount
        user.shares[companyId] = (user.shares[companyId] ?: 0) + amount
        return Observable.just("ok")
    }

    fun sellShares(queryParam: Map<String, List<String>>): Observable<String> {
        val userId = queryParam["userId"]?.get(0)
            ?: return Observable.just("Missed attribute: userId")
        val companyId = queryParam["companyId"]?.get(0)
            ?: return Observable.just("Missed attribute: companyId")
        val amount = queryParam["amount"]?.get(0)?.toInt()
            ?: return Observable.just("Missed attribute: amount")
        val user = users[userId]
            ?: return Observable.just("This user doesn't exist.")
        if (amount > user.shares[companyId] ?: 0)
            return Observable.just("User doesn't have this amount of shares")
        val price = stockClient.getShareInfo(companyId)?.price ?: 0.0
        stockClient.sellShares(companyId, amount)
        user.balance += price * amount
        user.shares[companyId] = (user.shares[companyId] ?: 0) - amount
        return Observable.just("ok")
    }
}
