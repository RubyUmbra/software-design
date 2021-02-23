import org.bson.Document

data class User(
    val id: Int,
    val currency: Currency,
) {
    fun toDocument(): Document = Document("id", id)
        .append("currency", currency.toString())
}

fun Document.toUser() = User(
    getInteger("id"),
    getString("currency").toCurrency(),
)
