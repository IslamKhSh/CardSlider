package com.github.islamkhsh

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import androidx.viewpager.widget.PagerAdapter
import com.duolingo.open.rtlviewpager.RtlViewPager
import kotlin.math.max


@BindingMethods(
    value = [
        BindingMethod(
            type = CardSliderViewPager::class,
            attribute = "cardSlider_pageMargin",
            method = "setSliderPageMargin"
        ),
        BindingMethod(
            type = CardSliderViewPager::class,
            attribute = "cardSlider_otherPagesWidth",
            method = "setOtherPagesWidth"
        ),
        BindingMethod(
            type = CardSliderViewPager::class,
            attribute = "cardSlider_smallScaleFactor",
            method = "setSmallScaleFactor"
        ),
        BindingMethod(
            type = CardSliderViewPager::class,
            attribute = "cardSlider_smallAlphaFactor",
            method = "setSmallAlphaFactor"
        ),
        BindingMethod(
            type = CardSliderViewPager::class,
            attribute = "cardSlider_minShadow",
            method = "setMinShadow"
        ),
        BindingMethod(
            type = CardSliderViewPager::class,
            attribute = "cardSlider_baseShadow",
            method = "setBaseShadow"
        ),
        BindingMethod(
            type = CardSliderViewPager::class,
            attribute = "cardSlider_cardBackgroundColor",
            method = "setCardBackgroundColor"
        ),
        BindingMethod(
            type = CardSliderViewPager::class,
            attribute = "cardSlider_cardCornerRadius",
            method = "setCardCornerRadius"
        )
    ]
)
class CardSliderViewPager : RtlViewPager {

    private var indicatorId = -1

    /**
     * The small scale factor, height of cards in right and left (previous and next cards)
     */
    var smallScaleFactor = 1f
        set(value) {
            field = value

            (adapter as? CardSliderAdapter<*>)?.cards?.run {

                for (position in 0 until size) {
                    if (position != currentItem)
                        get(position)?.scaleY = field
                }
            }
        }

    /**
     * The small alpha factor, opacity of cards in right and left (previous and next cards)
     */
    var smallAlphaFactor = 1f
        set(value) {
            field = value

            (adapter as? CardSliderAdapter<*>)?.cards?.run {

                for (position in 0 until size) {
                    if (position != currentItem)
                        get(position)?.alpha = field
                }
            }
        }

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
        set(value) {
            field = value
            setPageMargin()
        }

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

        smallAlphaFactor =
            typedArray.getFloat(R.styleable.CardSliderViewPager_cardSlider_smallAlphaFactor, 1f)

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
            typedArray.getDimension(R.styleable.CardSliderViewPager_cardSlider_pageMargin, baseShadow + minShadow)

        otherPagesWidth =
            typedArray.getDimension(R.styleable.CardSliderViewPager_cardSlider_otherPagesWidth, 0f)

        indicatorId = typedArray.getResourceId(R.styleable.CardSliderViewPager_cardSlider_indicator, -1)

        typedArray.recycle()

        clipToPadding = false
        overScrollMode = View.OVER_SCROLL_NEVER
        offscreenPageLimit = 3
    }

    private fun setPageMargin() {
        pageMargin = max(sliderPageMargin, baseShadow + minShadow).toInt()
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

        require(adapter is CardSliderAdapter<*>) { "adapter must be CardSliderAdapter" }

        adapter.setViewPager(this)
        super.setAdapter(adapter)

        setPageTransformer(false, CardSliderTransformer(this))

        if (indicatorId != -1)
            rootView.findViewById<CardSliderIndicator>(indicatorId)?.run {
                viewPager = this@CardSliderViewPager
            }
    }


}