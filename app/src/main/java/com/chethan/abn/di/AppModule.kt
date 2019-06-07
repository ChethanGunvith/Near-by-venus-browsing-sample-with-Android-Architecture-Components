package com.chethan.abn.di

import android.app.Application
import androidx.room.Room
import com.chethan.abn.API_REST_URL
import com.chethan.abn.api.NetWorkApi
import com.chethan.abn.db.AppDatabase
import com.chethan.abn.db.VenueDao
import com.chethan.abn.db.VenueDetailsDao
import com.chethan.demoproject.utils.LiveDataCallAdapterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {
    @Singleton
    @Provides
    fun provideGithubService(): NetWorkApi {
        val retrofit =
            Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .baseUrl(API_REST_URL)
                .build()
        return retrofit.create(NetWorkApi::class.java)
    }

    @Singleton
    @Provides
    fun provideDb(app: Application): AppDatabase {
        return Room.databaseBuilder(app, AppDatabase::class.java, "venues.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideItemsDao(db: AppDatabase): VenueDao {
        return db.venueDao()
    }


    @Singleton
    @Provides
    fun provideVenueDetailsDao(db: AppDatabase): VenueDetailsDao {
        return db.venueDetailsDao()
    }

}
