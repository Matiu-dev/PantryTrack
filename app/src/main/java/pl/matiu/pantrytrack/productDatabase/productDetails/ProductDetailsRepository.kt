package pl.matiu.pantrytrack.productDatabase.productDetails

import android.util.Log
import pl.matiu.pantrytrack.productDatabase.scannedProductPhoto.ProductScannedDao
import pl.matiu.pantrytrack.productDatabase.scannedProductPhoto.ProductScannedEntity
import javax.inject.Inject

class ProductDetailsRepository @Inject constructor(val productDetailsDao: ProductDetailsDao) {

    suspend fun getProductDetails(): List<ProductDetailsEntity> {
        var productDetailsList = mutableListOf<ProductDetailsEntity>()
        for(productEntity in productDetailsDao.getAllProductDetails()) {
            productDetailsList.add(productEntity)
        }
        return productDetailsList
    }

    suspend fun addProductDetails(productDetails: ProductDetailsEntity) {
        productDetailsDao.addProductDetails(productDetails)
    }

    suspend fun deleteProductDetails(productDetails: ProductDetailsEntity) {
        productDetailsDao.deleteProductDetails(productDetails)
    }

    suspend fun getProductDetailsByProductName(productId: Int): ProductDetailsEntity {
        return productDetailsDao.getProductDetailsByProductId(productId)
    }

    suspend fun getProductNameByProductDetailsId(detailsId: Int): String {
        val test = productDetailsDao.getProductNameByProductDetailsId(productDetailsId = detailsId)

        return test
    }

    suspend fun deleteAllData() {
        return productDetailsDao.deleteAllData()
    }
}