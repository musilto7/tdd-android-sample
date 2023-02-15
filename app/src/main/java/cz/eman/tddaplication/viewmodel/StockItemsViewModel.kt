package cz.eman.tddaplication.viewmodel

import cz.eman.tddaplication.model.StocksItemsModel
import cz.eman.tddaplication.repository.StocksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StockItemsViewModel(
    private val repository: StocksRepository,
) {
    val model: StateFlow<StocksItemsModel> = MutableStateFlow(StocksItemsModel())
}