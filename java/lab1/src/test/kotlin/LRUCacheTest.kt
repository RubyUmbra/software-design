import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

class LRUCacheTest {
    private lateinit var cache: Cache<Int, Int>

    @Rule
    @JvmField
    val exceptionRule: ExpectedException = ExpectedException.none()

    @Before
    fun init() {
        cache = LRUCache(4)
    }

    @SafeVarargs
    private fun <K> emulateEntries(vararg keys: K): List<Pair<K, K>> =
        keys.map { x -> x to x }

    @Test
    fun `Correctly put entries with right entry order`() {
        (1 until 4).forEach { x -> cache[x] = x }

        assertThat(cache.values(), `is`(emulateEntries(3, 2, 1)))
    }

    @Test
    fun `Correctly get existing entries`() {
        (1 until 5).forEach { x -> cache[x] = x }

        assertThat(cache[1], `is`(1))
        assertThat(cache[2], `is`(2))
        assertThat(cache[3], `is`(3))
        assertThat(cache[4], `is`(4))
    }

    @Test
    fun `Correct entry order after getting entry`() {
        (1 until 5).forEach { x -> cache[x] = x }
        cache[1]

        assertThat(cache.values(), `is`(emulateEntries(1, 4, 3, 2)))
    }

    @Test
    fun `Correctly limit cache size by capacity`() {
        (1 until 6).forEach { x -> cache[x] = x }

        assertThat(cache[1], `is`(nullValue()))
        assertThat(cache.values().size, `is`(4))
    }

    @Test
    fun `Correct order after getting multiple values`() {
        (1 until 5).forEach { x -> cache[x] = x }
        (4 downTo 1).forEach { x -> cache[x] }

        assertThat(cache.values(), `is`(emulateEntries(1, 2, 3, 4)))
    }

    @Test
    fun `Return null on non existing entry`() {
        (1 until 5).forEach { x -> cache[x] = x }

        assertThat(cache[30239559], `is`(nullValue()))
    }

    @Test
    fun `Return empty list on empty cache`() {
        assertThat(cache.values(), `is`(emptyList()))
    }

    @Test
    fun `Assert negative capacity`() {
        exceptionRule.expect(AssertionError::class.java)

        LRUCache<Int, Int>(-1)
    }

    @Test
    fun `Assert zero capacity`() {
        exceptionRule.expect(AssertionError::class.java)

        LRUCache<Int, Int>(0)
    }
}
