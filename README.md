# Popular Movies App

Popular Movies is an android app to display trending and top rated movies in real time. It also stores favorite movies as marked by the user. The favorites section works offline based on stored data in SQLite DB. The app uses the https://www.themoviedb.org/ API.
Lot of frequently used concepts, features and libraries. Listing few below:-

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
![Main Screen Showing Trending Movies](https://s5.postimg.org/qv5talp2v/Movie_Main_Phone1.png)

![enter image description here](https://s5.postimg.org/kewukigjb/Movie_Details_phone1.png)

![enter image description here](https://s5.postimg.org/7c1818qbb/Movie_Details_phone2.png)


----------

Tablet Screenshot
-----------------

![enter image description here](https://s5.postimg.org/7rchu9c8n/Movie_Table1.png)

![enter image description here](https://s5.postimg.org/gnn9y72uv/Movie_Tablet2.png)
