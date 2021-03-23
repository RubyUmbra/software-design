package clients.entities

class Share(val amount: Int, val price: Double) {
    override fun toString(): String = "Share{amount=$amount, price=$price}"
}
