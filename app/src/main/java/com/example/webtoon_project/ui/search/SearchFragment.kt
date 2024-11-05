//package com.example.webtoon_project.ui.search
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.SearchView
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.ViewModelProvider
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.webtoon_project.databinding.FragmentSearchBinding
//
//class SearchFragment : Fragment() {
//
//    private var _binding: FragmentSearchBinding? = null
//    private val binding get() = _binding!!
//    private lateinit var viewModel: SearchViewModel
//    private lateinit var webtoonAdapter: WebtoonAdapter
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
//        _binding = FragmentSearchBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        setupRecyclerView()
//        setupSearchView()
//        setupSearchButton()
//        observeViewModel()
//    }
//
//    private fun setupRecyclerView() {
//        webtoonAdapter = WebtoonAdapter()
//        binding.recyclerViewWebtoons.apply {
//            layoutManager = LinearLayoutManager(context)
//            adapter = webtoonAdapter
//        }
//    }
//
//    private fun setupSearchView() {
//        binding.searchViewWebtoons.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                query?.let { performSearch(it) }
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                return false
//            }
//        })
//    }
//
//    private fun setupSearchButton() {
//        binding.buttonSearch.setOnClickListener {
//            val query = binding.searchViewWebtoons.query.toString()
//            performSearch(query)
//        }
//    }
//
//    private fun performSearch(query: String) {
//        viewModel.searchWebtoons(query)
//    }
//
//    private fun observeViewModel() {
//        viewModel.webtoons.observe(viewLifecycleOwner) { webtoons ->
//            webtoonAdapter.submitList(webtoons)
//        }
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}