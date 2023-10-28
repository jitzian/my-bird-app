package repository

import constants.GlobalConstants.Companion.baseURL
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import model.BirdImage

class BirdsRepositoryImpl : BirdsRepository {
    override val httpClient: HttpClient
        get() = HttpClient {
            install(ContentNegotiation) {
                json()
            }
        }

    override suspend fun getImages(): List<BirdImage> = httpClient.get(baseURL).body()
    override fun closeConnection() = httpClient.close()
}