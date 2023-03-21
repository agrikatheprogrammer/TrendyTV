package com.example.trendytv

import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONException

private const val TAG = "DetailActivity"
private const val API_KEY= "a07e22bc18f5cb106bfe4cc1f83ad8ed"
private const val PIECE="&language=en-US&page=1"
private const val END_URL="/similar?api_key=$API_KEY$PIECE"
class DetailActivity : AppCompatActivity() {
    private lateinit var tvTitle: TextView
    private lateinit var tvOverview: TextView
    private lateinit var ratingBar: RatingBar
    private lateinit var tvVoter: TextView
    private lateinit var detailRv: RecyclerView
    private var similarshows= mutableListOf<TVShow>()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        detailRv=findViewById(R.id.detailRV)
        val showadapter=TVShowAdapter(this,similarshows)
        detailRv.adapter=showadapter
        detailRv.layoutManager=LinearLayoutManager(this)
        tvTitle = findViewById(R.id.tvTitle)
        tvOverview = findViewById(R.id.tvOverview)
        ratingBar = findViewById(R.id.rbVoteAverage)
        tvVoter=findViewById(R.id.tvVoters)
        val show = intent.getParcelableExtra<TVShow>(TV_EXTRA) as TVShow
        Log.i(TAG, "TV Show is $show")
        val client = AsyncHttpClient()
        val similarshowid=show.TVId
        val similarshowURL= "https://api.themoviedb.org/3/tv/$similarshowid$END_URL"
        client.get(similarshowURL, object : JsonHttpResponseHandler(){
            override fun onFailure(statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?) {
                Log.e(TAG, "onFailure $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                Log.i(TAG, "onSuccess: JSON data $json")
                try {
                    val movieJsonArray = json.jsonObject.getJSONArray("results")
                    similarshows.addAll(TVShow.fromJsonArray(movieJsonArray))
                    showadapter.notifyDataSetChanged() //update movies
                    Log.i(TAG, "Movie list $similarshows")
                }
                catch (e: JSONException){
                    Log.e(TAG, "Encountered error $e")
                }
            }

        })
        tvTitle.text = show.title
        tvOverview.text = show.overview
        tvVoter.text=show.voterCount+" voters"
        ratingBar.rating = show.voteAverage.toFloat()

    }
}