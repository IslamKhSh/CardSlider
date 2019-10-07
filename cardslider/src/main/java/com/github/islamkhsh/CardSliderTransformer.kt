package com.github.islamkhsh

import android.content.Context
import android.graphics.Point
import android.view.View
import android.view.WindowManager
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.ViewPager
import kotlin.math.absoluteValue


internal class CardSliderTransformer(private val viewPager: CardSliderViewPager) :
    ViewPager.PageTransformer {

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
        if (!position.isNaN()) {
            val absPosition = (position - startOffset).absoluteValue

            if (absPosition >= 1) {

                (page as CardView).cardElevation = viewPager.minShadow
                page.scaleY = viewPager.smallScaleFactor
                page.alpha = viewPager.smallAlphaFactor

            } else {
                // This will be during transformation
                (page as CardView).cardElevation =
                    scalingEquation(viewPager.minShadow, viewPager.baseShadow, absPosition)

                page.scaleY = scalingEquation(viewPager.smallScaleFactor, 1f, absPosition)
                page.alpha = scalingEquation(viewPager.smallAlphaFactor, 1f, absPosition)
            }
        }
    }


    private fun scalingEquation(minValue: Float, maxValue: Float, absPosition: Float) =
        (minValue - maxValue) * absPosition + maxValue

}
