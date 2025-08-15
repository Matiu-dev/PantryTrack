package pl.matiu.pantrytrack.productDatabase.scannedProductPhoto

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProductScannedDao {

    @Query("SELECT * FROM scannedProducts")
    fun getAllScannedProducts(): List<ProductScannedEntity>

    @Query("SELECT * FROM scannedProducts WHERE name =:scannedProductName")
    fun getProductByProductName(scannedProductName: String): ProductScannedEntity?

    @Insert
    fun addScannedProduct(product: ProductScannedEntity)

    @Update
    fun updateScannedProduct(product: ProductScannedEntity)

    @Delete
    fun deleteScannedProduct(product: ProductScannedEntity)
}