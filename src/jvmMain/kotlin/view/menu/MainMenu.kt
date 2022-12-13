package view.menu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import constants.Language
import constants.ScreenType
import view.navigation.AppState

@Composable
fun MainMenu() {
    Row(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().align(Alignment.CenterVertically)) {
            Row {
                Button(onClick = {
                    AppState.screenState(ScreenType.LevelMenu)
                }) {
                    Text("Select Level!")
                }
            }
            Row {
                Text(text = "Language")
            }
            Language.values().forEach { language ->
                Row {
                    Button(onClick = {
                        AppState.language(language)
                    }) {
                        Text(language.toString())
                    }
                }
            }

//            Row {
//                DropdownMenu(expanded = true, onDismissRequest = {}) {
//                    Language.values().forEach { language ->
//                        DropdownMenuItem(
//                            onClick = { GameSettings.language(language) }) {
//                            Text(text = language.toString())
//                        }
//                    }
//                }
//            }
        }
    }

}