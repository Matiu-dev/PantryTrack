package pl.matiu.pantrytrack.productDatabase

import pl.matiu.pantrytrack.product.Product
import javax.inject.Inject

class ProductRepository @Inject constructor(private val productDao: ProductDao) {
    suspend fun getProducts(): List<Product> {
        var productList = mutableListOf<Product>()
        for(productEntity in productDao.getAllProducts()) {
            productList.add(toProduct(productEntity))
        }
        return productList
    }

    suspend fun addProduct(product: Product) {
        productDao.addProduct(toProductEntity(product))
    }
}