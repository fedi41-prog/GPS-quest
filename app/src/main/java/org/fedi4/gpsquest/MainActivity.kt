package org.fedi4.gpsquest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.fedi4.gpsquest.core.ui.GPSQuestApp
import org.fedi4.gpsquest.core.ui.theme.GPSquestTheme

class MainActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GPSquestTheme {
                GPSQuestApp()
            }
        }
    }
}