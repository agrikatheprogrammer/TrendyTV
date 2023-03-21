package com.example.trendytv

import android.os.Parcelable
import org.json.JSONArray
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize

data class TVShow (
    val TVId: Int,
    val voteAverage: Double,
    private val posterPath: String,
    val title: String,
    val overview: String,
    val voterCount: String,
) : Parcelable
{
    @IgnoredOnParcel
    val posterImageURL = "https://image.tmdb.org/t/p/w342/$posterPath"
    companion object {
        fun fromJsonArray(TVJsonArray: JSONArray): List<TVShow> {
            val tvshows = mutableListOf<TVShow>()
            for(i in 0 until TVJsonArray.length()){
                val TVJson = TVJsonArray.getJSONObject(i)
                tvshows.add(
                    TVShow(
                        TVJson.getInt("id"),
                        TVJson.getDouble("vote_average"),
                        TVJson.getString("poster_path"),
                        TVJson.getString("name"),
                        TVJson.getString("overview"),
                        TVJson.getString("vote_count")
                    )
                )
            }
            return tvshows
        }
    }
}