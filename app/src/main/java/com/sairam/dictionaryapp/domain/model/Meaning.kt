package com.sairam.dictionaryapp.domain.model

data class Meaning(
    val definition: Definition,
    val partOfSpeech: String,
    val synonyms: List<String>,
    val antonyms: List<Any>
)