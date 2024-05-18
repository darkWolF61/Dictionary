package com.sairam.dictionaryapp.presentation

import com.sairam.dictionaryapp.domain.model.WordItem

data class MainState(
    val isLoading : Boolean = false,
    val searchWord: String = "",
    val wordItem: WordItem? = null
)
