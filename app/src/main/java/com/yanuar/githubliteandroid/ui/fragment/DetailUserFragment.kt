package com.yanuar.githubliteandroid.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.yanuar.githubliteandroid.R
import com.yanuar.githubliteandroid.data.adapter.TabsPagerAdapter
import com.yanuar.githubliteandroid.data.database.UserFavDatabase
import com.yanuar.githubliteandroid.data.model.GithubDetailAccount
import com.yanuar.githubliteandroid.data.model.UserFav
import com.yanuar.githubliteandroid.data.repository.UserFavRepository
import com.yanuar.githubliteandroid.databinding.FragmentDetailUserBinding
import com.yanuar.githubliteandroid.viewmodel.DetailUserViewModel
import com.yanuar.githubliteandroid.viewmodel.DetailUserViewModelFactory

class DetailUserFragment : Fragment() {

    companion object {
        fun newInstance() = DetailUserFragment()
    }

    private lateinit var viewModel: DetailUserViewModel

    private var _binding: FragmentDetailUserBinding? = null
    private val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    private fun startShimmer() {
        binding!!.idShimmerFrameTop.startShimmer()
        binding!!.idShimmerFrameFollower.startShimmer()
        binding!!.idShimmerFrameFollowing.startShimmer()
    }

    private fun stopShimmer() {
        binding!!.idShimmerFrameTop.hideShimmer()
        binding!!.idShimmerFrameFollower.hideShimmer()
        binding!!.idShimmerFrameFollowing.hideShimmer()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userDao = UserFavDatabase.getDatabase(requireContext()).userDao()
        val userFavRepository = UserFavRepository(userDao)
        val factory = DetailUserViewModelFactory(userFavRepository)
        viewModel = ViewModelProvider(this, factory).get(DetailUserViewModel::class.java)
        val username = arguments?.getString("username")
        startShimmer()
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
        stopShimmer()
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
        binding!!.idTvJumlahFollowed.setText(users.following.toString())
        binding!!.idTvJumlahFollower.setText(users.followers.toString())
        val username = users.login.toString()

        viewModel.checkUserExistence(username)
        viewModel.isUserFav.observe(viewLifecycleOwner) { isFav ->
            val iconRes = if (isFav) R.drawable.baseline_favorite_24 else R.drawable.baseline_favorite_border_24
            binding!!.btnFav.setImageResource(iconRes)
        }

        binding!!.btnFav.setOnClickListener {
            // Pengecekan dan aksi berdasarkan status isFav saat tombol diklik
            val currentUserFav = UserFav(username, users.avatarUrl)
            if (viewModel.isUserFav.value == true) {
                viewModel.deleteUser(currentUserFav)
            } else {
                viewModel.insertUser(currentUserFav)
            }
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailUserBinding.inflate(inflater,container,false)
        return _binding?.root
    }
}