import org.bson.Document

data class Product(
    val name: String,
    val value: Double,
    val currency: Currency,
) {
    fun toDocument(): Document = Document("name", name)
        .append("value", value)
        .append("currency", currency.toString())

    fun convert(to: Currency): Product =
        Product(name, currency.convert(value, to), to)
}

fun Document.toProduct(): Product = Product(
    getString("name"),
    getDouble("value"),
    getString("currency").toCurrency(),
)
