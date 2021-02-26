package com.github.islamkhsh

import android.content.Context
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import androidx.core.util.forEach
import androidx.core.view.children
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.github.islamkhsh.viewpager2.ViewPager2
import java.util.*
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
            attribute = "auto_slide_time",
            method = "setAutoSlideTime"
        )
    ]
)
open class CardSliderViewPager : ViewPager2 {

    companion object {
        const val STOP_AUTO_SLIDING = -1
    }

    private var indicatorId = View.NO_ID

    private val recyclerViewInstance = children.first { it is RecyclerView } as RecyclerView


    /**
     * The small scale factor, height of cards in right and left (previous and next cards)
     */
    var smallScaleFactor = 1f
        set(value) {
            field = value

            (adapter as? CardSliderAdapter<*>)?.viewHolders?.forEach { position, holder ->

                if (position != currentItem)
                    holder.itemView.scaleY = field
            }
        }

    /**
     * The small alpha factor, opacity of cards in right and left (previous and next cards)
     */
    var smallAlphaFactor = 1f
        set(value) {
            field = value

            (adapter as? CardSliderAdapter<*>)?.viewHolders?.forEach { position, holder ->

                if (position != currentItem)
                    holder.itemView.alpha = field
            }
        }

    /**
     * The card shadow in case of current card
     */
    var baseShadow = 0.0f
        set(value) {
            field = value
            setPageMargin()
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
     * The auto sliding time in seconds
     */
    var autoSlideTime = STOP_AUTO_SLIDING
        set(value) {
            field = value

            initAutoSlidingTimer()
        }

    private lateinit var timer: Timer

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }


    private fun init(attrs: AttributeSet?) {

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CardSliderViewPager)

        smallScaleFactor =
            typedArray.getFloat(R.styleable.CardSliderViewPager_cardSlider_smallScaleFactor, 1f)

        smallAlphaFactor =
            typedArray.getFloat(R.styleable.CardSliderViewPager_cardSlider_smallAlphaFactor, 1f)

        baseShadow = typedArray.getDimension(
            R.styleable.CardSliderViewPager_cardSlider_baseShadow,
            context.resources.getDimension(R.dimen.baseCardElevation)
        )
        minShadow =
            typedArray.getDimension(
                R.styleable.CardSliderViewPager_cardSlider_minShadow,
                baseShadow * smallScaleFactor
            )

        sliderPageMargin =
            typedArray.getDimension(
                R.styleable.CardSliderViewPager_cardSlider_pageMargin,
                baseShadow + minShadow
            )

        otherPagesWidth =
            typedArray.getDimension(R.styleable.CardSliderViewPager_cardSlider_otherPagesWidth, 0f)

        indicatorId =
            typedArray.getResourceId(
                R.styleable.CardSliderViewPager_cardSlider_indicator,
                View.NO_ID
            )

        autoSlideTime =
            typedArray.getInt(R.styleable.CardSliderViewPager_auto_slide_time, STOP_AUTO_SLIDING)

        typedArray.recycle()

        recyclerViewInstance.clipToPadding = false
    }

    private fun setPageMargin() {
        val pageMargin = max(sliderPageMargin, baseShadow + minShadow)
        recyclerViewInstance.addItemDecoration(PageDecoration(pageMargin))
    }

    private fun setPagePadding() {
        recyclerViewInstance.run {
            val pageMargin = max(sliderPageMargin, baseShadow + minShadow).toInt()

            if (orientation == ORIENTATION_HORIZONTAL)
                setPadding(
                    otherPagesWidth.toInt() + pageMargin / 2, max(paddingTop, baseShadow.toInt()),
                    otherPagesWidth.toInt() + pageMargin / 2, max(paddingBottom, baseShadow.toInt())
                )
            else
                setPadding(
                    max(paddingLeft, baseShadow.toInt()), otherPagesWidth.toInt() + pageMargin / 2,
                    max(paddingRight, baseShadow.toInt()), otherPagesWidth.toInt() + pageMargin / 2
                )
        }

    }


    /**
     * Set a CardSliderAdapter that will supply views for this pager.
     * @param adapter PagerAdapter? instance of CardSliderAdapter
     * @throws IllegalArgumentException if adapter passed isn't a CardSliderAdapter
     */
    @Throws(IllegalArgumentException::class)
    override fun setAdapter(adapter: RecyclerView.Adapter<*>?) {
        require(adapter is CardSliderAdapter<*>) { "adapter must be CardSliderAdapter" }
        super.setAdapter(adapter)

        setPageTransformer(CardSliderTransformer(this))

        rootView.findViewById<CardSliderIndicator>(indicatorId)?.run {
            viewPager = this@CardSliderViewPager
        }

        doOnPageSelected { initAutoSlidingTimer() }
    }

    private fun initAutoSlidingTimer() {

        if (::timer.isInitialized) {
            timer.cancel()
            timer.purge()
        }

        if (autoSlideTime != STOP_AUTO_SLIDING) {
            timer = Timer()
            timer.schedule(SlidingTask(), autoSlideTime.toLong() * 1000)
        }
    }

    private inner class SlidingTask : TimerTask() {

        override fun run() {
            adapter?.run {
                Handler(Looper.getMainLooper()).post {
                    currentItem = if (currentItem == itemCount - 1) 0 else currentItem + 1
                }
            }
        }
    }

    inner class PageDecoration(private val space: Float) : ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect, view: View,
            parent: RecyclerView, state: RecyclerView.State
        ) {
            if (orientation == ORIENTATION_HORIZONTAL) {
                outRect.left = (space / 2).toInt()
                outRect.right = (space / 2).toInt()
                outRect.top = 0
                outRect.bottom = 0
            } else {
                outRect.top = (space / 2).toInt()
                outRect.bottom = (space / 2).toInt()
                outRect.left = 0
                outRect.right = 0
            }
        }

    }
}
