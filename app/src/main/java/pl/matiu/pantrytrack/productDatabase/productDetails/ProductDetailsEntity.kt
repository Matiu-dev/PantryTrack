package pl.matiu.pantrytrack.productDatabase.productDetails

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productDetails")
data class ProductDetailsEntity (

    @PrimaryKey
    val productDetailsId: Int,
    val numberOfProtein: Double,
    val numberOfFat: Double,
    val numberOfCarbohydrate: Double,
    val numberOfSalt: Double,
    val energy: Energy,
    val type: Type
)