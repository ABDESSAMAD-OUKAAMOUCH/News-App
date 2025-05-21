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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TechnologyFragment:Fragment() {
    val api:String="22b900ad18f845e09405f64b97977943"
    lateinit var modelClassArrayList:ArrayList<ModelClass>
    lateinit var newsAdapter: NewsAdapter
    private lateinit var recyclerViewTechnology: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.technology_fragment,null)
        recyclerViewTechnology=view.findViewById(R.id.recyclerViewTechnology)
        modelClassArrayList=ArrayList()
        recyclerViewTechnology.layoutManager= LinearLayoutManager(context)
        newsAdapter= NewsAdapter(requireContext(),modelClassArrayList)
        recyclerViewTechnology.adapter=newsAdapter
        findNews()
        return view
    }
    fun findNews(){
        ApiUtilities.getApiInterface().getCategoryNews("us","technology",100,api).enqueue(object:
            Callback<MainNews> {
            override fun onResponse(call: Call<MainNews>, response: Response<MainNews>) {
                if(response.isSuccessful){
                    response.body()?.let { modelClassArrayList.addAll(it.articles) }
                    newsAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<MainNews>, t: Throwable) {
            }

        })
    }
}