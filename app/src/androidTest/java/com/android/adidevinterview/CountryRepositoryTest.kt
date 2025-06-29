package com.android.adidevinterview

import com.android.adidevinterview.data.repository.CountryRepositoryImpl
import com.android.adidevinterview.data.datasource.CountryLocalDataSource
import com.android.adidevinterview.data.datasource.CountryRemoteDataSource
import com.android.adidevinterview.data.local.CountryEntity
import com.android.adidevinterview.data.remote.CountryResponse
import com.android.adidevinterview.domain.model.Country
import com.android.adidevinterview.domain.repository.CountryRepository
import com.android.adidevinterview.util.CountryMapper
import com.android.adidevinterview.util.Resource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class CountryRepositoryTest {

    private lateinit var repository: CountryRepository
    private val remoteDataSource: CountryRemoteDataSource = mockk()
    private val localDataSource: CountryLocalDataSource = mockk()
    private val mapper: CountryMapper = mockk()

    @Before
    fun setup() {
        repository = CountryRepositoryImpl(
            remoteDataSource,
            localDataSource,
            mapper
        )
    }

    @Test
    fun getCountries_shouldEmitLoadingThenSuccess_whenNetworkAvailable() = runTest {
        // Given
        val testCountries = listOf(
            CountryResponse("Test1", "Region1", "Code1", "Capital1"),
            CountryResponse("Test2", "Region2", "Code2", "Capital2")
        )
        val mappedCountries = listOf(
            Country("Test1", "Region1", "Code1", "Capital1", 0),
            Country("Test2", "Region2", "Code2", "Capital2", 1)
        )

        coEvery { remoteDataSource.getCountries() } returns testCountries
        coEvery { localDataSource.cacheCountries(any()) } returns Unit
        coEvery { localDataSource.getCountries() } returns flow {
            emit(
                listOf(
                    CountryEntity(0, "Test1", "Region1", "Code1", "Capital1", 0),
                    CountryEntity(0, "Test2", "Region2", "Code2", "Capital2", 1)
                )
            )
        }
        coEvery { mapper.mapToEntity(any(), any()) } answers {
            val response = arg<CountryResponse>(0)
            val position = arg<Int>(1)
            CountryEntity(
                0,
                response.name,
                response.region,
                response.code,
                response.capital,
                position
            )
        }
        coEvery { mapper.mapFromEntityList(any()) } returns mappedCountries

        // When
        val result = repository.getCountries().toList()

        // Then
        Assert.assertTrue(result[0] is Resource.Loading)
        Assert.assertTrue(result[1] is Resource.Success)
        Assert.assertEquals(2, (result[1] as Resource.Success).data?.size)
        coVerify { localDataSource.cacheCountries(any()) }
    }

    @Test
    fun getCountries_shouldEmitError_whenNoNetworkAndNoCache() = runTest {
        // Given
        coEvery { remoteDataSource.getCountries() } throws IOException("Network error")
        coEvery { localDataSource.getCountries().first() } returns emptyList()

        // When
        val result = repository.getCountries().first()

        // Then
        Assert.assertTrue(result is Resource.Error)
        Assert.assertTrue((result as Resource.Error).message?.contains("no cached data") == true)
    }

    @Test
    fun getCountries_shouldEmitCachedDataWithError_whenNetworkFails() = runTest {
        // Given
        val cachedEntities = listOf(
            CountryEntity(0, "Cached1", "Region1", "Code1", "Capital1", 1)
        )
        val mappedCountries = listOf(
            Country("Cached1", "Region1", "Code1", "Capital1", 0)
        )

        coEvery { remoteDataSource.getCountries() } throws IOException("Network error")
        coEvery { localDataSource.getCountries() } returns flow { emit(cachedEntities) }
        coEvery { mapper.mapFromEntityList(cachedEntities) } returns mappedCountries

        // When
        val result = repository.getCountries().toList()

        // Then
        Assert.assertTrue(result[0] is Resource.Loading)
        Assert.assertTrue(result[1] is Resource.Success)
        Assert.assertTrue(result[2] is Resource.Error)
        Assert.assertEquals("Cached1", (result[1] as Resource.Success).data?.get(0)?.name)
        Assert.assertTrue((result[2] as Resource.Error).message?.contains("Network error") == true)
    }

    @Test
    fun getCountries_shouldEmitError_whenResponseIsEmpty() = runTest {
        // Given
        coEvery { remoteDataSource.getCountries() } returns emptyList()
        coEvery { localDataSource.getCountries().first() } returns emptyList()

        // When
        val result = repository.getCountries().first()

        // Then
        Assert.assertTrue(result is Resource.Error)
        Assert.assertTrue((result as Resource.Error).message?.contains("No data available") == true)
    }
}