package com.sairam.dictionaryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sairam.dictionaryapp.domain.model.Meaning
import com.sairam.dictionaryapp.domain.model.WordItem
import com.sairam.dictionaryapp.presentation.MainState
import com.sairam.dictionaryapp.presentation.MainUiEvents
import com.sairam.dictionaryapp.presentation.MainViewModel
import com.sairam.dictionaryapp.ui.theme.DictionaryAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DictionaryAppTheme {
                BarColor()
                val mainViewModel = hiltViewModel<MainViewModel>()
                val mainState by mainViewModel.mainState.collectAsState()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp),
                            value =mainState.searchWord,
                            onValueChange = {
                                mainViewModel.onEvent(
                                    MainUiEvents.OnSearchWordChange(it)
                                )
                            },
                            trailingIcon = {
                                Icon(imageVector = Icons.Filled.Search, contentDescription = getString(
                                    R.string.search_a_word),
                                    tint = MaterialTheme.colorScheme.primary ,
                                modifier = Modifier
                                    .size(30.dp)
                                    .clickable {
                                        mainViewModel.onEvent(MainUiEvents.OnSearchClick)
                                    }
                                )
                            },
                            label = {
                                Text(
                                    text = getString(R.string.search_a_word),
                                    fontSize = 15.sp,
                                    modifier = Modifier.alpha(0.8f)
                                )
                            },
                            textStyle = TextStyle(
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 19.sp
                            ),
                        )
                    }
                ) { paddingValues ->
                       val padding = paddingValues
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = paddingValues.calculateTopPadding())
                    ) {
                        MainScreen(mainState)
                    }
                }
            }
        }
    }

}

@Composable
private fun BarColor() {
    val systemUiController = rememberSystemUiController()
    val color = MaterialTheme.colorScheme.background
    LaunchedEffect(color) {
        systemUiController.setSystemBarsColor(color)
    }
}


    //creating compose function for the word and its phonetic
    @Composable
    fun MainScreen(
        mainState: MainState
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 30.dp)
            ) {
                mainState.wordItem?.let { wordItem ->
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = wordItem.word,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = wordItem.phonetic,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Thin,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                }

            }
        }
            Box(
                modifier = Modifier
                    .padding(top = 110.dp)
                    .fillMaxSize()
                    .clip(
                        RoundedCornerShape(
                            topStart = 50.dp,
                            topEnd = 50.dp
                        )
                    )
                    .background(MaterialTheme.colorScheme.secondaryContainer.copy(0.7f))
            ){
                if (mainState.isLoading){
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(80.dp)
                            .align(Alignment.Center),
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 1.dp
                    )
                }
                else{
                    mainState.wordItem?.let { wordItem ->
                        wordDetails(wordItem)
                    }
                }
            }
    }

@Composable
fun wordDetails(wordItem : WordItem) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 33.dp)
         ){
        items(wordItem.meanings.size){index ->
            Meaning(
                meaning = wordItem.meanings[index],
                index = index
            )
            Spacer(modifier = Modifier.height(32.dp))

        }
    }
}

@Composable
fun Meaning(
    meaning : Meaning,
    index : Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {

        Text(
            text = "${index + 1}. ${meaning.partOfSpeech}",
            fontSize = 17.sp,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primary.copy(0.4f),
                            Color.Transparent
                        )
                    )
                )
                .padding(
                    top = 2.dp, bottom = 4.dp,
                    start = 12.dp, end = 12.dp
                )
        )
        if (meaning.definition.definition.isNotEmpty()){
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.definition),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 19.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = meaning.definition.definition,
                    fontSize = 17.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

