package control

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import model.LetterChambers
import model.Word
import model.WordGame
import model.gameField.Base
import model.gameField.Enemy
import model.gameField.GameField
import util.Logger


@Composable
fun backgroundTasks(wordGame: WordGame, onGameOver: () -> Unit) {
    val backgroundScope = rememberCoroutineScope()
    backgroundScope.launch {
        spawnEnemies(gameField = wordGame.gameField)
    }
    backgroundScope.launch {
        moveEnemies(
            enemies = wordGame.gameField.enemiesOnField,
            base = wordGame.gameField.path.base,
            isOver = wordGame.isOver
        )
    }
    backgroundScope.launch {
        fireLetters(
            queue = wordGame.wordQueue,
            enemiesOnField = wordGame.gameField.enemiesOnField,
            chambers = wordGame.letterChambers,
            isOver = wordGame.isOver
        )
        Logger.LOGGER.logGameOver(wordGame)
    }
    backgroundScope.launch {
        waitForGameOver(
            isOver = wordGame.isOver,
            onGameOver = onGameOver
        )
    }
}

suspend fun moveEnemies(enemies: SnapshotStateList<Enemy>, base: Base, isOver: () -> Boolean) = coroutineScope {
    while (!isOver()) {
        enemies.forEach {
            it.move()
            if (it.distance <= 0) {
                base.health.value -= it.health.value
            }
        }
        enemies.removeIf { it.reachedEnd() }
        delay(100)
    }
}

suspend fun spawnEnemies(gameField: GameField) = coroutineScope {
    while (gameField.enemiesIncoming.isNotEmpty()) {
        gameField.enemiesIncoming.firstOrNull()?.let {
            delay(it.delay)
            gameField.enemiesOnField.add(it)
        }
        gameField.enemiesIncoming.removeFirst()
    }
}

//TODO: un-nest function
suspend fun fireLetters(
    queue: SnapshotStateList<Word>,
    enemiesOnField: SnapshotStateList<Enemy>,
    chambers: LetterChambers,
    isOver: () -> Boolean
) = coroutineScope {
    while (!isOver()) {
        if (queue.isNotEmpty() && enemiesOnField.isNotEmpty()) {
            val word = queue.removeFirst()
            while (word.size() > 0 && !isOver()) {
                if (enemiesOnField.isNotEmpty()) {
                    val letter = word.removeFirstLetter()

                    enemiesOnField.minBy { it.distance }.damage(letter.totalValue)
                    enemiesOnField.removeIf { it.health.value <= 0 }

                    Logger.LOGGER.logLetterFired(letter)

                    chambers.loadLetters(listOf(letter))
                }
                delay(300)
            }
            delay(750)
        }
        delay(250)
    }
}

suspend fun waitForGameOver(isOver: () -> Boolean, onGameOver: () -> Unit) = coroutineScope {
    while (!isOver()) {
        delay(100)
    }
    onGameOver()
}