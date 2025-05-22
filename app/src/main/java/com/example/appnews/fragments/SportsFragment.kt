package com.example.appnews.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appnews.ApiUtilities
import com.example.appnews.Models.MainNews
import com.example.appnews.Models.ModelClass
import com.example.appnews.R
import com.example.appnews.adapters.NewsAdapter
import com.example.appnews.dataBase.NewsDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SportsFragment : Fragment() {

    private val apiKey = "7eb7cc3900f247f7961ee1c519b145ae"
    private lateinit var modelClassArrayList: ArrayList<ModelClass>
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var recyclerViewSports: RecyclerView
    private val category = "sports"

    private val newsDatabase: NewsDatabase by lazy {
        NewsDatabase.getDatabase(requireContext().applicationContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.sports_fragment, container, false)

        recyclerViewSports = view.findViewById(R.id.recyclerViewSports)
        modelClassArrayList = ArrayList()
        recyclerViewSports.layoutManager = LinearLayoutManager(context)
        newsAdapter = NewsAdapter(requireContext(), modelClassArrayList)
        recyclerViewSports.adapter = newsAdapter

        // جلب الأخبار من Room أولًا
        loadFromRoom()

        // ثم جلبها من الشبكة
        if (isNetworkAvailable()) {
            fetchFromApi()
        }


        return view
    }

    private fun loadFromRoom() {
        CoroutineScope(Dispatchers.IO).launch {
            val localArticles = newsDatabase.newsDao().getArticlesByCategory(category)

            Log.d("ROOM_FETCH", "Fetched ${localArticles.size} articles from Room")

            withContext(Dispatchers.Main) {
                modelClassArrayList.clear()
                modelClassArrayList.addAll(localArticles)
                newsAdapter.notifyDataSetChanged()
            }
        }
    }


    private fun fetchFromApi() {
        ApiUtilities.getApiInterface().getCategoryNews("us", category, 100, apiKey)
            .enqueue(object : Callback<MainNews> {
                override fun onResponse(call: Call<MainNews>, response: Response<MainNews>) {
                    if (response.isSuccessful && response.body() != null) {
                        val articles = response.body()!!.articles.map {
                            it.copy(category = category)
                        }
                        modelClassArrayList.clear()
                        modelClassArrayList.addAll(articles)
                        newsAdapter.notifyDataSetChanged()

                        CoroutineScope(Dispatchers.IO).launch {
                            newsDatabase.newsDao().deleteByCategory(category)
                            newsDatabase.newsDao().insertArticles(articles)
                        }
                    }
                }

                override fun onFailure(call: Call<MainNews>, t: Throwable) {
                    // فشل الاتصال - البيانات من Room تعرض سابقًا
                }
            })
    }
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

}
