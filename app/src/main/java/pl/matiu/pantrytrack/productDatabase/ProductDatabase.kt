package pl.matiu.pantrytrack.productDatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import pl.matiu.pantrytrack.productDatabase.category.CategoryDao
import pl.matiu.pantrytrack.productDatabase.category.CategoryEntity
import pl.matiu.pantrytrack.productDatabase.productDetails.EnergyConverter
import pl.matiu.pantrytrack.productDatabase.productDetails.ProductDetailsDao
import pl.matiu.pantrytrack.productDatabase.productDetails.ProductDetailsEntity
import pl.matiu.pantrytrack.productDatabase.scannedProductPhoto.ProductScannedDao
import pl.matiu.pantrytrack.productDatabase.scannedProductPhoto.ProductScannedEntity

@Database(entities = [ProductEntity::class, ProductScannedEntity::class, ProductDetailsEntity::class, CategoryEntity::class], version = 2)
@TypeConverters(EnergyConverter::class)
abstract class ProductDatabase: RoomDatabase() {

    abstract fun getProductDao(): ProductDao
    abstract fun getProductScannedDao(): ProductScannedDao
    abstract fun getProductDetailsDao(): ProductDetailsDao
    abstract fun getCategoryDao(): CategoryDao
}