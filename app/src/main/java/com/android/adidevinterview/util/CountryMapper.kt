package com.android.adidevinterview.util

import com.android.adidevinterview.data.local.CountryEntity
import com.android.adidevinterview.data.remote.CountryResponse
import com.android.adidevinterview.domain.model.Country

class CountryMapper {
    fun mapToEntity(response: CountryResponse, originalPosition: Int): CountryEntity {
        return CountryEntity(
            name = response.name,
            region = response.region,
            code = response.code,
            capital = response.capital,
            originalPosition = originalPosition
        )
    }

    fun mapFromEntity(entity: CountryEntity): Country {
        return Country(
            name = entity.name,
            region = entity.region,
            code = entity.code,
            capital = entity.capital,
            originalPosition = entity.originalPosition
        )
    }

    fun mapFromEntityList(entities: List<CountryEntity>): List<Country> {
        return entities.map { mapFromEntity(it) }
    }
}