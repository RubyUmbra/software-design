package api

import client.HttpClient
import model.VKResponse
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Matchers.anyLong
import org.mockito.Matchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class TagStatisticsTest {
    @Mock
    private lateinit var client: HttpClient<VKResponse>
    private lateinit var statistics: TagStatistics

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        statistics = TagStatistics(client)
    }

    @Test
    fun baseTest() {
        Mockito.`when`(client.query(anyString(), anyLong(), anyLong()))
            .thenReturn(VKResponse(0))

        val actual = statistics.getStatistic("tag", 1)

        Assert.assertEquals(1, actual.size)
        Assert.assertEquals(0, actual[0])
    }

    @Test
    fun oneHourTest() {
        Mockito.`when`(client.query(anyString(), anyLong(), anyLong()))
            .thenReturn(VKResponse(42))

        val actual = statistics.getStatistic("tag", 1)

        Assert.assertEquals(1, actual.size)
        Assert.assertEquals(42, actual[0])
    }

    @Test
    fun test() {
        Mockito.`when`(client.query(anyString(), anyLong(), anyLong()))
            .thenReturn(VKResponse(0))
            .thenReturn(VKResponse(1))
            .thenReturn(VKResponse(2))
            .thenReturn(VKResponse(3))

        val actual = statistics.getStatistic("tag", 4)

        Assert.assertEquals(4, actual.size)

        Assert.assertEquals(0, actual[0])
        Assert.assertEquals(1, actual[1])
        Assert.assertEquals(2, actual[2])
        Assert.assertEquals(3, actual[3])
    }
}
