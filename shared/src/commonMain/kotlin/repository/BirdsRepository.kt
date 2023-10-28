package repository

import io.ktor.client.HttpClient
import model.BirdImage

interface BirdsRepository {
    val httpClient: HttpClient

    suspend fun getImages(): List<BirdImage>
    fun closeConnection()

}