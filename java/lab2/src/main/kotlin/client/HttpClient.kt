package client

interface HttpClient<R> {
    fun query(query: String, startTime: Long, endTime: Long): R
}
