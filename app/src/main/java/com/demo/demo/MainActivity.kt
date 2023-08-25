package com.demo.demo

import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.demo.demo.ui.components.ControlBrightness
import com.demo.demo.ui.components.ControlVolume
import com.demo.demo.ui.components.VolumeType
import com.demo.demo.ui.components.WifiForm
import com.demo.demo.ui.theme.TestingTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        audioManager = applicationContext.getSystemService(AUDIO_SERVICE) as AudioManager
        setContent {
            TestingTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier
                            .padding(all = 20.dp)
                            .verticalScroll(rememberScrollState())
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Welcome()
                        ControlVolume(audioManager, VolumeType("Music", AudioManager.STREAM_MUSIC))
                        ControlVolume(
                            audioManager,
                            VolumeType("Voice", AudioManager.STREAM_VOICE_CALL)
                        )
                        ControlVolume(
                            audioManager,
                            VolumeType("System", AudioManager.STREAM_SYSTEM)
                        )
                        ControlVolume(audioManager, VolumeType("Ring", AudioManager.STREAM_RING))
                        ControlVolume(audioManager, VolumeType("Alarm", AudioManager.STREAM_ALARM))
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            ControlVolume(
                                audioManager,
                                VolumeType("Accessibility", AudioManager.STREAM_ACCESSIBILITY)
                            )
                        }
                        ControlBrightness(applicationContext)
                        WifiForm(applicationContext)
                    }
                }
            }
        }
    }

    override fun onStart() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.CHANGE_WIFI_STATE), 1)
            }
        }
        super.onStart()
    }

    private lateinit var audioManager: AudioManager
}

@Composable
fun Welcome(modifier: Modifier = Modifier) {
    Text(
        text = "Welcome to demo manager",
        textAlign = TextAlign.Center,
        fontSize = TextUnit(14f, TextUnitType.Sp),
        modifier = modifier.padding(bottom = 12.dp)
    )
}

