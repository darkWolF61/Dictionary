package com.sairam.dictionaryapp.data.repo

import android.app.Application
import com.sairam.dictionaryapp.R
import com.sairam.dictionaryapp.data.api.DictionaryApi
import com.sairam.dictionaryapp.data.mapper.toWordItem
import com.sairam.dictionaryapp.domain.model.WordItem
import com.sairam.dictionaryapp.domain.repo.DictionaryRepo
import com.sairam.dictionaryapp.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class DictionaryRepoImplementation @Inject constructor(
    private val dictionaryApi: DictionaryApi,
    private val application: Application
) : DictionaryRepo {
    override suspend fun getWordResult(word: String): Flow<Result<WordItem>> {
        return flow {
            emit(Result.Loading(true))
            val remoteWordResultDto = try{
                dictionaryApi.getWordResult(word)
            }catch (e: HttpException){
                e.printStackTrace()
                emit(Result.Error(application.getString(R.string.unable_to_fetch_data)))
                emit(Result.Loading(false))
                return@flow
            }catch (e: IOException){
                e.printStackTrace()
                emit(Result.Error(application.getString(R.string.unable_to_fetch_data)))
                emit(Result.Loading(false))
                return@flow
            }catch (e: Exception){
                e.printStackTrace()
                emit(Result.Error(application.getString(R.string.unable_to_fetch_data)))
                emit(Result.Loading(false))
                return@flow
            }
            remoteWordResultDto?.let { wordResultDto ->
                wordResultDto[0]?.let { wordItemDto ->
                emit(Result.Success(wordItemDto.toWordItem()))
                emit(Result.Loading(false))
                    return@flow
                }
            }
            emit(Result.Error(application.getString(R.string.unable_to_fetch_data)))
            emit(Result.Loading(true))
        }
    }
}