package com.demo.demo.ui.components

import android.media.AudioManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

data class VolumeType(val name: String, val type: Int)

@Composable
fun ControlVolume(audioManager: AudioManager, type: VolumeType) {
    var volume by remember { mutableStateOf(audioManager.getStreamVolume(type.type).toString()) }
    Column(
        modifier = Modifier
            .padding(all = 20.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Label("Volume ${type.name}")
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .fillMaxWidth()
        ) {
            Button(onClick = {
                audioManager.adjustStreamVolume(
                    type.type,
                    AudioManager.ADJUST_LOWER,
                    AudioManager.FLAG_PLAY_SOUND
                )
                volume = audioManager
                    .getStreamVolume(type.type)
                    .toString()
            }) {
                Text("-")
            }
            Box(Modifier.weight(fill = true, weight = 1f)) {
                Text(
                    text = volume,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            Button(onClick = {
                audioManager.adjustStreamVolume(
                    type.type,
                    AudioManager.ADJUST_RAISE,
                    AudioManager.FLAG_PLAY_SOUND
                )
                volume = audioManager
                    .getStreamVolume(type.type)
                    .toString()
            }) {
                Text("+")
            }
        }
    }
}