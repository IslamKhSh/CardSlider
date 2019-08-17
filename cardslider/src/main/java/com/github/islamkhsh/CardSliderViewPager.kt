package com.github.islamkhsh

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import androidx.viewpager.widget.PagerAdapter
import com.duolingo.open.rtlviewpager.RtlViewPager
import kotlin.math.max

class CardSliderViewPager : RtlViewPager {

    private var indicatorId = -1

    /**
     * The small scale factor, height of cards in right and left (previous and next cards) will be scaled to this factor
     */
    var smallScaleFactor = 1f

    /**
     * The card shadow in case of current card
     */
    var baseShadow = 0.0f
        set(value) {
            field = value
            setPageMargin()
            adapter?.notifyDataSetChanged()
        }

    /**
     * The card shadow in case of previous and next cards
     */
    var minShadow = baseShadow * smallScaleFactor

    /**
     * Space between pages
     */
    var sliderPageMargin = baseShadow
        set(value) {
            field = value
            setPageMargin()
        }

    /**
     * The width of displayed part from previous and next cards
     */
    var otherPagesWidth = 0f
        set(value) {
            field = value
            setPagePadding()
        }

    /**
     * The background color of the CardView
     */
    var cardBackgroundColor = Color.WHITE
        set(value) {
            field = value
            adapter?.notifyDataSetChanged()
        }

    /**
     * The corner radius of the CardView
     */
    var cardCornerRadius = 0f
        set(value) {
            field = value
            adapter?.notifyDataSetChanged()
        }


    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }


    private fun init(attrs: AttributeSet?) {

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CardSliderViewPager)

        smallScaleFactor = typedArray.getFloat(R.styleable.CardSliderViewPager_cardSlider_smallScaleFactor, 1f)

        baseShadow = typedArray.getDimension(
            R.styleable.CardSliderViewPager_cardSlider_baseShadow,
            context.resources.getDimension(R.dimen.baseCardElevation)
        )
        minShadow =
            typedArray.getDimension(R.styleable.CardSliderViewPager_cardSlider_minShadow, baseShadow * smallScaleFactor)

        cardBackgroundColor =
            typedArray.getColor(R.styleable.CardSliderViewPager_cardSlider_cardBackgroundColor, Color.WHITE)
        cardCornerRadius = typedArray.getDimension(R.styleable.CardSliderViewPager_cardSlider_cardCornerRadius, 0f)

        sliderPageMargin =
            typedArray.getDimension(R.styleable.CardSliderViewPager_cardSlider_pageMargin, baseShadow)

        otherPagesWidth =
            typedArray.getDimension(R.styleable.CardSliderViewPager_cardSlider_otherPagesWidth, 0f)

        indicatorId = typedArray.getResourceId(R.styleable.CardSliderViewPager_cardSlider_indicator, -1)

        typedArray.recycle()

        clipToPadding = false
        overScrollMode = View.OVER_SCROLL_NEVER
        offscreenPageLimit = 3
    }

    private fun setPageMargin() {
        pageMargin = max(sliderPageMargin, baseShadow).toInt()
        setPagePadding()
    }

    private fun setPagePadding() {
        setPadding(
            otherPagesWidth.toInt() + pageMargin, max(paddingTop, baseShadow.toInt()),
            otherPagesWidth.toInt() + pageMargin, max(paddingBottom, baseShadow.toInt())
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        var heightMeasure = heightMeasureSpec
        val mode = MeasureSpec.getMode(heightMeasureSpec)


        if (mode == MeasureSpec.UNSPECIFIED || mode == MeasureSpec.AT_MOST) {

            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            var height = 0
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
                val h = child.measuredHeight
                if (h > height) height = h
            }
            heightMeasure = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        }

        super.onMeasure(widthMeasureSpec, heightMeasure + paddingTop + paddingBottom)
    }


    /**
     * Set a CardSliderAdapter that will supply views for this pager.
     * @param adapter PagerAdapter? instance of CardSliderAdapter
     * @throws IllegalArgumentException if adapter passed isn't a CardSliderAdapter
     */
    @Throws(IllegalArgumentException::class)
    override fun setAdapter(adapter: PagerAdapter?) {

        if (adapter !is CardSliderAdapter<*>)
            throw IllegalArgumentException("adapter must be CardSliderAdapter")

        adapter.setViewPager(this)
        super.setAdapter(adapter)

        setPageTransformer(false, CardSliderTransformer(this))

        if (indicatorId != -1)
            rootView.findViewById<CardSliderIndicator>(indicatorId)?.run {
                setupWithViewCardSliderViewPager(this@CardSliderViewPager)
            }
    }


}