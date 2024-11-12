package com.example.webtoon_project.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.webtoon_project.databinding.FragmentHomeBinding
import com.example.webtoon_project.Retrofit.INodeJS
import com.example.webtoon_project.WebtoonAdapter
import com.example.webtoon_project.Retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var webtoonAdapter: WebtoonAdapter
    private lateinit var myAPI: INodeJS

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        
        // Initialize API
        val retrofit = RetrofitClient.getInstance()
        myAPI = retrofit.create(INodeJS::class.java)

        // Initialize RecyclerView
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = GridLayoutManager(context, 3)

        // Fetch webtoons
        fetchWebtoonList()

        return binding.root
    }

    private fun fetchWebtoonList() {
        myAPI.getWebtoons().enqueue(object : Callback<List<INodeJS.Webtoon>> {
            override fun onResponse(
                call: Call<List<INodeJS.Webtoon>>,
                response: Response<List<INodeJS.Webtoon>>
            ) {
                if (response.isSuccessful) {
                    val webtoons = response.body() ?: emptyList()
                    webtoonAdapter = WebtoonAdapter(webtoons) { webtoonId ->
                        // Handle webtoon click
                        Toast.makeText(context, "Clicked webtoon: $webtoonId", Toast.LENGTH_SHORT).show()
                    }
                    recyclerView.adapter = webtoonAdapter
                } else {
                    Toast.makeText(context, "Failed to load webtoons", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<INodeJS.Webtoon>>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

