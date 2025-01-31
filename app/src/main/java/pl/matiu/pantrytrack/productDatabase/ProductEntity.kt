package pl.matiu.pantrytrack.productDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val productId: Int = 0,
    var name: String,
    var price: Double,
    var amount: Int
)
