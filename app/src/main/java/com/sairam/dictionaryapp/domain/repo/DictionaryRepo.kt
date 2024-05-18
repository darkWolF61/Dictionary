package com.sairam.dictionaryapp.domain.repo

import com.sairam.dictionaryapp.domain.model.WordItem
import com.sairam.dictionaryapp.util.Result
import kotlinx.coroutines.flow.Flow

interface DictionaryRepo {
    suspend fun getWordResult(
        word: String
    ): Flow<Result<WordItem>>
}