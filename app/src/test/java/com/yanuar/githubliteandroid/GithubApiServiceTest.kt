package com.yanuar.githubliteandroid

import com.yanuar.githubliteandroid.data.remote.GithubApiService
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(JUnit4::class)
class GithubApiServiceTest {
    private lateinit var service: GithubApiService
    private lateinit var mockWebServer: MockWebServer
    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GithubApiService::class.java)
    }
    @Test
    fun searchUsers_apiSuccess() = runBlocking {
        val mockResponse = MockResponse()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .setBody("""{"total_count": 1, "items": [{"login": "Greek-Cp", "id": 82995911}]}""")
            .setResponseCode(200)
        mockWebServer.enqueue(mockResponse)
        val response = service.searchUsers("Greek-Cp")
        assertTrue(response.isSuccessful)
        assertNotNull(response.body())
        assertEquals(1, response.body()?.totalCount)
        assertEquals("Greek-Cp", response.body()?.items?.get(0)?.login)
    }
    @Test
    fun getUserDetail_apiSuccess() = runBlocking {
        val mockResponse = MockResponse()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .setBody("""{
                "login": "Greek-Cp",
                "id": 82995911,
                "name": "Yanuar Tr"
        }""")
            .setResponseCode(200)
        mockWebServer.enqueue(mockResponse)
        val response = service.getUserDetail("Greek-Cp")
        assertTrue(response.isSuccessful)
        assertNotNull(response.body())
        with(response.body()!!) {
            assertEquals("Greek-Cp", login,)
            assertEquals(82995911, id)
            assertEquals("Yanuar Tr", name)
        }
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}
