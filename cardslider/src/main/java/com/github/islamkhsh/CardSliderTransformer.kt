package com.github.islamkhsh

import android.content.Context
import android.graphics.Point
import android.view.View
import android.view.WindowManager
import androidx.core.view.ViewCompat
import com.github.islamkhsh.viewpager2.ViewPager2
import kotlin.math.absoluteValue


internal class CardSliderTransformer(private val viewPager: CardSliderViewPager) :
    ViewPager2.PageTransformer {

    private val startOffset: Float

    init {
        val windowManager = viewPager.context
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager

        val screen = Point()
        windowManager.defaultDisplay.getSize(screen)

        val horizontalPadding = viewPager.paddingEnd + viewPager.paddingStart
        startOffset = ((horizontalPadding / 2).toFloat() / (screen.x - horizontalPadding).toFloat())
    }

    override fun transformPage(page: View, position: Float) {

        if (position.isNaN())
            return

        val absPosition = (position - startOffset).absoluteValue

        if (absPosition >= 1) {
            ViewCompat.setElevation(page, viewPager.minShadow)
            page.alpha = viewPager.smallAlphaFactor

            if (viewPager.orientation == ViewPager2.ORIENTATION_HORIZONTAL){
                page.scaleY = viewPager.smallScaleFactor
                page.scaleX = 1f
            } else {
                page.scaleY = 1f
                page.scaleX = viewPager.smallScaleFactor
            }

        } else {
            // This will be during transformation
            ViewCompat.setElevation(
                page, scalingEquation(viewPager.minShadow, viewPager.baseShadow, absPosition)
            )

            page.alpha = scalingEquation(viewPager.smallAlphaFactor, 1f, absPosition)

            if (viewPager.orientation == ViewPager2.ORIENTATION_HORIZONTAL){
                page.scaleY = scalingEquation(viewPager.smallScaleFactor, 1f, absPosition)
                page.scaleX = 1f
            } else {
                page.scaleY = 1f
                page.scaleX = scalingEquation(viewPager.smallScaleFactor, 1f, absPosition)
            }
        }
    }


    private fun scalingEquation(minValue: Float, maxValue: Float, absPosition: Float) =
        (minValue - maxValue) * absPosition + maxValue

}
