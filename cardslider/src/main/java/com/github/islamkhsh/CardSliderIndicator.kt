package com.github.islamkhsh

import android.animation.LayoutTransition
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.islamkhsh.CardSliderIndicator.IndicatorState.*
import com.github.islamkhsh.CardSliderIndicator.SwipeDirection.TO_END
import com.github.islamkhsh.CardSliderIndicator.SwipeDirection.TO_START
import com.github.islamkhsh.viewpager2.ViewPager2
import com.github.islamkhsh.viewpager2.ViewPager2.ORIENTATION_VERTICAL
import kotlin.math.min


class CardSliderIndicator : LinearLayout {

    companion object {
        const val UNLIMITED_INDICATORS = -1
    }

    private val pageChangeListener = object : ViewPager2.OnPageChangeCallback(){
        override fun onPageScrollStateChanged(state: Int) {}

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

        override fun onPageSelected(position: Int) {

            if (position > selectedPosition)
                swipeDirection = TO_END
            else if (position < selectedPosition)
                swipeDirection = TO_START

            changeIndicatorsDisplayingState(position)

            for (i in 0 until childCount) {

                if (i == position)
                    changeIndicatorState(i, selectedIndicator!!)
                else
                    changeIndicatorState(i, defaultIndicator!!)
            }

            selectedPosition = position
        }
    }

    private var selectedPosition = 0
    private var swipeDirection = TO_END
    private var displayingRang = 0..0

    internal var viewPager: CardSliderViewPager? = null
        set(value) {
            field = value
            setupWithViewCardSliderViewPager()
        }

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

    /**
     * max number of indicators to show and others will be hidden , -1 to show all
     */
    var indicatorsToShow = UNLIMITED_INDICATORS
        set(value) {
            field = value
            viewPager?.currentItem = 0
            setupWithViewCardSliderViewPager()
        }


    constructor(context: Context) : super(context) {
        initIndicatorGroup(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initIndicatorGroup(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
            context,
            attrs,
            defStyleAttr
    ) {
        initIndicatorGroup(attrs)
    }


    private fun initIndicatorGroup(attrs: AttributeSet?) {

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CardSliderIndicator)

        defaultIndicator = typedArray.getDrawable(R.styleable.CardSliderIndicator_defaultIndicator)
        selectedIndicator =
                typedArray.getDrawable(R.styleable.CardSliderIndicator_selectedIndicator)

        indicatorMargin = typedArray.getDimension(
                R.styleable.CardSliderIndicator_indicatorMargin,
                min(defaultIndicator!!.intrinsicWidth, selectedIndicator!!.intrinsicWidth).toFloat()
        )

        indicatorsToShow = typedArray.getInt(R.styleable.CardSliderIndicator_indicatorsToShow, -1)

        typedArray.recycle()

        if (indicatorsToShow != UNLIMITED_INDICATORS)
            displayingRang = 0 until indicatorsToShow

        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        layoutTransition = LayoutTransition()
    }


    private fun setupWithViewCardSliderViewPager() {

        viewPager?.adapter?.run {

            removeAllViews()

            // create indicators
            for (i in 0 until itemCount) {
                addView(Indicator(context), i)
            }

            pageChangeListener.onPageSelected(viewPager!!.currentItem)

            viewPager?.unregisterOnPageChangeCallback(pageChangeListener)
            viewPager?.registerOnPageChangeCallback(pageChangeListener)

            registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){
                override fun onChanged() {
                    setupWithViewCardSliderViewPager()
                }

                override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                    setupWithViewCardSliderViewPager()
                }

                override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                    setupWithViewCardSliderViewPager()
                }

                override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                    setupWithViewCardSliderViewPager()
                }

                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    setupWithViewCardSliderViewPager()
                }
            })
        }
    }

    private fun changeIndicatorState(indicatorPosition: Int, drawableState: Drawable) {

        (getChildAt(indicatorPosition) as Indicator).run {
            changeIndicatorDrawableState(drawableState)
            changeIndicatorAppearanceState(indicatorPosition)
        }
    }

    private fun changeIndicatorsDisplayingState(currentPosition: Int) {

        if (currentPosition == 0)
            displayingRang = 0 until indicatorsToShow
        else if (currentPosition == displayingRang.first && swipeDirection == TO_START)
            displayingRang = displayingRang.decrement()
        else if (currentPosition == displayingRang.last && swipeDirection == TO_END)
            displayingRang = displayingRang.increment(childCount - 1)
    }



    private inner class Indicator : View {

        private val infiniteScaleFactor = 0.5f

        private var state = NORMAL
            set(value) {

                field = if (indicatorsToShow == UNLIMITED_INDICATORS) NORMAL else value

                when (field) {
                    NORMAL -> {
                        layoutParams = (layoutParams as MarginLayoutParams).apply {
                            marginEnd = indicatorMargin.toInt()
                        }

                        scaleX = 1f
                        scaleY = 1f
                        visibility = VISIBLE
                    }
                    HIDDEN -> visibility = GONE
                    LAST -> {
                        layoutParams = (layoutParams as MarginLayoutParams).apply { marginEnd = 0 }

                        scaleX = 1f
                        scaleY = 1f
                        visibility = VISIBLE
                    }
                    INFINITE_START -> {
                        layoutParams = (layoutParams as MarginLayoutParams).apply {
                            marginEnd = indicatorMargin.toInt()
                        }

                        scaleX = infiniteScaleFactor
                        scaleY = infiniteScaleFactor
                        visibility = VISIBLE
                    }
                    INFINITE_END -> {
                        layoutParams = (layoutParams as MarginLayoutParams).apply { marginEnd = 0 }

                        scaleX = infiniteScaleFactor
                        scaleY = infiniteScaleFactor
                        visibility = VISIBLE
                    }
                }
            }

        constructor(context: Context) : super(context)
        constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
        constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
                context, attrs, defStyleAttr
        )

        fun changeIndicatorDrawableState(drawableState: Drawable) {
            background = drawableState
            layoutParams = LayoutParams(drawableState.intrinsicWidth, drawableState.intrinsicHeight)
        }

        fun changeIndicatorAppearanceState(indicatorPosition: Int) {

            val lastPosition = childCount - 1

            state = when {
                indicatorPosition != 0 && indicatorPosition == displayingRang.first -> INFINITE_START
                indicatorPosition != lastPosition && indicatorPosition == displayingRang.last -> INFINITE_END
                indicatorPosition == lastPosition && indicatorPosition in displayingRang -> LAST
                indicatorPosition in displayingRang -> NORMAL
                else -> HIDDEN
            }
        }
    }

    private enum class SwipeDirection { TO_END, TO_START }

    private enum class IndicatorState {
        NORMAL,
        HIDDEN,
        LAST,
        INFINITE_START,
        INFINITE_END,
    }
}