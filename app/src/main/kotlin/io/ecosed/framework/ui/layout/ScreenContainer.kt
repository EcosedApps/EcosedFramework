package io.ecosed.framework.ui.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentContainerView
import com.google.android.material.appbar.MaterialToolbar
import io.ecosed.framework.R
import io.ecosed.framework.ui.preview.ScreenPreviews
import io.ecosed.framework.ui.theme.EcosedFrameworkTheme
import io.ecosed.framework.ui.widget.NavHostFactory
import io.ecosed.framework.ui.widget.TopActionBar

@Composable
fun ScreenContainer(
    topBarVisible: Boolean,
    topBarFactory: MaterialToolbar,
    container: FragmentContainerView,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
//            Image(
//                painter = painterResource(
//                    id = R.drawable.custom_wallpaper_24
//                ),
//                contentDescription = null,
//                contentScale = ContentScale.FillBounds,
//                modifier = Modifier.fillMaxSize()
//            )
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 12.dp,
                            end = 12.dp,
                            top = 12.dp,
                            bottom = 6.dp
                        ),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    TopActionBar(
                        factory = topBarFactory,
                        modifier = Modifier.fillMaxWidth(),
                        visible = topBarVisible,
                    )
                }
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 12.dp,
                            end = 12.dp,
                            top = 6.dp,
                            bottom = 12.dp
                        )
                ) {
                    NavHostFactory(
                        factory = container,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
@ScreenPreviews
fun ScreenContainerPreview() {
    EcosedFrameworkTheme {
        ScreenContainer(
            topBarVisible = true,
            topBarFactory = MaterialToolbar(
                LocalContext.current
            ).apply {
                title = stringResource(id = R.string.toolbar_preview)
                navigationIcon = ContextCompat.getDrawable(
                    LocalContext.current,
                    R.drawable.baseline_arrow_back_24
                )
            },
            container = FragmentContainerView(
                context = LocalContext.current
            )
        )
    }
}