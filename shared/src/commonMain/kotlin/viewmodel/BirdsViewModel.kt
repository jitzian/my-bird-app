package viewmodel

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import model.BirdImage
import repository.BirdsRepository
import repository.BirdsRepositoryImpl
import kotlin.time.Duration.Companion.seconds

data class BirdsUIState(
    val images: List<BirdImage> = emptyList(),
    val selectedCategory: String? = null,
    val isLoading: Boolean = false,
) {
    val categories = images.map { it.category }.toSet()
    val selectedImages = images.filter { it.category == selectedCategory }
}

class BirdsViewModel : ViewModel() {
    private val _state = MutableStateFlow(BirdsUIState())
    val state = _state.asStateFlow()

    private val repository: BirdsRepository = BirdsRepositoryImpl()

    init {
        updateImages()
    }

    override fun onCleared() = repository.closeConnection()

    fun selectCategory(category: String) {
        _state.update { it.copy(selectedCategory = category) }
    }

    private fun updateImages() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }
        delay(2.seconds)
        _state.update {
            it.copy(
                images = getImages(),
                isLoading = false,
            )
        }
    }

    private suspend fun getImages() = repository.getImages()

}