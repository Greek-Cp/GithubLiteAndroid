package com.yanuar.githubliteandroid.ui.fragment

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.yanuar.githubliteandroid.data.adapter.TabsPagerAdapter
import com.yanuar.githubliteandroid.data.adapter.UserAdapter
import com.yanuar.githubliteandroid.data.model.GithubDetailAccount
import com.yanuar.githubliteandroid.data.model.ItemsItem
import com.yanuar.githubliteandroid.databinding.FragmentDetailUserBinding
import com.yanuar.githubliteandroid.viewmodel.DetailUserViewModel
import com.yanuar.githubliteandroid.viewmodel.ListUserViewModel

class DetailUserFragment : Fragment() {

    companion object {
        fun newInstance() = DetailUserFragment()
    }

    private val viewModel: DetailUserViewModel by viewModels()
    private var _binding: FragmentDetailUserBinding? = null
    private val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: Use the ViewModel

    }
    private fun observeViewModel() {
        viewModel.snackbarText.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { snackBarText ->
                Snackbar.make(
                    requireView().rootView,
                    snackBarText,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })

        viewModel.userDetail.observe(viewLifecycleOwner, Observer { userDetail ->
            userDetail?.let {
                updateUI(it as GithubDetailAccount)
            }
        })
    }
//    private fun showLoading(isLoading: Boolean) {
//        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
//    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = arguments?.getString("username")
        viewModel.fetchUserDetail(username!!)
        observeViewModel()
        setupTabBar(username)
    }
    private fun setupTabBar(username: String) {
        val adapter = TabsPagerAdapter(this, username)
        binding?.viewPager?.adapter = adapter
        TabLayoutMediator(binding!!.tabs, binding!!.viewPager) { tab, position ->
            tab.text = if (position == 0) "Followers" else "Following"
        }.attach()
    }


    private fun updateUI(users: GithubDetailAccount) {
        //(binding.idRecUser.adapter as? UserAdapter)?.updateUsers(users)
        binding!!.imageView
        val shimmer = Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
            .setDuration(1800) // how long the shimmering animation takes to do one full sweep
            .setBaseAlpha(0.7f) //the alpha of the underlying children
            .setHighlightAlpha(0.6f) // the shimmer alpha amount
            .setDirection(Shimmer.Direction.RIGHT_TO_LEFT)
            .setAutoStart(true)
            .build()

        val shimmerDrawable = ShimmerDrawable().apply {
            setShimmer(shimmer)
        }

        Glide.with(requireActivity())
            .load(users.avatarUrl)
            .placeholder(shimmerDrawable)
            .into(  binding!!.imageView)
        binding!!.idTvNama.setText(users.name)
        binding!!.idTvUsername.setText(users.login)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailUserBinding.inflate(inflater,container,false)
        return _binding?.root
    }
}