package com.sairam.dictionaryapp.presentation
/* allowing user to make changes on the UI
changing search word from  hello-> helloo
 or allowing user to click the search button*/

sealed class MainUiEvents{
    data class OnSearchWordChange(val newWord: String): MainUiEvents()
    object OnSearchClick: MainUiEvents()
}
