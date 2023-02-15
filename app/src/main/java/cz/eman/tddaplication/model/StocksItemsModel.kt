package cz.eman.tddaplication.model

 data class StocksItemsModel(
     val state: State,
     val stockItems : List<StockItem>?,
 )