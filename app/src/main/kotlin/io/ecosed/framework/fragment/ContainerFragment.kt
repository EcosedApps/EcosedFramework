package io.ecosed.framework.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.idlefish.flutterboost.containers.FlutterBoostFragment
import io.ecosed.framework.R
import io.ecosed.framework.utils.AppCompatFlutter
import io.flutter.embedding.android.RenderMode
import io.flutter.embedding.android.TransparencyMode

class ContainerFragment : Fragment() {

    private var flutterFragment: FlutterBoostFragment? = null
    private lateinit var mFragmentManager: FragmentManager
    private lateinit var mFlutterReturn: FragmentContainerView
    private lateinit var mNavigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFragmentManager = childFragmentManager
        flutterFragment = mFragmentManager.findFragmentByTag(
            tagFlutterFragment
        ) as FlutterBoostFragment?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FrameLayout(requireContext()).apply {
        addView(
            FragmentContainerView(
                context = requireContext()
            ).apply {
                visibility = View.INVISIBLE
                id = R.id.flutter_container
            }.also {
                mFlutterReturn = it
            },
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        )
    }.apply {
        addView(
            NavigationView(requireContext()).apply {
                visibility = View.VISIBLE
                setBackgroundColor(Color.TRANSPARENT)
            }.also {
                mNavigationView = it
            },
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState).run {
            mFlutterReturn.let { flutter ->
                if (flutterFragment == null) {
                    mFragmentManager.commit(
                        allowStateLoss = false,
                        body = {
                            flutterFragment = getFlutterFragment().apply {
                                add(
                                    flutter.id,
                                    this@apply,
                                    tagFlutterFragment
                                )
                            }
                        }
                    )
                }
            }
        }.run {
            mNavigationView.apply {
                inflateHeaderView(R.layout.nav_header_main)
                inflateMenu(R.menu.activity_main_drawer)
                setupWithNavController(
                    navController = findNavController()
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        flutterFragment = null
    }

    private fun getFlutterFragment(): FlutterBoostFragment {
        return if (activity is AppCompatFlutter) {
            (activity as AppCompatFlutter).getFlutterFragment()
        } else {
            FlutterBoostFragment.CachedEngineFragmentBuilder(
                ReturnFragment::class.java
            )
                .destroyEngineWithFragment(false)
                .renderMode(RenderMode.surface)
                .transparencyMode(TransparencyMode.opaque)
                .shouldAttachEngineToActivity(false)
                .build()
        }
    }

    companion object {
        const val tagFlutterFragment = "flutter_boost_fragment"
    }
}