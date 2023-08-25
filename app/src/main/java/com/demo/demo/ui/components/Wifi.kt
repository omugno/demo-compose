package com.demo.demo.ui.components

import android.content.Context
import android.content.Intent
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WifiForm(context: Context) {
    var result by remember { mutableStateOf<Result?>(null) }
    var ssid by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    Column(
        modifier = Modifier
            .padding(all = 20.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Label("Wifi")
        TextField(
            value = ssid,
            onValueChange = { ssid = it },
            label = { Text(text = "SSID") },
            singleLine = true
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true
        )
        result?.let {
            Text(
                text = it.name,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(all = 8.dp)
                    .fillMaxWidth(),
            )
        }
        Button(onClick = {
            result = connect(context, ssid, password)
        }) {
            Text("Connect")
        }
    }
}

enum class Result { Success, Failed, Unsupported, NotFound }

fun connect(context: Context, ssid: String, ssidPassword: String): Result {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        context.startActivity(Intent(WifiManager.ACTION_PICK_WIFI_NETWORK))
        return Result.Unsupported
    }
    return runCatching {
        val wifiManager =
            context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val getConfigMethod = wifiManager.javaClass.getMethod("getWifiApConfiguration")
        val wifiConfig = getConfigMethod.invoke(wifiManager) as WifiConfiguration
        wifiConfig.SSID = "\"" + ssid + "\""
        wifiConfig.preSharedKey = "\"" + ssidPassword + "\""
        val networkId = wifiManager.addNetwork(wifiConfig)
        if (networkId != -1) {
            wifiManager.disconnect()
            wifiManager.enableNetwork(networkId, true)
            wifiManager.reconnect()
            Result.Success
        } else {
            Result.NotFound
        }
    }.onFailure {
        println(it.stackTraceToString())
    }.getOrDefault(Result.Failed)
}