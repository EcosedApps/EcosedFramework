package io.ecosed.framework.ui.layout

import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlutterDash
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import io.ecosed.framework.R
import io.ecosed.framework.ui.preview.ScreenPreviews
import io.ecosed.framework.ui.theme.EcosedFrameworkTheme
import io.ecosed.framework.ui.widget.FlutterFactory

@Composable
fun ScreenFlutter(
    rootLayout: FrameLayout,
    search: () -> Unit,
    navControllerFragment: NavController
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
            Column {
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(
                            weight = 1f,
                            fill = true
                        )
                        .padding(
                            start = 12.dp,
                            top = 12.dp,
                            end = 12.dp,
                            bottom = 6.dp
                        )
                ) {
                    FlutterFactory(
                        factory = rootLayout,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 12.dp,
                            top = 6.dp,
                            end = 12.dp,
                            bottom = 12.dp
                        ),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(
                                intrinsicSize = IntrinsicSize.Min
                            )
                            .clickable {
                                search()
                            }
                    ) {
                        IconButton(
                            onClick = {
                                search()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = null
                            )
                        }
                        Column(
                            modifier = Modifier.fillMaxHeight(),
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                text = "Search...",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                        IconButton(
                            onClick = {

                                if (navControllerFragment.currentDestination?.id != R.id.nav_flutter){
                                    navControllerFragment.navigate(R.id.nav_flutter)
                                } else {

                                }

                            },
                            modifier = Modifier
                                .weight(
                                    weight = 1f,
                                    fill = true
                                )
                                .wrapContentWidth(
                                    align = Alignment.End
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Filled.FlutterDash,
                                contentDescription = null,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
@ScreenPreviews
fun ScreenFlutterPreview() {
    EcosedFrameworkTheme {
        ScreenFlutter(
            rootLayout = FrameLayout(LocalContext.current).apply {
                addView(
                    TextView(LocalContext.current).apply {
                        text = stringResource(
                            id = R.string.flutter_view_preview
                        )
                    },
                    FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        Gravity.CENTER
                    )
                )
            },
            search = {

            },
            navControllerFragment = rememberNavController()
        )
    }
}