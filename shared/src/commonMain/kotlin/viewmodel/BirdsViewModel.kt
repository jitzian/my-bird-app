package viewmodel

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import model.BirdImage
import repository.BirdsRepository
import repository.BirdsRepositoryImpl

data class BirdsUIState(
    val images: List<BirdImage> = emptyList(),
    val selectedCategory: String? = null,
) {
    val categories = images.map { it.category }.toSet()
    val selectedImages = images.filter { it.category == selectedCategory }
}

class BirdsViewModel : ViewModel() {
    private val _state = MutableStateFlow(BirdsUIState())
    val state = _state.asStateFlow()

    private val repository: BirdsRepository = BirdsRepositoryImpl()

    /*private val httpClient: HttpClient = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }*/

    init {
        updateImages()
    }

    override fun onCleared() {
        //httpClient.close()
        repository.closeConnection()
    }

    fun selectCategory(category: String) {
        _state.update { it.copy(selectedCategory = category) }
    }

    fun updateImages() {
        viewModelScope.launch {
            _state.update { it.copy(images = getImages()) }
        }
    }

    /*private suspend fun getImages(): List<BirdImage> {
        return httpClient
            .get("https://sebi.io/demo-image-api/pictures.json")
            .body()
    }*/

    private suspend fun getImages() = repository.getImages()


}