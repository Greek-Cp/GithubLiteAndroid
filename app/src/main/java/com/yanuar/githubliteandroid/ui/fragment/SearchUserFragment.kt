package com.yanuar.githubliteandroid.ui.fragment

import SettingAppPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.yanuar.githubliteandroid.R
import com.yanuar.githubliteandroid.data.adapter.UserAdapter
import com.yanuar.githubliteandroid.data.model.ItemsItem
import com.yanuar.githubliteandroid.databinding.FragmentSearchUserBinding
import com.yanuar.githubliteandroid.viewmodel.SearchUserViewModel
import dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SearchUserFragment : Fragment() {
    private val viewModel: SearchUserViewModel by viewModels()
    private var _binding: FragmentSearchUserBinding? = null
    private val binding get()= _binding!!
    private val settingPreferences: SettingAppPreferences by lazy {
        SettingAppPreferences.getInstance(requireContext().dataStore)
    }
    private fun darkModeFeature() {

        settingPreferences.getThemeSetting().asLiveData().observe(viewLifecycleOwner) { isDarkModeActive ->
            binding.imageDark.tag = if (isDarkModeActive) "darkModeIcon" else "lightModeIcon"
            val iconRes = if (isDarkModeActive) R.drawable.baseline_light_mode_24 else R.drawable.baseline_dark_mode_24
            binding.imageDark.setImageResource(iconRes)
        }
        binding.imageDark.setOnClickListener {
            lifecycleScope.launch {
                val currentMode = settingPreferences.getThemeSetting().first()
                settingPreferences.saveThemeSetting(!currentMode)
            }
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.cariUsers("a")
        setupRecyclerView()
        setupSearchView()
        observeViewModel()
        binding.idFavButton.setOnClickListener{
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container,FavoritUserFragment())
                .addToBackStack(null)
                .commit()
        }
    }
    private fun setupRecyclerView() {
        binding.idRecUser.layoutManager = LinearLayoutManager(context)
        binding.idRecUser.adapter = UserAdapter(emptyList(),{username ->
            val detailFragment = DetailUserFragment().apply {
                arguments = Bundle().apply {
                    putString("username", username)
                }
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, detailFragment)
                .addToBackStack(null)
                .commit()
        })
    }
    private fun setupSearchView() {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { textView, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val searchQuery = searchView.editText.text.toString()
                    if (searchQuery.isNotEmpty()) {
                        viewModel.cariUsers(searchQuery)
                        searchBar.setText(searchView.text)
                        searchView.hide()
                    }
                    true
                } else {
                    false
                }
            }
        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun observeViewModel() {
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            showLoading(isLoading)
        })
        viewModel.snackbarText.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { snackBarText ->
                Snackbar.make(
                    requireView().rootView,
                    snackBarText,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })
        viewModel.searchResults.observe(viewLifecycleOwner, Observer { searchResponse ->
            searchResponse?.let {
                updateUI(it.items as List<ItemsItem>)
            }
        })
    }
    private fun updateUI(users: List<ItemsItem>) {
        (binding.idRecUser.adapter as? UserAdapter)?.updateUsers(users)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchUserBinding.inflate(inflater,container,false)
        darkModeFeature()
        return binding?.root
    }
    companion object {
        fun newInstance() = SearchUserFragment()
    }

}