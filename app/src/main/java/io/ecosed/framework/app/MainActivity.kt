package io.ecosed.framework.app

import android.os.Bundle
import androidx.activity.compose.setContent
import io.ecosed.framework.ui.layout.ActivityMain
import io.ecosed.framework.ui.theme.EcosedFrameworkTheme
import rikka.material.app.MaterialActivity

class MainActivity : MaterialActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EcosedFrameworkTheme {
                ActivityMain()
            }
        }
    }



}