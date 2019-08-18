package com.github.islamkhsh

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import kotlin.math.min


class CardSliderIndicator : LinearLayout {

    /**
     * default indicator drawable, the background of the view if not selected
     */
    var defaultIndicator: Drawable? = null
        set(value) {
            field = value ?: ContextCompat.getDrawable(context, R.drawable.default_dot)
        }

    /**
     * selected indicator drawable, the background of the view if selected
     */
    var selectedIndicator: Drawable? = null
        set(value) {
            field = value ?: ContextCompat.getDrawable(context, R.drawable.selected_dot)
        }

    /**
     * space between one indicator and the next one
     */
    var indicatorMargin = 0f

    constructor(context: Context) : super(context) {
        initIndicatorGroup(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initIndicatorGroup(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initIndicatorGroup(attrs)
    }


    private fun initIndicatorGroup(attrs: AttributeSet?) {

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CardSliderIndicator)

        defaultIndicator = typedArray.getDrawable(R.styleable.CardSliderIndicator_default_indicator)
        selectedIndicator = typedArray.getDrawable(R.styleable.CardSliderIndicator_selected_indicator)

        indicatorMargin = typedArray.getDimension(
            R.styleable.CardSliderIndicator_indicator_margin,
            min(defaultIndicator!!.intrinsicWidth, selectedIndicator!!.intrinsicWidth).toFloat()
        )

        typedArray.recycle()

        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
    }


    internal fun setupWithViewCardSliderViewPager(viewPager: CardSliderViewPager) {

        removeAllViews()

        viewPager.adapter?.run {

            // create indicators
            for (i in 0 until count) {

                val indicator = View(context)
                addView(indicator, i)
                changeIndicatorState(i, defaultIndicator!!, count - 1)
            }

            // set selected
            changeIndicatorState(viewPager.currentItem, selectedIndicator!!)

            viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

                override fun onPageScrollStateChanged(state: Int) {}
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

                override fun onPageSelected(position: Int) {

                    for (i in 0 until childCount) {

                        if (i == position)
                            changeIndicatorState(i, selectedIndicator!!)
                        else
                            changeIndicatorState(i, defaultIndicator!!)
                    }
                }
            })
        }

    }

    private fun changeIndicatorState(position: Int, drawableState: Drawable, lastPosition: Int = childCount - 1) {

        getChildAt(position).apply {

            background = drawableState

            val parms = LayoutParams(drawableState.intrinsicWidth, drawableState.intrinsicHeight)
            if (position < lastPosition)
                parms.marginEnd = indicatorMargin.toInt()

            layoutParams = parms
        }
    }
}