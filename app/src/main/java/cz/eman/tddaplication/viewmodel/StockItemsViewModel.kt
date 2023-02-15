package cz.eman.tddaplication.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.eman.tddaplication.model.State
import cz.eman.tddaplication.model.StockItem
import cz.eman.tddaplication.model.StocksItemsModel
import cz.eman.tddaplication.repository.StocksRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StockItemsViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val repository: StocksRepository,
) : ViewModel() {

    val model: StateFlow<StocksItemsModel> = savedStateHandle.getStateFlow(
        SAVED_STATE_HANDLE_KEY, StocksItemsModel(
            state = State.LOADING,
            stockItems = null
        )
    )

    init {
        loadStocksIfNecessary()
    }

    private fun loadStocksIfNecessary() {
        if (model.value.state == State.LOADING) {
            loadStocks()
        }
    }

    private fun loadStocks() {
        viewModelScope.launch {
            val stocks = repository.getStocks()
            if (stocks != null) {
                onStocksLoadedSuccessfully(stocks)
            } else {
                onStocksLoadedError()
            }
        }
    }

    private fun onStocksLoadedSuccessfully(stocks: List<StockItem>?) {
        updateModel {
            it.copy(
                state = State.LOADED,
                stockItems = stocks
            )
        }
    }

    private fun onStocksLoadedError() {
        updateModel { it.copy(state = State.ERROR) }
    }

    private fun updateModel(transform: (StocksItemsModel) -> StocksItemsModel) {
        savedStateHandle[SAVED_STATE_HANDLE_KEY] = transform(model.value)
    }

    companion object {
        private const val SAVED_STATE_HANDLE_KEY = "StockItemsViewModel"
    }
}