package pl.matiu.pantrytrack.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import pl.matiu.pantrytrack.api.ApiRepository
import pl.matiu.pantrytrack.api.MyApi
import pl.matiu.pantrytrack.productDatabase.ProductDao
import pl.matiu.pantrytrack.productDatabase.ProductDatabase
import pl.matiu.pantrytrack.productDatabase.ProductRepository
import pl.matiu.pantrytrack.productDatabase.category.CategoryDao
import pl.matiu.pantrytrack.productDatabase.category.CategoryRepository
import pl.matiu.pantrytrack.productDatabase.productDetails.ProductDetailsDao
import pl.matiu.pantrytrack.productDatabase.productDetails.ProductDetailsRepository
import pl.matiu.pantrytrack.productDatabase.scannedProductPhoto.ProductScannedDao
import pl.matiu.pantrytrack.productDatabase.scannedProductPhoto.ProductScannedRepository
import retrofit2.Retrofit
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
        )
            .fallbackToDestructiveMigration()
            .build()
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

    @Provides
    @Singleton
    fun categoryDao(database: ProductDatabase): CategoryDao {
        return database.getCategoryDao()
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(categoryDao: CategoryDao): CategoryRepository {
        return CategoryRepository(categoryDao)
    }

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        val retrofit: Retrofit = Retrofit.Builder().baseUrl("http://192.168.100.4:8081").build()

        return retrofit
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): MyApi {
        return retrofit.create(MyApi::class.java)
    }

    @Singleton
    @Provides
    fun provideApiRepository(myApi: MyApi, @ApplicationContext context: Context): ApiRepository {
        return ApiRepository(myApi, context)
    }
}