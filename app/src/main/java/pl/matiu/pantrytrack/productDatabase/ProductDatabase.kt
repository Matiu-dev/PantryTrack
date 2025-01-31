package pl.matiu.pantrytrack.productDatabase

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ProductEntity::class], version = 1)
abstract class ProductDatabase: RoomDatabase() {

    abstract fun getProductDao(): ProductDao
}