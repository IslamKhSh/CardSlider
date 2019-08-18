# Card Slider
This is an amazing card slider for the Android platform with many features and attrs to get exactly what you need.

![perview](https://github.com/IslamKhSh/CardSlider/blob/master/card%20slider%201.gif)



## Card Slider components
1. [CardSliderViewPager](https://github.com/IslamKhSh/CardSlider/blob/master/cardslider/src/main/java/com/github/islamkhsh/CardSliderViewPager.kt):
A cutom ViewPager built on [RTL ViewPager](https://github.com/duolingo/rtl-viewpager) to support RTL and uses a page transformer to apply scalling action as shown in GIF.
2. [CardSliderIndicator](https://github.com/IslamKhSh/CardSlider/blob/master/cardslider/src/main/java/com/github/islamkhsh/CardSliderIndicator.kt): Custom LinearLayout that that contain indicaors as children views.
3. [CardSliderAdapter](https://github.com/IslamKhSh/CardSlider/blob/master/cardslider/src/main/java/com/github/islamkhsh/CardSliderAdapter.kt): Abtract class that must be extended and passed to CardSliderviewPager as its adapter.

## Add to project
1. Add it in your root build.gradle at the end of repositories:
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
``` 
2. Add the dependency:
```
implementation 'com.github.IslamKhSh:CardSlider:{latest_version}'
```
  Find the latest version [here](https://github.com/IslamKhSh/CardSlider/releases)
   
    
 
 ## Usage
 1. Add it to your layout:
 ```
  <com.github.islamkhsh.CardSliderViewPager
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:id="@+id/viewPager"
            android:layout_marginTop="24dp"
            app:cardSlider_smallScaleFactor="0.9" //scale factor of height of pages in left and right (1 if no resizing nedded)
            app:cardSlider_otherPagesWidth="24dp" // width of displayed parts of left and right pages
            app:cardSlider_pageMargin="12dp" // margin between pages
            app:cardSlider_cardCornerRadius="5dp"/> // corner radius of every page
```

2. Extend CardSliderAdapter
```
class MovieAdapter(movies : ArrayList<Movie>) : CardSliderAdapter<Movie>(movies) {

    override fun bindView(position: Int, itemContentView: View, item: Movie?) {
      //TODO bind item object with item layout view
    }

    override fun getItemContentLayout(position: Int) : Int {
        //TODO return the item layout of every position 
        //This layout will be added as a child of CardView
    }
}
```
3. Craeate item layout to return it in `getItemContentLayout`

4. Add adapter to CardSliderViewPager
```
  val movies = ArrayList<Movie>().apply{
  // add items to arraylist
  }
  
  findViewById<CardSliderViewPager>(R.id.viewPager).adapter = MoviesAdapter(movies)
```

5- To add indicator add it to your layout
```
 <com.github.islamkhsh.CardSliderIndicator
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:id="@+id/indicator"/>
```
And you must bind it with your CardSliderViewPager
```
  <com.github.islamkhsh.CardSliderViewPager
           .
           .
           .
            app:cardSlider_indicator="@id/indicator"/>
```


 ## Attributes List
 1- CardSliderViewPager
 
| Attribute | Description  | Default value  |
| ------------- |-------------| -----|
| `cardSlider_smallScaleFactor` | The small scale of the next and previous pages. | 1 (no resizing) |
| `cardSlider_baseShadow`  | The CardView Elevation when selected. |  2dp |
| `cardSlider_minShadow` | The CardView Elevation of  next and previous cards. | baseShadow * smallScaleFactor |
| `cardSlider_pageMargin` | The space between two pages. This must be large than **baseShadow + minShadow** or it will be overided. | baseShadow + minShadow |
| `cardSlider_otherPagesWidth` | The width of displayed parts from next and previous cards . | 0 |
| `cardSlider_cardBackgroundColor` | The background color of the card. | White |
| `cardSlider_cardCornerRadius` | The corner radius of the card view. | 0 |
| `cardSlider_indicator` | The id of **CardSliderIndicator** to work with this view pager. | no indicator |

paddingLeft and right will be overided with `otherPagesWidth + pageMargin` 

 2- CardSliderIndicator
 
 | Attribute | Description  | Default value  |
| ------------- |-------------| -----|
| `default_indicator` | The indicator drawable in case of not selected | [default_dot.xml](https://github.com/IslamKhSh/CardSlider/blob/master/cardslider/src/main/res/drawable/default_dot.xml) |
| `selected_indicator`  | The indicator drawable in case of selected. |  [selected_dot.xml](https://github.com/IslamKhSh/CardSlider/blob/master/cardslider/src/main/res/drawable/selected_dot.xml) |
| `indicator_margin` | The space between indicators | the minimum width of `default_indicator` and `selected_indicator` |
