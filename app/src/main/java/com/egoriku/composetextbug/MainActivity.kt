package com.egoriku.composetextbug

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionLayoutDebugFlags
import androidx.constraintlayout.compose.MotionScene
import java.util.*

const val TEXT = "Current time: "

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Column(modifier = Modifier.fillMaxSize()) {
                    var text by remember { mutableStateOf(TEXT) }

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Button(onClick = { text = TEXT + System.currentTimeMillis() }) {
                            Text(text = "Change text")
                        }
                    }

                    MotionSwipeBug(text = text)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun MotionSwipeBug(text: String) {

    val componentHeight by remember { mutableStateOf(2000f) }
    val swipeableState = rememberSwipeableState("Bottom")
    val anchors = mapOf(0f to "Bottom", componentHeight to "Top")

    val progress = (swipeableState.offset.value / componentHeight)

    MotionLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                reverseDirection = true,
                orientation = Orientation.Vertical
            ),
        motionScene = MotionScene(
            """{
                    ConstraintSets: {
                      start: {
                        icon: {
                          width: 'wrap',
                          start: ['parent', 'start', 16],
                          bottom: ['parent', 'bottom', 16],
                          height: 'wrap'
                        },
                        title: {
                          width: 'spread',
                          height: 'wrap',
                          start: ['icon', 'end', 16],
                          end: ['parent', 'end', 16],
                          bottom: ['icon', 'bottom'],
                          top: ['icon', 'top'],
                          custom: {
                            textSize: 10
                          }
                        }
                      },
                      end: {
                        icon: {
                          width: 'wrap',
                          start: ['parent', 'start', 16],
                          bottom: ['parent', 'bottom', 16],
                          height: 'wrap'
                        },
                        title: {
                          width: 'spread',
                          height: 'wrap',
                          bottom: ['icon', 'top', 16],
                          start: ['parent', 'start', 16],
                          end: ['parent', 'end', 16],
                          custom: {
                            textSize: 25
                          }
                        }
                      }
                    }
                }"""
        ),
        debug = EnumSet.of(MotionLayoutDebugFlags.SHOW_ALL),
        progress = progress
    ) {
        Image(
            modifier = Modifier.layoutId("icon"),
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = ""
        )
        Text(
            modifier = Modifier.layoutId("title"),
            text = text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = motionFontSize("title", "textSize")
        )
    }
}