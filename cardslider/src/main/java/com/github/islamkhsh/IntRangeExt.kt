package com.github.islamkhsh

fun IntRange.increment(maxValue: Int) =
    if (endInclusive < maxValue) first + 1..endInclusive + 1 else this

fun IntRange.decrement() = if (first > 0) first - 1 until endInclusive else this
