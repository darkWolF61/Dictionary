package com.sairam.dictionaryapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sairam.dictionaryapp.domain.repo.DictionaryRepo
import com.sairam.dictionaryapp.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MainViewModel @Inject constructor(
    private val dictionaryRepo: DictionaryRepo
) : ViewModel() {
    private val _mainState = MutableStateFlow(MainState())

    val mainState = _mainState.asStateFlow()

    private var searchJob: Job? = null

    init {
        _mainState.update {
            it.copy(searchWord = "love")
        }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            loadWorsResult()
        }
    }
    fun onEvent(mainUiEvent: MainUiEvents){
        when(mainUiEvent){
            MainUiEvents.OnSearchClick -> {
/*if youor are already searching for a word, & while results are loading, and now if you are
searching for a new word, then .cancel() function cancels the previous operation and initialized
a new one. its nullable so if there are no previous tasks, it still manages */
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    loadWorsResult()
                }

            }
            is MainUiEvents.OnSearchWordChange -> {
                _mainState.update {
                    it.copy(searchWord = mainUiEvent.newWord.lowercase())
                }
            }
        }
    }

    private fun loadWorsResult(){
        viewModelScope.launch {
            dictionaryRepo.getWordResult(mainState.value.searchWord)
                .collect{result ->
                    when(result){
                        is Result.Error -> Unit
                        is Result.Loading -> {
                            _mainState.update {
                                it.copy(isLoading = result.isLoading)
                            }
                        }
                        is Result.Success -> {
                            result.data?.let { wordItem ->
                                _mainState.update {
                                    it.copy(
                                        wordItem =wordItem
                                    )
                                }
                            }

                        }
                    }
                }
        }
    }
}