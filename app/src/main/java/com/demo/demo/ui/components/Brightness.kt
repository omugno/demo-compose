package com.demo.demo.ui.components

import android.content.Context
import android.provider.Settings
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

@Composable
fun ControlBrightness(context: Context) {
    var brightness by remember {
        mutableStateOf(
            Settings.System.getInt(context.contentResolver, Settings.System.SCREEN_BRIGHTNESS)
        )
    }
    Column(
        modifier = Modifier
            .padding(all = 20.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Label("Brightness")
        Box(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .fillMaxWidth()
        ) {
            Slider(
                modifier = Modifier.semantics { contentDescription = "Brightness Description" },
                value = brightness.toFloat(),
                steps = 255,
                valueRange = 0f..255f,
                onValueChange = {
                    brightness = it.toInt()
                    Settings.System.putInt(
                        context.contentResolver,
                        Settings.System.SCREEN_BRIGHTNESS,
                        brightness
                    )
                })
        }
    }
}