package io.isometrik.groupstreaming.ui.hearts

import android.content.Context
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.util.AttributeSet
import io.isometrik.groupstreaming.ui.R
import io.isometrik.groupstreaming.ui.hearts.HeartsRenderer.Config

import org.rajawali3d.view.SurfaceView

/**
 * Floating hearts view for the like animation.
 *
 */
class HeartsView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
  SurfaceView(context, attrs) {

  private val renderer =
    HeartsRenderer(context)

  init {
    attrs?.also {
      context.obtainStyledAttributes(it, R.styleable.IsmHeartsView).apply {
        val xMax = getFloat(R.styleable.IsmHeartsView_x_max, HeartsRenderer.DEFAULT_CONFIG.xMax)
        val sizeCoeff =
          getFloat(R.styleable.IsmHeartsView_size_coeff, HeartsRenderer.DEFAULT_CONFIG.sizeCoeff)
        val timeCoeff = getFloat(
          R.styleable.IsmHeartsView_floating_time_coeff,
          HeartsRenderer.DEFAULT_CONFIG.floatingTimeCoeff
        )

        applyConfig(
          getConfig().copy(
            xMax = xMax,
            sizeCoeff = sizeCoeff,
            floatingTimeCoeff = timeCoeff
          )
        )

        recycle()
      }
    }

    setFrameRate(60.0)
    setZOrderOnTop(true)
    setEGLConfigChooser(8, 8, 8, 8, 16, 0)
    holder.setFormat(PixelFormat.TRANSLUCENT)
    setTransparent(true)
    setSurfaceRenderer(renderer)
  }

  /**
   * Let the heart (or any bitmap image) fly.
   *
   * @param model Model with bitmap (image) to fly and unique id for Rajawali materials caching.
   * @param maxY Magic param.
   */
  @Synchronized
  @JvmOverloads
  fun emitHeart(model: Model, maxY: Float = MAX_Y_FULL) {
    val height =
      (model.bitmap.height.toFloat() / (model.bitmap.width.toFloat() / HEART_WIDTH.toFloat())).toInt()
    val resultBitmap = Bitmap.createScaledBitmap(
      model.bitmap,
      HEART_WIDTH, height, true
    )

    renderer.emitHeart(
      resultBitmap,
      HEART_WIDTH, height, model.id, maxY
    )
  }

  fun getConfig() = renderer.getConfig()

  fun applyConfig(config: Config) = renderer.applyConfig(config)

  data class Model(val id: Int, val bitmap: Bitmap)

  companion object {

    /**
     * Magic param, calculated by experience.
     */
    const val MAX_Y_FULL = 1.5f

    /**
     * Another magic param. Bigger -> better (affects the quality of flying images).
     */
    private const val HEART_WIDTH = 100
  }
}