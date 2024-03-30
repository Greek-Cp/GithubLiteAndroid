package com.yanuar.githubliteandroid.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.yanuar.githubliteandroid.R
import com.yanuar.githubliteandroid.data.adapter.FavoritUserAdapter
import com.yanuar.githubliteandroid.data.database.UserFavDatabase
import com.yanuar.githubliteandroid.data.repository.UserFavRepository
import com.yanuar.githubliteandroid.databinding.FragmentFavoriteUserBinding
import com.yanuar.githubliteandroid.viewmodel.FavoritUserViewModel
import com.yanuar.githubliteandroid.viewmodel.FavoritUserViewModelFactory

class FavoritUserFragment : Fragment() {

    private var _binding: FragmentFavoriteUserBinding? = null
    private val binding get() = _binding!!

    // Inisialisasi ViewModel dengan factory
    private val viewModel: FavoritUserViewModel by viewModels {
        val userDao = UserFavDatabase.getDatabase(requireContext()).userDao()
        val repository = UserFavRepository(userDao)
        FavoritUserViewModelFactory(repository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFavoriteUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set up RecyclerView and Adapter
        val adapter = FavoritUserAdapter(listOf()) { username ->
            val detailFragment = DetailUserFragment().apply {
                arguments = Bundle().apply {
                    putString("username", username)
                }
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, detailFragment)
                .addToBackStack(null) // Optional, untuk menambahkan transaksi ke back stack
                .commit()        }
        binding.idRecFavUser.adapter = adapter

        // Observe ViewModel data and update UI
        viewModel.favoriteUsers.observe(viewLifecycleOwner) { users ->
            adapter.updateUsers(users)
        }
        binding.idBackBtn.setOnClickListener{
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container,SearchUserFragment())
                .commit()
        }
        with(binding) {
            searchFavUser.setupWithSearchBar(searchBar)
            searchFavUser.editText.setOnEditorActionListener { textView, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val searchQuery = searchFavUser.editText.text.toString()
                    if (searchQuery.isNotEmpty()) {
                        viewModel.searchFavoriteUsers(searchQuery)
                        searchBar.setText(searchFavUser.text)
                        searchFavUser.hide()
                    }
                    true
                } else {
                    false
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = FavoritUserFragment()
    }
}
