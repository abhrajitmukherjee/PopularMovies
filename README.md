# Popular Movies App

Popular Movies is an android app to display trending and top rated movies in real time. It also stores favorite movies as marked by the user. The favorites section works offline based on stored data in SQLite DB. The app uses the https://www.themoviedb.org/ API.
Many frequently used concepts, features and libraries have been used in this app. Listing few below:-

 - Picasso (For image caching and processing)
 - Retrofit (A type-safe HTTP client) with GSON (Json parser by Google)
 - GridView and RecyclerView
 - Async Task (For background calls). In phase 1 had coded using Async task then discovered the beauty of Retrofit. Kept it in the code for future reference.
 - View Holder Pattern for smooth image loading and scrolling in Grid Views.
 - Material design patterns using CardLayout, FloatingActionButton.
 - Transparent ActionBar and Status Bar.
 - Using Toast to show short messages
 - SQLite DB usage with DBHelper, Contract classes.
 - Content Provider for the SQLite DB (Although not required if you are the only consumer, but a good way to learn by implementing).
 - Use of intents to launch apps like Youtube.

----------

Phone Screenshot
----------------
![Main Screen Showing Trending Movies](https://i.postimg.cc/g2P9VFHh/Movie-Main-Phone1.png)

![enter image description here](https://i.postimg.cc/bwfScrpB/Movie-Details-phone1.png)

![enter image description here](https://i.postimg.cc/NjvDJQVg/Movie-Details-phone2.png)


----------

Tablet Screenshot
-----------------

![enter image description here](https://i.postimg.cc/P5kxP7L3/Movie-Table1.png)

![enter image description here](https://i.postimg.cc/3xHXYn37/Movie-Tablet2.png)
