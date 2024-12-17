package com.example.mymovielibrary.search.di

import com.example.mymovielibrary.search.data.repository.SearchRepositoryImpl
import com.example.mymovielibrary.search.domain.repository.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SearchRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSearchRepository(
       searchRepositoryImpl: SearchRepositoryImpl
    ): SearchRepository

}























