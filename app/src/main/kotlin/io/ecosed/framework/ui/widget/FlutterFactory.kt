package io.ecosed.framework.ui.widget

import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import io.ecosed.framework.R
import io.ecosed.framework.ui.preview.WidgetPreview
import io.ecosed.framework.ui.theme.EcosedFrameworkTheme

@Composable
fun FlutterFactory(
    factory: FrameLayout,
    modifier: Modifier
) {
    AndroidView(
        factory = {
            factory
        },
        modifier = modifier
    )
}

@Composable
@WidgetPreview
fun FlutterFactoryPreview() {
    EcosedFrameworkTheme {
        FlutterFactory(
            factory = FrameLayout(LocalContext.current).apply {
                addView(
                    TextView(LocalContext.current).apply {
                        text = stringResource(
                            id = R.string.flutter_view_preview
                        )
                        gravity = Gravity.CENTER
                    },
                    FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        Gravity.CENTER
                    )
                )
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}