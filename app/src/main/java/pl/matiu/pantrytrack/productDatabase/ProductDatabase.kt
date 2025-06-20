package pl.matiu.pantrytrack.productDatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import pl.matiu.pantrytrack.productDatabase.scannedProductPhoto.ProductScannedDao
import pl.matiu.pantrytrack.productDatabase.scannedProductPhoto.ProductScannedEntity

@Database(entities = [ProductEntity::class, ProductScannedEntity::class], version = 1)
abstract class ProductDatabase: RoomDatabase() {

    abstract fun getProductDao(): ProductDao
    abstract fun getProductScannedDao(): ProductScannedDao
}