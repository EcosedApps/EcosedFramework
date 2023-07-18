package io.ecosed.framework.app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.ComposeView
import io.ecosed.framework.framework.AppBarActivity
import io.ecosed.framework.ui.layout.ActivityMain
import io.ecosed.framework.ui.theme.EcosedFrameworkTheme
import rikka.material.app.MaterialActivity

class MainActivity : AppBarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            ComposeView(this@MainActivity).apply {
                setContent {
                    EcosedFrameworkTheme {
                        ActivityMain()
                    }
                }
            }
        )

    }



}