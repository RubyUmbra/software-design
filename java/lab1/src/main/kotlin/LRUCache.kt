class LRUCache<K, V>(
    private val capacity: Int
) : Cache<K, V> {
    private val cache: MutableMap<K, Node>
    private var head: Node?
    private var tail: Node?

    init {
        assert(capacity > 0)

        cache = HashMap(capacity)
        head = null
        tail = null
    }

    override fun set(key: K, value: V) {
        val sizeBefore: Int = cache.size -
                if (key in cache || cache.size == capacity) 1 else 0

        if (key in cache)
            remove(cache.remove(key)!!)

        if (cache.size >= capacity)
            remove(cache.remove(tail!!.key)!!)

        val node = Node(key, value)
        addFirst(node)
        cache[key] = node

        assert(head == node)
        assert(head?.key == key)
        assert(head?.value == key)
        assert(sizeBefore + 1 == listSize())
        assert(sizeBefore + 1 == cache.size)
        assert(sizeBefore + 1 <= capacity)
    }

    override fun get(key: K): V? {
        val sizeBefore: Int = cache.size

        val node: Node = cache[key] ?: return null
        remove(node)
        addFirst(node)

        assert(head == node)
        assert(head?.key == key)
        assert(sizeBefore == listSize())
        assert(sizeBefore == cache.size)
        return node.value
    }

    override fun values(): List<Pair<K, V>> =
        mutableListOf<Pair<K, V>>().apply {
            var cur: Node? = head
            while (cur != null)
                cur = cur.apply { add(key to value) }.next

            assert(size == cache.size)
        }

    private inner class Node(
        val key: K,
        val value: V,
        var prev: Node? = null,
        var next: Node? = null
    )

    private fun remove(node: Node) {
        if (node.prev == null)
            head = node.next
        else
            node.prev!!.next = node.next

        if (node.next == null)
            tail = node.prev
        else
            node.next!!.prev = node.prev

        assert(head != node)
        assert(tail != node)
        assert(node.prev == null || node.prev!!.next == node.next)
        assert(node.next == null || node.next!!.prev == node.prev)
    }

    private fun addFirst(node: Node) {
        node.next = head
        node.prev = null
        head?.prev = node
        head = node
        if (tail == null)
            tail = head

        assert(head == node)
        assert(tail != null)
    }

    private fun listSize(): Int {
        var size = 0
        var cur: Node? = head
        while (cur != null) {
            cur = cur.next
            size++
        }
        return size
    }
}
