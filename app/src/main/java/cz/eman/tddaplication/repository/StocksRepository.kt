package cz.eman.tddaplication.repository

import cz.eman.tddaplication.model.StockItem

interface StocksRepository {
    fun getStocks() : List<StockItem>?
}