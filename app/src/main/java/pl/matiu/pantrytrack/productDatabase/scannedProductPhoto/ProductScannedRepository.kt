package pl.matiu.pantrytrack.productDatabase.scannedProductPhoto

import javax.inject.Inject

class ProductScannedRepository @Inject constructor(val productScannedDao: ProductScannedDao) {

    suspend fun getProducts(): List<ProductScannedEntity> {
        var productList = mutableListOf<ProductScannedEntity>()
        for(productEntity in productScannedDao.getAllScannedProducts()) {
            productList.add(productEntity)
        }
        return productList
    }

    suspend fun getProductByName(productScannedName: String): ProductScannedEntity? {
        return productScannedDao.getProductByProductName(productScannedName)
    }

    suspend fun addProduct(product: ProductScannedEntity) {
        productScannedDao.addScannedProduct(product)
    }

    suspend fun updateProduct(product: ProductScannedEntity) {
        productScannedDao.updateScannedProduct(product)
    }

    suspend fun deleteProduct(product: ProductScannedEntity) {
        productScannedDao.deleteScannedProduct(product)
    }
}