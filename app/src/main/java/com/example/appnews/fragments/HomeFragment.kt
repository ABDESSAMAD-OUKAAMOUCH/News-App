package com.example.appnews.fragments

import android.os.Bundle
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

class HomeFragment:Fragment() {
    val api:String="7eb7cc3900f247f7961ee1c519b145ae"
    lateinit var modelClassArrayList:ArrayList<ModelClass>
    lateinit var newsAdapter: NewsAdapter
    private lateinit var recyclerViewOfHome: RecyclerView
    private val newsDatabase: NewsDatabase by lazy {
        NewsDatabase.getDatabase(requireContext().applicationContext)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.home_fragment,null)
        recyclerViewOfHome=view.findViewById(R.id.recyclerViewHome)
        modelClassArrayList=ArrayList()
        recyclerViewOfHome.layoutManager=LinearLayoutManager(context)
        newsAdapter=NewsAdapter(requireContext(),modelClassArrayList)
        recyclerViewOfHome.adapter=newsAdapter
        findNews()
        return view
    }
    fun findNews(){
        ApiUtilities.getApiInterface().getNews("us",100,api).enqueue(object:Callback<MainNews>{
            override fun onResponse(call: Call<MainNews>, response: Response<MainNews>) {
                if(response.isSuccessful){
                    val articles = response.body()!!.articles
                    modelClassArrayList.clear()
                    modelClassArrayList.addAll(articles)
                    newsAdapter.notifyDataSetChanged()
                    // حفظ في Room
                    CoroutineScope(Dispatchers.IO).launch {
                        newsDatabase.newsDao().clearArticles()
                        newsDatabase.newsDao().insertArticles(articles)
                    }
                }
            }

            override fun onFailure(call: Call<MainNews>, t: Throwable) {
                // في حال الفشل - جلب الأخبار من Room
                CoroutineScope(Dispatchers.IO).launch {
                    val localArticles = newsDatabase.newsDao().getAllArticles()
                    withContext(Dispatchers.Main) {
                        modelClassArrayList.clear()
                        modelClassArrayList.addAll(localArticles)
                        newsAdapter.notifyDataSetChanged()
                    }
                }
            }

        })
    }
}