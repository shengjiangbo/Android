package com.sheng.compose

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.sheng.compose.ui.theme.AndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Conversation(getLists(), this)
                }
            }
        }
    }
}

var listData = mutableListOf<Message>()

fun getLists(): List<Message> {
    listData.clear()
    listData.add(Message("Android", "Compose 列表", 0))
    listData.add(Message("Android", "Compose Layout 布局", 1))
    listData.add(Message("Android", "Compose ViewModel和LiveData", 2))
    return listData
}

@Composable
fun Conversation(messages: List<Message>, context: Context?) {
    LazyColumn {
        items(messages) { message ->
            MessageCard(message, context)
        }
    }
}

data class Message(val author: String, val body: String, val id: Int)

@Composable
fun MessageCard(msg: Message, context: Context?) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
        .padding(all = 8.dp)
        .clickable {
            context?.apply {
                if (msg.id == 0) {
                    startActivity(Intent(this, ListActivity::class.java))
                } else if (msg.id == 1) {
                    startActivity(Intent(this, LayoutActivity::class.java))
                } else if (msg.id == 2) {
                    startActivity(Intent(this, ViewModeActivity::class.java))
                }
            }
        }) {
        Image(
            painter = painterResource(id = R.mipmap.power),
            contentDescription = "Contact profile picture",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colors.secondary, CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        var isExpanded by remember { mutableStateOf(true) }
        Column(
            modifier = Modifier
//            .clickable {
//                isExpanded = !isExpanded
//            }
                .weight(1f)
        ) {
            Text(
                text = msg.author,
                color = if (isExpanded) MaterialTheme.colors.secondaryVariant else Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Surface(shape = MaterialTheme.shapes.medium, elevation = 1.dp) {
                Text(
                    text = msg.body,
                    modifier = Modifier.padding(all = 4.dp),
                    style = MaterialTheme.typography.body2
                )
            }
        }
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
        Conversation(getLists(), null)
    }
}