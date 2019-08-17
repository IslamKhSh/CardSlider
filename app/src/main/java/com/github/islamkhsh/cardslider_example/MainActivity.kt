package com.github.islamkhsh.cardslider_example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val movies = arrayListOf<Movie>()

        movies.add(
            Movie(R.drawable.harry_potter,"Harry Potter",
            "An orphaned boy enrolls in a school of wizardry, where he learns the truth about himself, his family and the terrible evil that haunts the magical world."
            ))

        movies.add(
            Movie(R.drawable.lord_of_rings,"The Lord of the Rings",
            "A meek Hobbit from the Shire and eight companions set out on a journey to destroy the powerful One Ring and save Middle-earth from the Dark Lord Sauron."
            ))

        movies.add(
            Movie(R.drawable.the_matrix,"The Matrix",
            "A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers."
            ))

        movies.add(
            Movie(R.drawable.avengers,"Avengers Assemble",
            "Earth's mightiest heroes must come together and learn to fight as a team if they are going to stop the mischievous Loki and his alien army from enslaving humanity."
            ))

        viewPager.adapter = MovieAdapter(movies)
    }
}
