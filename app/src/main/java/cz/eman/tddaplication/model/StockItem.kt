package cz.eman.tddaplication.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StockItem(val id: String, val name: String) : Parcelable