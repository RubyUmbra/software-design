package searcher

import org.junit.jupiter.api.Test
import searchengine.SearchEngine
import java.time.Duration
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SearcherTest {
    @Test
    fun `Successful simple test with one engine`() {
        val query = "test1"
        val (name, _, port) = engine1

        SearcherStubServer(name, port, NO_DELAY).use { server ->
            val searcher = Searcher(listOf(engine1))
            val actual = searcher.search(query, TIMEOUT_1s)

            assertEquals(1, actual.size)
            assertTrue(actual.contains(name))
            assertEquals(server.genResponse(query), actual[name])
        }
    }

    @Test
    fun `Successful test with three engines`() {
        val query = "test2"
        val (name1, _, port1) = engine1
        val (name2, _, port2) = engine2
        val (name3, _, port3) = engine3

        SearcherStubServer(name1, port1, NO_DELAY).use {
            SearcherStubServer(name2, port2, NO_DELAY).use {
                SearcherStubServer(name3, port3, NO_DELAY).use {
                    val searcher = Searcher(listOf(engine1, engine2, engine3))
                    val actual = searcher.search(query, TIMEOUT_1s)
                    assertEquals(3, actual.size)
                    assertTrue(actual.contains(name1))
                    assertTrue(actual.contains(name2))
                    assertTrue(actual.contains(name3))
                }
            }
        }
    }

    @Test
    fun `Successful test with three engines where one not answered in timeout`() {
        val query = "test3"
        val (name1, _, port1) = engine1
        val (name2, _, port2) = engine2
        val (name3, _, port3) = engine3

        SearcherStubServer(name1, port1, NO_DELAY).use {
            SearcherStubServer(name2, port2, NO_DELAY).use {
                SearcherStubServer(name3, port3, LONG_DELAY).use {
                    val searcher = Searcher(listOf(engine1, engine2, engine3))
                    val actual = searcher.search(query, TIMEOUT_1s)
                    assertEquals(2, actual.size)
                    assertTrue(actual.contains(name1))
                    assertTrue(actual.contains(name2))
                    assertFalse(actual.contains(name3))
                }
            }
        }
    }

    @Test
    fun `Successful test with three engines where all not answered in timeout`() {
        val query = "test3"
        val (name1, _, port1) = engine1
        val (name2, _, port2) = engine2
        val (name3, _, port3) = engine3

        SearcherStubServer(name1, port1, LONG_DELAY).use {
            SearcherStubServer(name2, port2, LONG_DELAY).use {
                SearcherStubServer(name3, port3, LONG_DELAY).use {
                    val searcher = Searcher(listOf(engine1, engine2, engine3))
                    val actual = searcher.search(query, TIMEOUT_1s)
                    assertEquals(0, actual.size)
                }
            }
        }
    }

    companion object {
        private const val LOCALHOST = "localhost"
        private val engine1 = SearchEngine("e1", LOCALHOST, 12345)
        private val engine2 = SearchEngine("e2", LOCALHOST, 12346)
        private val engine3 = SearchEngine("e3", LOCALHOST, 12347)

        private val NO_DELAY: Duration = Duration.ofSeconds(0)
        private val LONG_DELAY: Duration = Duration.ofSeconds(3)

        private val TIMEOUT_1s: Duration = Duration.ofSeconds(1)
    }
}
