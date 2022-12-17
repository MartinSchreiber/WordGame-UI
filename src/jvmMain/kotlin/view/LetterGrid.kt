package view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.PointerMatcher
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.onClick
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerButton
import model.Letter

@Composable
fun LetterGrid(
    letters: SnapshotStateList<Letter>,
    title: String,
    subTitle: String,
    lettersPerRow: Int = 10,
    onClick: (Int, Boolean) -> Unit
) {
    Text(text = "----$title--------")
    Text(text = "------------------")
    Text(text = "----$subTitle--------")
    LazyVerticalGrid(columns = GridCells.Fixed(lettersPerRow)) {
        items(letters.size) {
            ClickableLetter(letters[it]) { rightMouseBtn: Boolean -> onClick(it, rightMouseBtn) }
        }
    }
    Text(text = "------------------")
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ClickableLetter(letter: Letter, onClick: (Boolean) -> Unit) {
    Box(modifier = Modifier
        .onClick(matcher = PointerMatcher.mouse(PointerButton.Secondary), onClick = { onClick(true) })
        .onClick { onClick(false) }
    ) {
        Text(text = letter.toString(), modifier = Modifier.align(Alignment.Center))
    }
}