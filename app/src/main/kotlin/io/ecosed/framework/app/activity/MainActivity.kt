package io.ecosed.framework.app.activity

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import io.ecosed.framework.framework.ComposeActivity
import io.ecosed.framework.ui.layout.ActivityMain
import io.ecosed.framework.ui.theme.EcosedFrameworkTheme

class MainActivity : ComposeActivity() {

    @Composable
    override fun Contents() {
        super.Contents()
        EcosedFrameworkTheme {
//            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                Text(text = "666")
//            }

//            ActivityMain()
            AndroidView(
                factory = { context ->
                    ViewPager2(context).apply {
                        adapter = object : FragmentStateAdapter(this@MainActivity) {
                            override fun getItemCount(): Int {
                                return 1
                            }

                            override fun createFragment(position: Int): Fragment {
                                return mFlutter
                            }

                        }
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}