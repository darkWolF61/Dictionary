package com.sairam.dictionaryapp.di
//injecting repo
import com.sairam.dictionaryapp.data.repo.DictionaryRepoImplementation
import com.sairam.dictionaryapp.domain.repo.DictionaryRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {
    // binding repo
    @Binds
    @Singleton
    abstract fun bindDictionaryRepo(
        dictionaryRepoImplementation: DictionaryRepoImplementation
    ) : DictionaryRepo
}