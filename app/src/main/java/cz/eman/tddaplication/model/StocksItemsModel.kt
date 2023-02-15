package cz.eman.tddaplication.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StocksItemsModel(
     val state: State,
     val stockItems : List<StockItem>?,
 ): Parcelable