package com.egoriku.composetextbug

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
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
                    var animateToEnd by remember { mutableStateOf(false) }

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Button(onClick = { animateToEnd = !animateToEnd }) {
                            Text(text = "Run")
                        }
                        Button(onClick = { text = TEXT + System.currentTimeMillis() }) {
                            Text(text = "Change text")
                        }
                    }

                    MotionBug(
                        text = text,
                        animateToEnd = animateToEnd
                    )
                }
            }
        }
    }
}

@Composable
private fun MotionBug(text: String, animateToEnd: Boolean) {
    Log.d("bugg", text)

    val progress by animateFloatAsState(
        targetValue = if (animateToEnd) 1f else 0f,
        animationSpec = tween(2000)
    )
    MotionLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        motionScene = MotionScene(
            """{
                    ConstraintSets: {
                      start: {
                        title: {
                          width: 'wrap',
                          height: 'wrap',
                          start: ['parent', 'start', 16],
                          bottom: ['parent', 'bottom', 16]
                        }
                      },
                      end: {
                        title: {
                          width: 'wrap',
                          height: 'wrap',
                          end: ['parent', 'end', 16],
                          top: ['parent', 'top', 16]
                        }
                      }
                    }
                }"""
        ),
        debug = EnumSet.of(MotionLayoutDebugFlags.SHOW_ALL),
        progress = progress
    ) {
        Text(
            modifier = Modifier.layoutId("title"),
            text = text
        )
    }
}
