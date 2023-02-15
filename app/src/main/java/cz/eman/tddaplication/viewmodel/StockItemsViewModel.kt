package cz.eman.tddaplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.eman.tddaplication.model.State
import cz.eman.tddaplication.model.StocksItemsModel
import cz.eman.tddaplication.repository.StocksRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StockItemsViewModel(
    private val repository: StocksRepository,
) : ViewModel() {
    val model: MutableStateFlow<StocksItemsModel> =
        MutableStateFlow(StocksItemsModel(state = State.LOADING))

    init {
        viewModelScope.launch {
            delay(50)
            model.update { it.copy(state = State.ERROR) }
        }
    }
}