package com.example.movies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
 
import androidx.room.PrimaryKey
import com.example.movies.features.movies.data.models.data.ApiMovieResponse
import com.example.movies.features.movies.data.models.data.MoviesEntity

import androidx.recyclerview.widget.GridLayoutManager
import com.example.movies.databinding.ActivityMainBinding

import com.example.movies.features.movies.data.models.data.MoviesResponse
import com.example.movies.features.movies.data.services.APIService
import com.example.movies.features.movies.view.adapters.MoviesAdapter
import com.example.movies.features.movies.view.adapters.OnClickListenerMovie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener,
    androidx.appcompat.widget.SearchView.OnQueryTextListener {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var allMovies: MutableList<MoviesResponse>
    private lateinit var mAdapter: MoviesAdapter
    private lateinit var mGridLayout: GridLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        initRecyclerView()
        mBinding.svMovies.setOnQueryTextListener(this)
        mBinding.reset.setOnClickListener {
            initRecyclerView()
            mBinding.rvMovies.visibility = View.VISIBLE
            mBinding.tvFail.visibility = View.GONE
            mBinding.svMovies.setQuery("", false);
            mBinding.svMovies.clearFocus();
            mBinding.svMovies.onActionViewCollapsed();
        }
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getMovies() {
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(APIService::class.java)

                .getMovies("trending/movie/week?api_key=95644482b1c66a2342c85021908cc3dd")

            val moviesRes = call.body()
            runOnUiThread {
                if (call.isSuccessful) {
                    //show recyclerview
                    allMovies?.clear()
                    allMovies?.addAll(moviesRes!!.results)
                    allMovies.forEach {
                        //println(it.title)

                        println(it.title)
                        val movie = MoviesEntity(
                            id = it.id,
                            overview = it.overview,
                            release_date = it.release_date,
                            title = it.title,
                            adult = it.adult,
                            backdrop_path = it.backdrop_path,
                            vote_count = it.vote_count,
                            original_language = it.original_language,
                            original_title = it.original_title,
                            poster_path = it.poster_path,
                            video = it.video,
                            vote_average = it.vote_average,
                            popularity = it.popularity,
                            media_type = "movie"
                        )
                        Thread {
                            MovieAplication.database.moviesDao().addMovies(movie)
                        }.start()


                    }
                    Log.i("response", allMovies[0].toString())
                    mAdapter.notifyDataSetChanged()
                    Toast.makeText(this@MainActivity, "Funciono", Toast.LENGTH_SHORT).show()
                } else {
                    //show error
//                    showError()
                    Toast.makeText(this@MainActivity, "No Funciono", Toast.LENGTH_SHORT).show()
                }
                hideKeyboard()
            }
        }
    }

    private fun searchMovie(query: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(APIService::class.java)
                .getMovies("search/movie?api_key=95644482b1c66a2342c85021908cc3dd&language=en-US&query=$query&page=1&include_adult=false")
            val moviesRes = call.body()
            runOnUiThread {
                if (call.isSuccessful) {
                    //show recyclerview
                        if (!moviesRes!!.results.isEmpty()){
                            allMovies?.clear()
                            allMovies?.addAll(moviesRes.results)
                            Log.i("response", allMovies[0].toString())
                            mAdapter.notifyDataSetChanged()
                            Toast.makeText(this@MainActivity, "Funciono", Toast.LENGTH_SHORT).show()
                        }else{

                            mBinding.rvMovies.visibility = View.GONE
                            mBinding.tvFail.visibility = View.VISIBLE
                        }


                } else {
                    //show error
//                    showError()
                    Toast.makeText(this@MainActivity, "No Funciono", Toast.LENGTH_SHORT).show()
                }
                hideKeyboard()
            }
        }
    }


    private fun initRecyclerView() {
        allMovies = ArrayList()

        mAdapter = MoviesAdapter(allMovies)
        mGridLayout = GridLayoutManager(this, 2)
        getMovies()

        mBinding.rvMovies.apply {
            setHasFixedSize(true)
            layoutManager = mGridLayout
            adapter = mAdapter
        }
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (!query.isNullOrEmpty()) {
            searchMovie(query)
        }
        return true
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mBinding.root.windowToken, 0)
    }

}