package com.sheng.compose

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import coil.compose.rememberImagePainter
import com.sheng.compose.ui.theme.AndroidTheme

/**
 * 创建人：Bobo
 * 创建时间：2022/2/9 17:27
 * 类描述：
 */
class LayoutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    AppBar()
                }
            }
        }
    }
}

@Composable
fun AppBar() {
    var selectIcon by remember { mutableStateOf(false) }
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = "Top Title")
        }, actions = {
            IconButton(onClick = { selectIcon = !selectIcon }) {
                Icon(
                    if (selectIcon) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = null
                )
            }
        })
    }) { innerPadding ->
        LazyList(Modifier.padding(innerPadding))
    }
}

@Composable
fun LazyList(modifier: Modifier = Modifier) {
    // We save the scrolling position with this state that can also
    // be used to programmatically scroll the list
    // We save the scrolling position with this state
    val scrollState = rememberLazyListState()
    // We save the coroutine scope where our animated scroll will be executed
    val coroutineScope = rememberCoroutineScope()
//    coroutineScope.launch {//滑动到指定位置
//        scrollState.animateScrollToItem(0)
//    }
    Column {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            Text(text = "StartText",
                Modifier
                    .padding(4.dp)
                    .weight(1f)
                    .wrapContentWidth(Alignment.Start))
            Divider(color = Color.Black,modifier = Modifier.fillMaxHeight().width(1.dp))
            Text(text = "EndText",
                Modifier
                    .padding(4.dp)
                    .weight(1f)
                    .wrapContentWidth(Alignment.End))
        }
        LazyColumn(state = scrollState, modifier = modifier) {
            items(100) {
                AppContent()
            }
        }
    }
}

@Composable
private fun AppContent() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(8.dp)
            .clickable { }
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colors.surface)
            .padding(8.dp)
    ) {
        Image(
//            painter = painterResource(id = R.mipmap.power),
            painter = rememberImagePainter(
                data = "https://a.msstatic.com/huya/icenter/main/img/header_hover_6f5fb29.png"
            ),
            contentDescription = "",
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
        )
//        BodyContent()
        Column(
            modifier = Modifier
                .padding(start = 10.dp)
        ) {
            Text(text = "Title", fontSize = 16.sp, color = Color.Black)
            Text(text = "Content", fontSize = 12.sp, color = Color.Gray)
        }

        //xml中的约束布局
        BoxWithConstraints(
            modifier = Modifier
                .padding(start = 10.dp)
                .weight(1f)
        ) {
            val constraints = decoupledConstraints()

            ConstraintLayout(constraints) {
                Text(
                    modifier = Modifier.layoutId("topText"),
                    text = "ConstraintTitle",
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Text(
                    modifier = Modifier.layoutId("bottomText"),
                    text = "ConstraintContent",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

    }
}

//约束实现方式一
@Composable
fun ConstraintLayoutContent() {
    ConstraintLayout {
        val (button, text) = createRefs()

        Button(
            onClick = { /* Do something */ },
            modifier = Modifier.constrainAs(button) {
                top.linkTo(parent.top, margin = 16.dp)
            }
        ) {
            Text("Button")
        }

        Text("Text", Modifier.constrainAs(text) {
            top.linkTo(button.bottom, margin = 16.dp)
        })

    }
}

//约束实现方式二
private fun decoupledConstraints(margin: Dp = 0.dp): ConstraintSet {
    return ConstraintSet {
        val topText = createRefFor("topText")
        val bottomText = createRefFor("bottomText")

        constrain(topText) {
            top.linkTo(parent.top, margin = margin)
        }
        constrain(bottomText) {
            top.linkTo(topText.bottom, margin)
            start.linkTo(topText.start, margin = 0.dp)
        }
    }
}

@Composable
fun BodyContent(modifier: Modifier = Modifier) {
    MyOwnColumn(modifier.padding(8.dp)) {
        Text("MyOwnColumn")
        Text("places items")
        Text("vertically.")
        Text("We've done it by hand!")
    }
}

@Composable
fun MyOwnColumn(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        // Don't constrain child views further, measure them with given constraints
        // List of measured children
        val placeables = measurables.map { measurable ->
            // Measure each child
            measurable.measure(constraints)
        }

        // Track the y co-ord we have placed children up to
        var yPosition = 0

        // Set the size of the layout as big as it can
        layout(constraints.maxWidth, constraints.maxHeight) {
            // Place children in the parent layout
            placeables.forEach { placeable ->
                // Position item on the screen
                placeable.placeRelative(x = 0, y = yPosition)

                // Record the y co-ord placed up to
                yPosition += placeable.height
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
private fun LayoutActivityPreview() {
    AndroidTheme {
        AppBar()
    }
}