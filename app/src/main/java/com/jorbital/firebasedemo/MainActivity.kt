package com.jorbital.firebasedemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.jorbital.firebasedemo.ui.theme.FirebaseDemoTheme

class MainActivity : ComponentActivity() {
    private val remoteConfig by lazy { Firebase.remoteConfig }
    private var color = Color.Magenta

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 10
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            val blue = remoteConfig.getBoolean("feature_flag_blue")
            if (blue) color = Color.Blue
            setContent {
                FirebaseDemoTheme {
                    FirebaseDemoScreen(color)
                }
            }
        }
    }
}

@Composable
fun FirebaseDemoScreen(color: Color) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Firebase Demo") }) },
        content = { Greeting("Feature Flag Test", color) }
    )
}

@Composable
fun Greeting(name: String, color: Color) {
    Box(Modifier.fillMaxSize()) {
        Box(
            Modifier
                .align(Alignment.Center)
                .background(color)
                .fillMaxWidth()
                .height(100.dp))
        Text(
            text = "Hello $name!",
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FirebaseDemoTheme {
        FirebaseDemoScreen(Color.Magenta)
    }
}