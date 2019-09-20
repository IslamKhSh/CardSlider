package com.github.islamkhsh.cardslider_sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.islamkhsh.CardSliderIndicator
import com.github.islamkhsh.CardSliderViewPager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val movies = arrayListOf<Movie>()

        val posters = resources.obtainTypedArray(R.array.posters)

        for (i in resources.getStringArray(R.array.titles).indices) {
            movies.add(
                Movie(
                    posters.getResourceId(i, -1),
                    resources.getStringArray(R.array.titles)[i],
                    resources.getStringArray(R.array.overviews)[i]
                )
            )
        }

        posters.recycle()

        viewPager.adapter = MovieAdapter(movies)

        check_scaling.setOnCheckedChangeListener { _, isChecked ->
            viewPager.smallScaleFactor = if (isChecked) 0.9f else 1f
        }

        check_alpha_changing.setOnCheckedChangeListener { _, isChecked ->
            viewPager.smallAlphaFactor = if (isChecked) 0.5f else 1f
        }

        check_enable_auto_sliding.setOnCheckedChangeListener { _, isChecked ->
            viewPager.autoSlideTime =
                if (isChecked) 3 else CardSliderViewPager.STOP_AUTO_SLIDING
        }

        check_infinite_indicator.setOnCheckedChangeListener { _, isChecked ->
            indicator.indicatorsToShow =
                if (isChecked) 5 else CardSliderIndicator.UNLIMITED_INDICATORS
        }
    }
}
