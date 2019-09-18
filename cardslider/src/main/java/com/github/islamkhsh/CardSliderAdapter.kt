package com.github.islamkhsh

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout.LayoutParams
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.PagerAdapter

abstract class CardSliderAdapter<T>(private val items: ArrayList<T>) : PagerAdapter() {

    internal val cards = arrayOfNulls<CardView>(count)

    private lateinit var cardSliderViewPager : CardSliderViewPager

    internal fun setViewPager(cardSliderViewPager : CardSliderViewPager) {
        this.cardSliderViewPager = cardSliderViewPager
    }

    override fun isViewFromObject(view: View, `object`: Any) = view === `object`

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
        cards[position] = null
    }

    @CallSuper
    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        // create card view as a root to item view
        val cardView = CardView(container.context)
        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        cardView.layoutParams = params

        // set card attrs
        cardView.maxCardElevation = cardSliderViewPager.baseShadow
        cardView.radius = cardSliderViewPager.cardCornerRadius
        cardView.setCardBackgroundColor(cardSliderViewPager.cardBackgroundColor)

        // get item view
        val cardContent = LayoutInflater.from(container.context)
            .inflate(getItemContentLayout(position), cardView, false)

        bindView(position, cardContent, getItem(position))

        cardView.addView(cardContent)
        container.addView(cardView)
        cards[position] = cardView

        return cardView
    }

    /**
     * @return Int The total number of pages
     */
    override fun getCount() = items.size

    /**
     * Call this method to get the item at specific position
     * @param position Int the position of the item
     * @return T? the nullable item of the passed position
     */
    open fun getItem(position: Int): T? = items[position]

    /**
     * Override it to bind the #item with the inflated view of the page layout #itemContentView
     * @param position Int the current position
     * @param itemContentView View the inflated view of #getItemContentLayout with #position
     * @param item T? the current item #getItem with #position
     */
    abstract fun bindView(position: Int, itemContentView: View, item: T?)

    /**
     * Override it to provide the page layout for every position,
     *      - this layout will be added as a child of CardView.
     *      - this layout will be inflated and passed as a View instance to bindView()
     * @param position Int the position of page
     * @return Int layout resource of the page
     */
    @LayoutRes
    abstract fun getItemContentLayout(position: Int): Int


}