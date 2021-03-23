package clients.entities

class UserAccount(
    var balance: Double,
    val shares: MutableMap<String, Int> = mutableMapOf()
)
