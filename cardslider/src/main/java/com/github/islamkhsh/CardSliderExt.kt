package com.github.islamkhsh

import com.github.islamkhsh.viewpager2.ViewPager2

fun IntRange.increment(maxValue: Int) =
        if (endInclusive < maxValue) first + 1..endInclusive + 1 else this

fun IntRange.decrement() = if (first > 0) first - 1 until endInclusive else this


fun CardSliderViewPager.doOnPageSelected(action: () -> Unit) {

    this.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){

        override fun onPageScrollStateChanged(state: Int) {}
        override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            action()
        }
    })
}