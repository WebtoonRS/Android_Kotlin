package com.example.webtoon_project.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.webtoon_project.databinding.FragmentStoryBasedBinding
import com.example.webtoon_project.ui.search.adapter.SearchTitleAdapter
import com.example.webtoon_project.ui.search.adapter.RecommendationAdapter

class StoryBasedFragment : Fragment() {
    private var _binding: FragmentStoryBasedBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SearchViewModel
    private lateinit var searchTitleAdapter: SearchTitleAdapter
    private lateinit var recommendAdapter: RecommendationAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoryBasedBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireParentFragment())[SearchViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViews()
        observeViewModel()

        binding.searchResultsRecyclerView.visibility = View.VISIBLE
        binding.recommendationsRecyclerView.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
    }

    private fun setupRecyclerViews() {
        searchTitleAdapter = SearchTitleAdapter().apply {
            setOnItemClickListener { webtoon ->
                binding.progressBar.visibility = View.VISIBLE
                binding.searchResultsRecyclerView.visibility = View.GONE
                viewModel.getRecommendations(webtoon)
            }
        }
        
        binding.searchResultsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = searchTitleAdapter
        }

        recommendAdapter = RecommendationAdapter()
        binding.recommendationsRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = recommendAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.searchResults.observe(viewLifecycleOwner) { response ->
            if (viewModel.searchMode.value == SearchViewModel.SearchMode.STORY) {
                searchTitleAdapter.submitList(response.searchResults)
                binding.searchResultsRecyclerView.visibility = View.VISIBLE
                binding.recommendationsRecyclerView.visibility = View.GONE
            }
        }

        viewModel.recommendResults.observe(viewLifecycleOwner) { recommendations ->
            if (recommendations.isNotEmpty()) {
                recommendAdapter.submitList(recommendations)
                binding.recommendationsRecyclerView.visibility = View.VISIBLE
                binding.searchResultsRecyclerView.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
            }
        }

        viewModel.searchError.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}