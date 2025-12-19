package pl.matiu.pantrytrack.productDatabase.category

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CategoryDao {

    @Insert
    fun addCategory(category: CategoryEntity)

    @Query("SELECT * FROM category")
    fun getCategories(): List<CategoryEntity>
}