package com.android.adidevinterview.di

import android.content.Context
import androidx.room.Room
import com.android.adidevinterview.data.local.CountryDao
import com.android.adidevinterview.data.local.CountryDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CountryDatabase {
        return Room.databaseBuilder(
                context,
                CountryDatabase::class.java,
                "country.db"
            ).fallbackToDestructiveMigration(false).build()
    }

    @Provides
    fun provideCountryDao(database: CountryDatabase): CountryDao {
        return database.countryDao()
    }
}