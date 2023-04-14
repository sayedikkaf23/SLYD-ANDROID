package chat.hola.com.app.calling.myapplication.utility

import android.content.Context
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.Guideline
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.appscrip.myapplication.utility.Constants.Companion.NO_VALUE
import chat.hola.com.app.calling.video.call.VideosAdapter
import com.kotlintestgradle.remote.util.Constants.Companion.ONE
import com.kotlintestgradle.remote.util.Constants.Companion.QUADRA
import com.kotlintestgradle.remote.util.Constants.Companion.THRICE
import com.kotlintestgradle.remote.util.Constants.Companion.TWO
import org.webrtc.SurfaceViewRenderer
import com.appscrip.myapplication.utility.Utility

class CallingViewUtil {
    fun setViewDimen(
            listSize: Int,
            context: Context,
            localViewRenderer: SurfaceViewRenderer,
            videosAdapter: VideosAdapter?,
            rvRemoteViews: RecyclerView,
            clCallingRoot: ConstraintLayout,
            glCenterGuideLine: Guideline,
            glCenterHorizontalGuideLine: Guideline
    ) {
        when (listSize) {
            ONE -> {
                Utility.getDeviceConfiguration(context, object : Utility.DeviceConfiguration {
                    override fun deviceConfig(width: Int, height: Int) {
                        val params = localViewRenderer.layoutParams
                        params.width = width / QUADRA
                        params.height = height / QUADRA
                        localViewRenderer.layoutParams = params
                    }
                })
                rvRemoteViews.layoutManager = LinearLayoutManager(context)
                val constraintSetForRv = ConstraintSet()
                constraintSetForRv.clone(clCallingRoot)
                constraintSetForRv.connect(
                    rvRemoteViews.id,
                    ConstraintSet.BOTTOM,
                    clCallingRoot.id,
                    ConstraintSet.BOTTOM,
                    NO_VALUE
                )
                constraintSetForRv.connect(
                    rvRemoteViews.id,
                    ConstraintSet.TOP,
                    clCallingRoot.id,
                    ConstraintSet.TOP,
                    NO_VALUE
                )
                constraintSetForRv.connect(
                    rvRemoteViews.id,
                    ConstraintSet.START,
                    clCallingRoot.id,
                    ConstraintSet.START,
                    NO_VALUE
                )
                constraintSetForRv.connect(
                    rvRemoteViews.id,
                    ConstraintSet.END,
                    clCallingRoot.id,
                    ConstraintSet.END,
                    NO_VALUE
                )
                constraintSetForRv.applyTo(clCallingRoot)

                val constraintSet = ConstraintSet()
                constraintSet.clone(clCallingRoot)
                constraintSet.connect(
                    localViewRenderer.id,
                    ConstraintSet.TOP,
                    clCallingRoot.id,
                    ConstraintSet.TOP,
                    NO_VALUE
                )
                constraintSet.connect(
                    localViewRenderer.id,
                    ConstraintSet.START,
                    clCallingRoot.id,
                    ConstraintSet.START,
                    NO_VALUE
                )
                val glV = Guideline(context)
                val gllpV = ConstraintLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
                gllpV.guidePercent = 0.25f
                gllpV.orientation = LinearLayout.VERTICAL
                glV.layoutParams = gllpV
                glV.id = View.generateViewId()
                clCallingRoot.addView(glV)
                val glH = Guideline(context)
                val gllpH = ConstraintLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
                gllpH.guidePercent = 0.25f
                gllpH.orientation = LinearLayout.HORIZONTAL
                glH.layoutParams = gllpH
                glH.id = View.generateViewId()
                clCallingRoot.addView(glH)

                constraintSet.connect(
                    localViewRenderer.id,
                    ConstraintSet.END,
                    glV.id,
                    ConstraintSet.START,
                    NO_VALUE
                )
                constraintSet.connect(
                    localViewRenderer.id,
                    ConstraintSet.BOTTOM,
                    glH.id,
                    ConstraintSet.TOP,
                    NO_VALUE
                )
                constraintSet.applyTo(clCallingRoot)
            }
            TWO -> {
                Utility.getDeviceConfiguration(context, object : Utility.DeviceConfiguration {
                    override fun deviceConfig(width: Int, height: Int) {
                        val params = localViewRenderer.layoutParams
                        params.width = width
                        params.height = height / TWO
                        localViewRenderer.layoutParams = params
                    }
                })
                rvRemoteViews.layoutManager =
                        StaggeredGridLayoutManager(TWO, StaggeredGridLayoutManager.VERTICAL)
                val constraintSet = ConstraintSet()
                constraintSet.clone(clCallingRoot)
                constraintSet.connect(
                    localViewRenderer.id,
                    ConstraintSet.BOTTOM,
                    clCallingRoot.id,
                    ConstraintSet.BOTTOM,
                    NO_VALUE
                )
                constraintSet.connect(
                    localViewRenderer.id,
                    ConstraintSet.TOP,
                    glCenterHorizontalGuideLine.id,
                    ConstraintSet.BOTTOM,
                    NO_VALUE
                )
                constraintSet.connect(
                    localViewRenderer.id,
                    ConstraintSet.START,
                    clCallingRoot.id,
                    ConstraintSet.START,
                    NO_VALUE
                )
                constraintSet.connect(
                    localViewRenderer.id,
                    ConstraintSet.END,
                    clCallingRoot.id,
                    ConstraintSet.END,
                    NO_VALUE
                )
                constraintSet.applyTo(clCallingRoot)
            }
            THRICE -> {
                Utility.getDeviceConfiguration(context, object : Utility.DeviceConfiguration {
                    override fun deviceConfig(width: Int, height: Int) {
                        val params = localViewRenderer.layoutParams
                        params.width = width / TWO
                        params.height = height / TWO
                        localViewRenderer.layoutParams = params
                    }
                })
                rvRemoteViews.layoutManager =
                        StaggeredGridLayoutManager(TWO, StaggeredGridLayoutManager.VERTICAL)
                val constraintSet = ConstraintSet()
                constraintSet.clone(clCallingRoot)
                constraintSet.connect(
                    localViewRenderer.id,
                    ConstraintSet.BOTTOM,
                    clCallingRoot.id,
                    ConstraintSet.BOTTOM,
                    NO_VALUE
                )
                constraintSet.connect(
                    localViewRenderer.id,
                    ConstraintSet.END,
                    clCallingRoot.id,
                    ConstraintSet.END,
                    NO_VALUE
                )
                constraintSet.connect(
                    localViewRenderer.id,
                    ConstraintSet.START,
                    glCenterGuideLine.id,
                    ConstraintSet.END,
                    NO_VALUE
                )
                constraintSet.applyTo(clCallingRoot)
            }
        }
        videosAdapter!!.notifyDataSetChanged()
    }
}