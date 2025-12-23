package pl.matiu.pantrytrack.productDatabase.productDetails

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import pl.matiu.pantrytrack.productDatabase.scannedProductPhoto.ProductScannedEntity

@Dao
interface ProductDetailsDao {

    @Query("SELECT * FROM productDetails")
    fun getAllProductDetails(): List<ProductDetailsEntity>

    @Query("SELECT * FROM productDetails WHERE productDetailsId= :productDetailsId")
    fun getProductDetailsByProductId(productDetailsId: Int): ProductDetailsEntity

    @Query("DELETE FROM productDetails")
    fun deleteAllData()

    @Query("SELECT productName FROM productDetails WHERE productDetailsId=:productDetailsId")
    fun getProductNameByProductDetailsId(productDetailsId: Int): String

    @Insert
    fun addProductDetails(productDetails: ProductDetailsEntity)

    @Delete
    fun deleteProductDetails(productDetails: ProductDetailsEntity)
}