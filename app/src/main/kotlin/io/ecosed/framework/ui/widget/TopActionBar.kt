package io.ecosed.framework.ui.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.shape.MaterialShapeDrawable
import io.ecosed.framework.R
import io.ecosed.framework.ui.preview.WidgetPreview
import io.ecosed.framework.ui.theme.EcosedFrameworkTheme

@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun TopActionBar(
    factory: MaterialToolbar,
    modifier: Modifier,
    visible: Boolean
) {
    val toolbarParams: AppBarLayout.LayoutParams = AppBarLayout.LayoutParams(
        AppBarLayout.LayoutParams.MATCH_PARENT,
        AppBarLayout.LayoutParams.WRAP_CONTENT
    )

    val toolbar: MaterialToolbar = factory.apply {
        setBackgroundColor(Color.Transparent.value.toInt())
    }

    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        label = ""
    ) {
        AndroidView(
            factory = { context ->
                AppBarLayout(context).apply {
                    addView(toolbar, toolbarParams)
                }
            },
            onReset = { appBar ->
                appBar.removeView(toolbar)
                appBar.addView(toolbar, toolbarParams)
            },
            modifier = modifier,
            update = { appBar ->
                appBar.setBackgroundColor(Color.Transparent.value.toInt())
                appBar.isLiftOnScroll = true
                appBar.statusBarForeground = MaterialShapeDrawable.createWithElevationOverlay(
                    appBar.context
                )
            },
            onRelease = { appBar ->
                appBar.removeView(toolbar)
            }
        )
    }
}

@Composable
@WidgetPreview
fun TopActionBarPreview() {
    EcosedFrameworkTheme {
        TopActionBar(
            factory = MaterialToolbar(LocalContext.current).apply {
                title = stringResource(id = R.string.toolbar_preview)
                navigationIcon = ContextCompat.getDrawable(
                    LocalContext.current,
                    R.drawable.baseline_arrow_back_24
                )
            },
            modifier = Modifier.fillMaxWidth(),
            visible = true
        )
    }
}