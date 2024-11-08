package com.example.trendytv

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONException

private const val TRENDING_TV="https://api.themoviedb.org/3/tv/popular?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed&language=en-US&page=1"
private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    private val movies = mutableListOf<TVShow>()
    private lateinit var rvMovies: RecyclerView

    /*
    * 1. Define a data model class as the data source, done
    * 2. Add the recyclerview to the layout, done
    * 3. Create a custom row layout XML file to visualize the item, done
    * 4. Create an adapter and viewholder to render the item, done
    * 5. Bind the adapter to the data source to populate th recyclerview, done
    * 6. Bind a layout manager to the recyclerview, done
    * */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rvMovies = findViewById(R.id.rvTVShows)

        val movieAdapter = TVShowAdapter(this, movies)
        rvMovies.adapter = movieAdapter
        rvMovies.layoutManager = LinearLayoutManager(this) //puts movies top to bottom

        val client = AsyncHttpClient()
        client.get(TRENDING_TV, object : JsonHttpResponseHandler(){
            override fun onFailure(statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?) {
                Log.e(TAG, "onFailure $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                Log.i(TAG, "onSuccess: JSON data $json")
                try {
                    val movieJsonArray = json.jsonObject.getJSONArray("results")
                    movies.addAll(TVShow.fromJsonArray(movieJsonArray))
                    movieAdapter.notifyDataSetChanged() //update movies
                    Log.i(TAG, "Movie list $movies")
                }
                catch (e: JSONException){
                    Log.e(TAG, "Encountered error $e")
                }
            }

        })
    }
}