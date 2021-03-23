package stock.entities

class Share(var amount: Int, var price: Double) {
    override fun toString(): String = "Share{amount=$amount, price=$price}"
}
