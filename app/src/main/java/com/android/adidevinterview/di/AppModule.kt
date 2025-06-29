package com.android.adidevinterview.di

import com.android.adidevinterview.data.repository.CountryRepositoryImpl
import com.android.adidevinterview.data.datasource.CountryLocalDataSource
import com.android.adidevinterview.data.datasource.CountryRemoteDataSource
import com.android.adidevinterview.domain.repository.CountryRepository
import com.android.adidevinterview.util.CountryMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideCountryMapper(): CountryMapper = CountryMapper()

    @Provides
    fun provideCountryRepository(
        remoteDataSource: CountryRemoteDataSource,
        localDataSource: CountryLocalDataSource,
        mapper: CountryMapper
    ): CountryRepository {
        return CountryRepositoryImpl(remoteDataSource, localDataSource, mapper)
    }
}