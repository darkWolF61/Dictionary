package com.sairam.dictionaryapp.data.mapper

import com.sairam.dictionaryapp.data.dto.DefinitionDto
import com.sairam.dictionaryapp.data.dto.MeaningDto
import com.sairam.dictionaryapp.data.dto.WordItemDto
import com.sairam.dictionaryapp.domain.model.Definition
import com.sairam.dictionaryapp.domain.model.Meaning
import com.sairam.dictionaryapp.domain.model.WordItem

fun WordItemDto.toWordItem() = WordItem(
    word = word?: "" ,
    meanings = meanings?.map {
        it.toMeaning()
    }?: emptyList(),
    phonetic = phonetic?: ""
)

fun MeaningDto.toMeaning() = Meaning(
    definition = definitionDtoToDefinition(definitions!!.get(0)),
    partOfSpeech = partOfSpeech?: "",
    synonyms = synonyms ?: emptyList(),
    antonyms = antonyms?.mapNotNull { it as? String } ?: emptyList()
)

fun definitionDtoToDefinition(
    definitionDto: DefinitionDto
) =Definition(
    definition = definitionDto.definition?: "",
    example = definitionDto.example?: "",
    synonyms = definitionDto.synonyms ?: emptyList(),
    antonyms = definitionDto.antonyms ?: emptyList()

)
