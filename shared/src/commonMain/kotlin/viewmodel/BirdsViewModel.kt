package viewmodel

import dev.icerock.moko.mvvm.viewmodel.ViewModel
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

    init {
        updateImages()
    }

    override fun onCleared() = repository.closeConnection()

    fun selectCategory(category: String) {
        _state.update { it.copy(selectedCategory = category) }
    }

    fun updateImages() {
        viewModelScope.launch {
            _state.update { it.copy(images = getImages()) }
        }
    }

    private suspend fun getImages() = repository.getImages()

}