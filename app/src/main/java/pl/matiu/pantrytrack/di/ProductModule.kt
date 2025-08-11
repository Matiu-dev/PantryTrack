package pl.matiu.pantrytrack.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import pl.matiu.pantrytrack.productDatabase.ProductDao
import pl.matiu.pantrytrack.productDatabase.ProductDatabase
import pl.matiu.pantrytrack.productDatabase.ProductRepository
import pl.matiu.pantrytrack.productDatabase.productDetails.ProductDetailsDao
import pl.matiu.pantrytrack.productDatabase.productDetails.ProductDetailsRepository
import pl.matiu.pantrytrack.productDatabase.scannedProductPhoto.ProductScannedDao
import pl.matiu.pantrytrack.productDatabase.scannedProductPhoto.ProductScannedRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ProductModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): ProductDatabase {
        return Room.databaseBuilder(
            appContext,
            ProductDatabase::class.java,
            "Product_DB"
        ).build()
    }

    @Provides
    @Singleton
    fun provideProductDao(database: ProductDatabase): ProductDao {
        return database.getProductDao()
    }

    @Provides
    @Singleton
    fun provideProductRepository(productDao: ProductDao): ProductRepository {
        return ProductRepository(productDao)
    }

    @Provides
    @Singleton
    fun productScannedProductDao(database: ProductDatabase): ProductScannedDao {
        return database.getProductScannedDao()
    }

    @Provides
    @Singleton
    fun provideProductScannedRepository(productScannedDao: ProductScannedDao): ProductScannedRepository {
        return ProductScannedRepository(productScannedDao)
    }

    @Provides
    @Singleton
    fun productDetailsProductDetailsDao(database: ProductDatabase): ProductDetailsDao {
        return database.getProductDetailsDao()
    }

    @Provides
    @Singleton
    fun provideProductDetailsRepository(productDetailsDao: ProductDetailsDao): ProductDetailsRepository {
        return ProductDetailsRepository(productDetailsDao)
    }
}