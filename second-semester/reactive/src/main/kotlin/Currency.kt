sealed class Currency(private val c: Double) {
    object EUR : Currency(80.0)
    object RUB : Currency(1.0)
    object USD : Currency(100.0)

    override fun toString() = when (this) {
        EUR -> "EUR"
        RUB -> "RUB"
        USD -> "USD"
    }

    fun convert(value: Double, to: Currency) = value * to.c / c
}

fun String.toCurrency() = when (this) {
    "EUR" -> Currency.EUR
    "RUB" -> Currency.RUB
    "USD" -> Currency.USD
    else -> throw IllegalArgumentException(this)
}
