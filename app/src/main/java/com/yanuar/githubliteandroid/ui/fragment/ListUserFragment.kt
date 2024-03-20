package com.yanuar.githubliteandroid.ui.fragment

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.yanuar.githubliteandroid.R
import com.yanuar.githubliteandroid.data.adapter.FollowerAdapter
import com.yanuar.githubliteandroid.data.adapter.UserAdapter
import com.yanuar.githubliteandroid.data.model.ItemsItem
import com.yanuar.githubliteandroid.databinding.FragmentListUserBinding
import com.yanuar.githubliteandroid.databinding.FragmentSearchUserBinding
import com.yanuar.githubliteandroid.viewmodel.ListUserViewModel

class ListUserFragment : Fragment() {

    companion object {
        fun newInstance() = ListUserFragment()
    }

    private val viewModel: ListUserViewModel by viewModels()
    private var _binding: FragmentListUserBinding? = null
    private val binding get()= _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        val sectionNumber = arguments?.getInt("section_number") ?: 0
        val username = arguments?.getString("username") ?: ""
        if (sectionNumber == 0) {
            viewModel.fetchUserData(username, ListUserViewModel.UserType.FOLLOWERS)
        } else {
            viewModel.fetchUserData(username, ListUserViewModel.UserType.FOLLOWING)
        }
        observeViewModel()
    }

    private fun setupRecyclerView() {
        binding.idListRecUser.layoutManager = LinearLayoutManager(context)
        binding.idListRecUser.adapter = FollowerAdapter(emptyList(),{})
    }
    private fun observeViewModel() {
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            showLoading(isLoading)
        })
        viewModel.userData.observe(viewLifecycleOwner, Observer { users ->
            (binding.idListRecUser.adapter as FollowerAdapter).updateUsers(users)
        })
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListUserBinding.inflate(inflater,container,false)
        val sectionNumber = arguments?.getInt("section_number")

        return _binding?.root
    }
}