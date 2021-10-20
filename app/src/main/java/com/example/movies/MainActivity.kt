package com.example.movies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movies.databinding.ActivityMainBinding
import com.example.movies.features.movies.data.models.data.MoviesResponse
import com.example.movies.features.movies.data.services.APIService
import com.example.movies.features.movies.view.adapters.MoviesAdapter
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
                    allMovies?.clear()
                    allMovies?.addAll(moviesRes!!.results)
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