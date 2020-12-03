interface Cache<K, V> {
    /**
     * Associates value with key in this cache.
     */
    operator fun set(key: K, value: V)

    /**
     * Returns the value associated with key in this cache.
     */
    operator fun get(key: K): V?

    /**
     * Returns all entry of cache.
     */
    fun values(): List<Pair<K, V>>
}
