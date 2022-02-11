package com.sheng.compose

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sheng.compose.ui.theme.AndroidTheme

/**
 * 创建人：Bobo
 * 创建时间：2022/2/10 15:18
 * 类描述：
 */
class ViewModeActivity : AppCompatActivity() {

    val model by viewModels<ComposeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidTheme {
                Surface(color = MaterialTheme.colors.background) {
                    ContentView(model)
                }
            }
        }
    }
}

@Composable
private fun ContentView(model: ComposeViewModel) {

    Column {
        ItemInput(model::addItem)
        LazyColumn {
            items(items = model.todoItems) { todo ->

            }
        }
    }
}


@Composable
private fun ItemInput(onAddItem: (TodoItem) -> Unit) {
    val (text, onTextChange) = rememberSaveable { mutableStateOf("") }
    val (icon, onIconChange) = remember { mutableStateOf(TodoIcon.Default) }
    val submit = {
        if (text.isNotBlank()) {
            onAddItem(TodoItem(text, icon))
            onTextChange("")
            onIconChange(TodoIcon.Default)
        }
    }
    Input(
        text = text,
        onTextChange = onTextChange,
        icon = icon,
        onIconChange = onIconChange,
        submit = submit,
        iconsVisible = text.isNotBlank()
    ) {
        TodoEditButton(onClick = submit, text = "add", enabled = text.isNotBlank())
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Input(
    text: String,
    onTextChange: (String) -> Unit,
    icon: TodoIcon,
    onIconChange: (TodoIcon) -> Unit,
    submit: () -> Unit,
    iconsVisible: Boolean,
    buttonSlot: @Composable () -> Unit,
) {
    Column {
        Row {
            val keyboardController = LocalSoftwareKeyboardController.current
            TextField(
                value = text,
                onValueChange = onTextChange,
                maxLines = 1,
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    submit()
                    keyboardController?.hide()
                }),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
            )
            Box(Modifier.align(CenterVertically)) {
                buttonSlot()
            }
        }
    }
}

@Composable
fun TodoEditButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    TextButton(
        onClick = onClick,
        shape = CircleShape,
        enabled = enabled,
        modifier = modifier
    ) {
        Text(text)
    }
}


@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
private fun MainPreview() {
    AndroidTheme {
        Surface(color = MaterialTheme.colors.background) {
            ItemInput() { }
        }
    }
}