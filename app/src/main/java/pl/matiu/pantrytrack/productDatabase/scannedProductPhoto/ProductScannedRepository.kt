package pl.matiu.pantrytrack.productDatabase.scannedProductPhoto

import pl.matiu.pantrytrack.product.Product
import pl.matiu.pantrytrack.productDatabase.toProduct
import pl.matiu.pantrytrack.productDatabase.toProductEntity
import javax.inject.Inject

class ProductScannedRepository @Inject constructor(val productScannedDao: ProductScannedDao) {

    suspend fun getProducts(): List<ProductScannedEntity> {
        var productList = mutableListOf<ProductScannedEntity>()
        for(productEntity in productScannedDao.getAllScannedProducts()) {
            productList.add(productEntity)
        }
        return productList
    }

    suspend fun addProduct(product: ProductScannedEntity) {
        productScannedDao.addScannedProduct(product)
    }

    suspend fun deleteProduct(product: ProductScannedEntity) {
        productScannedDao.deleteScannedProduct(product)
    }
}