package pl.matiu.pantrytrack.productDatabase.category

import pl.matiu.pantrytrack.productDatabase.productDetails.ProductDetailsDao
import javax.inject.Inject

class CategoryRepository @Inject constructor(val categoryDao: CategoryDao){
    fun saveCategories(category: CategoryEntity) {
        categoryDao.addCategory(category)
    }

    fun getCategories(): List<CategoryEntity> {
        return categoryDao.getCategories()
    }
}