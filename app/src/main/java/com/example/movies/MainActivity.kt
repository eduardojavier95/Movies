package com.example.movies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.databinding.ActivityMainBinding
import com.example.movies.features.movies.data.models.data.ApiMovieResponse
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

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding

    //    private lateinit var movies: ApiMovieResponse
    private var allMovies: MutableList<MoviesResponse> = ArrayList()

    lateinit var mRecyclerView: RecyclerView

    private var mAdapter: MoviesAdapter = MoviesAdapter(allMovies)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        getMovies()
        initRecyclerView()
    }

    private fun getRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getMovies(){
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(APIService::class.java).getMovies("trending/movie/week?api_key=95644482b1c66a2342c85021908cc3dd")
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
//                hideKeyboard()
            }
        }
    }

//    fun getList(movies: Objects){
//        return movies.
//    }


    private fun initRecyclerView(){
        mAdapter = MoviesAdapter(allMovies)
        mBinding.rvMovies.layoutManager = LinearLayoutManager(this)
        mBinding.rvMovies.adapter = mAdapter
    }
//
//    fun setUpRecyclerView(){
//
//        mRecyclerView = mBinding.rvMovies
//        mRecyclerView.setHasFixedSize(true)
//        mRecyclerView.layoutManager = LinearLayoutManager(this)
////        mAdapter.MoviesAdapter(getMovies())
//        mAdapter = MoviesAdapter(getMovies())
//        mRecyclerView.adapter = mAdapter
//    }

//    override fun onQueryTextChange(newText: String?): Boolean {
//        return true
//    }

//    override fun onQueryTextSubmit(query: String?): Boolean {
//        if(!query.isNullOrEmpty()){
//            searchByName()
//        }
//        return true
//    }
//
//    private fun hideKeyboard() {
//        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
//    }
}