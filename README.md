# Card Slider [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Card%20Slider-brightgreen.svg?style=plastic)](https://android-arsenal.com/details/1/7856) [![Jitpack.io](https://jitpack.io/v/IslamKhSh/CardSlider.svg)](https://jitpack.io/#IslamKhSh/CardSlider)


This is an Android library with many features and attrs to get exactly what you need.

![preview](https://github.com/IslamKhSh/CardSlider/blob/master/card%20slider.gif)

## Demo App
[![Android app on Google Play](https://developer.android.com/images/brand/en_app_rgb_wo_45.png)](https://play.google.com/store/apps/details?id=com.github.islamkhsh.cardslider_sample)

## Card Slider components
1. [CardSliderViewPager](https://github.com/IslamKhSh/CardSlider/blob/master/cardslider/src/main/java/com/github/islamkhsh/CardSliderViewPager.kt):
Custom ViewPager2 and uses a page transformer to apply scaling action as shown in GIF.
	- As ViewPager2 is still a final class [follow this issue](https://issuetracker.google.com/issues/140751461), I added it as part of my package.
2. [CardSliderIndicator](https://github.com/IslamKhSh/CardSlider/blob/master/cardslider/src/main/java/com/github/islamkhsh/CardSliderIndicator.kt): Custom LinearLayout that that contain indicators as children views.
3. [CardSliderAdapter](https://github.com/IslamKhSh/CardSlider/blob/master/cardslider/src/main/java/com/github/islamkhsh/CardSliderAdapter.kt): Abstract class that must be extended and passed to CardSliderViewPager as its adapter.

## Features
1- Show preview of pages in left and right (or top and bottom).

2- Can resize (scale) and change opacity of the pages to make focused page larger and more focused in height as shown in GIF.

3- Can make pages auto swiped after specific time.

4- Add indicator and full customize it easily.

5- Infinite indicators like those in the Instagram app.

6- RTL Support.

7- Support vertical orientation.


## Add to project
1. Add it in your root build.gradle at the end of repositories:
```groovy
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
``` 
2. Add the dependency:
```groovy
implementation 'com.github.IslamKhSh:CardSlider:{latest_version}'
```
  Find the latest version [here](https://github.com/IslamKhSh/CardSlider/releases)
   
    
 
 ## Usage
 1. Add it to your layout:
 ```xml
  <com.github.islamkhsh.CardSliderViewPager
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:id="@+id/viewPager"
            android:layout_marginTop="24dp"
            app:cardSlider_smallScaleFactor="0.9" //scale factor of height of pages in left and right (1 if no resizing nedded)
            app:cardSlider_otherPagesWidth="24dp" // width of displayed parts of left and right pages
            app:cardSlider_pageMargin="12dp" // margin between pages
		   app:auto_slide_time="3"/>  // auto sliding time in seconds
```

2. Create your item (page) layout.

3. Extend CardSliderAdapter
```kotlin
class MovieAdapter(private val movies : ArrayList<Movie>) : CardSliderAdapter<MovieAdapter.MovieViewHolder>() {

    override fun getItemCount() = movies.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_page, parent, false)
        return MovieViewHolder(view)
    }
    
    override fun bindVH(holder: MovieViewHolder, position: Int) {
      //TODO bind item object with item layout view
    }

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
```
or using Java
```java
public class MovieAdapter extends CardSliderAdapter<MovieAdapter.MovieViewHolder> {
    
    private ArrayList<Movie> movies;
    
    public MovieAdapter(ArrayList<Movie> movies){
        this.movies = movies;
    }
    
    @Override
    public int getItemCount(){
    	return movies.getSize();
    }
    
     @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_page, parent, false);
	return new MovieViewHolder(view);
    }
    
    @Override
    public void bindVH(int position, MovieViewHolder holder) {
      //TODO bind item object with item layout view
    }
    
    class MovieViewHolder extends RecyclerView.ViewHolder {
    	
	public MovieViewHolder(View view){
	      super(view);
	}
    }
}
```

4. Add adapter to CardSliderViewPager
```kotlin
  val movies = ArrayList<Movie>().apply{
  // add items to arraylist
  }
  
  findViewById<CardSliderViewPager>(R.id.viewPager).adapter = MoviesAdapter(movies)
```
or using Java
``` java
  ArrayList<Movie> movies = ArrayList<Movie>();
    // add items to arraylist
  
  CardSliderViewPager cardSliderViewPager = (CardSliderViewPager) findViewById(R.id.viewPager);
  cardSliderViewPager.setAdapter(new MoviesAdapter(movies));
```


5- To add indicator add it to your layout
```xml
 <com.github.islamkhsh.CardSliderIndicator
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:id="@+id/indicator"
	    app:indicatorsToShow="5" />
```
And then bind it with your CardSliderViewPager
```xml
  <com.github.islamkhsh.CardSliderViewPager
            ...
            app:cardSlider_indicator="@id/indicator"/>
```


 ## Attributes List
 1- CardSliderViewPager
 
| Attribute | Description  | Default value  |
| ------------- |-------------| -----|
| `cardSlider_smallScaleFactor` | The small scale of the next and previous pages. | 1 (no resizing) |
| `cardSlider_smallAlphaFactor` | The small opacity factor of the next and previous pages. | 1 (no opacity) |
| `cardSlider_baseShadow`  | The CardView Elevation when selected. |  2dp |
| `cardSlider_minShadow` | The CardView Elevation of  next and previous cards. | baseShadow * smallScaleFactor |
| `cardSlider_pageMargin` | The space between two pages. This must be large than **baseShadow + minShadow** or it will be override. | baseShadow + minShadow |
| `cardSlider_otherPagesWidth` | The width of displayed parts from next and previous cards . | 0 |
| `cardSlider_indicator` | The id of **CardSliderIndicator** to work with this view pager. | no indicator |
| `auto_slide_time` | The time in seconds to auto sliding between pages in it | no sliding (`STOP_AUTO_SLIDING`) |

 2- CardSliderIndicator
 
 | Attribute | Description  | Default value  |
| ------------- |-------------| -----|
| `defaultIndicator` | The indicator drawable in case of not selected | [default_dot.xml](https://github.com/IslamKhSh/CardSlider/blob/master/cardslider/src/main/res/drawable/default_dot.xml) |
| `selectedIndicator`  | The indicator drawable in case of selected. |  [selected_dot.xml](https://github.com/IslamKhSh/CardSlider/blob/master/cardslider/src/main/res/drawable/selected_dot.xml) |
| `indicatorMargin` | The space between indicators | the minimum width of `defaultIndicator` and `selectedIndicator` |
| `indicatorsToShow` | The number of indicators to show and others will be hidden like Instagram app | -1 (`CardSliderIndicator.UNLIMITED_INDICATORS`) |


      Copyright [2019] [IslamKhSh]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
   
