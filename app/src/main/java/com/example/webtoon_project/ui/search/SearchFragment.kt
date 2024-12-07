package com.example.webtoon_project.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.webtoon_project.databinding.FragmentSearchBinding
import android.content.Context
import com.google.android.material.tabs.TabLayoutMediator
import android.view.inputmethod.EditorInfo
import android.view.KeyEvent

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SearchViewModel
    private lateinit var searchPagerAdapter: SearchPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSearchView()
        setupViewPager()
        observeViewModel()
        
        binding.searchButton.setOnClickListener {
            val query = binding.searchEditText.text.toString()
            if (query.isNotEmpty()) {
                performSearch(query)
            }
        }
    }

    private fun setupSearchView() {
        binding.searchEditText.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                (event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                val query = binding.searchEditText.text.toString()
                if (query.isNotEmpty()) {
                    performSearch(query)
                }
                true
            } else {
                false
            }
        }
    }

    private fun setupViewPager() {
        searchPagerAdapter = SearchPagerAdapter(this)
        binding.searchViewPager.adapter = searchPagerAdapter

        TabLayoutMediator(binding.searchTabLayout, binding.searchViewPager) { tab, position ->
            tab.text = getString(SearchPagerAdapter.TAB_TITLES[position])
        }.attach()
    }

    private fun performSearch(query: String) {
        val searchMode = when(binding.searchViewPager.currentItem) {
            0 -> SearchViewModel.SearchMode.STORY
            1 -> SearchViewModel.SearchMode.STYLE
            else -> SearchViewModel.SearchMode.STORY
        }
        
        viewModel.searchWebtoons(query, searchMode)
        
        // 키보드 숨기기
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun observeViewModel() {
        viewModel.searchError.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
