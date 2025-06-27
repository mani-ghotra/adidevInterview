import com.android.adidevinterview.data.datasource.CountryLocalDataSource
import com.android.adidevinterview.data.datasource.CountryRemoteDataSource
import com.android.adidevinterview.domain.model.Country
import com.android.adidevinterview.domain.repository.CountryRepository
import com.android.adidevinterview.util.CountryMapper
import com.android.adidevinterview.util.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CountryRepositoryImpl @Inject constructor(
    private val remoteDataSource: CountryRemoteDataSource,
    private val localDataSource: CountryLocalDataSource,
    private val mapper: CountryMapper
) : CountryRepository {

    override fun getCountries(): Flow<Resource<List<Country>>> = flow {
        emit(Resource.Loading())

        try {
            val remoteCountries = remoteDataSource.getCountries()

            val entities = remoteCountries.mapIndexed { index, country ->
                mapper.mapToEntity(country, index)
            }

            // Validate before insertion
            val validEntities = entities.filter { entity ->
                localDataSource.countryExists(entity)
            }

            localDataSource.cacheCountries(validEntities)

            emitAll(
                localDataSource.getCountries()
                    .map { countries ->
                        if (countries.isEmpty()) {
                            Resource.Error("No data available")
                        } else {
                            Resource.Success(mapper.mapFromEntityList(countries))
                        }
                    }
            )

        } catch (e: Exception) {

            // Check if we have any cached data
            val cachedCountries = localDataSource.getCountries().firstOrNull()
            if (cachedCountries.isNullOrEmpty()) {
                emit(Resource.Error("Network error and no cached data available"))
            } else {
                emit(Resource.Error("Network error. Showing cached data"))
                delay(200)
                emit(Resource.Success(mapper.mapFromEntityList(cachedCountries)))
            }
        }
    }

    override fun searchCountries(query: String): Flow<List<Country>> {
        return localDataSource.searchCountries(query).map { mapper.mapFromEntityList(it) }
    }
}