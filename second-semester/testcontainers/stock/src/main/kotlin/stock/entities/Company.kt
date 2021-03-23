package stock.entities

class Company(val id: String, price: Double, amount: Int) {
    val share: Share = Share(amount, price)
    override fun toString(): String = "Company{id=$id, share=$share}"
}
