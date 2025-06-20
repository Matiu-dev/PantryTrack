package pl.matiu.pantrytrack.productDatabase.scannedProductPhoto

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProductScannedDao {

    @Query("SELECT * FROM scannedProducts")
    fun getAllScannedProducts(): List<ProductScannedEntity>

    @Insert
    fun addScannedProduct(product: ProductScannedEntity)

    @Delete
    fun deleteScannedProduct(product: ProductScannedEntity)
}