package pl.matiu.pantrytrack.productDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProductDao {

    @Query("SELECT * FROM products")
    fun getAllProducts(): List<ProductEntity>

    @Insert
    fun addProduct(product: ProductEntity)
}