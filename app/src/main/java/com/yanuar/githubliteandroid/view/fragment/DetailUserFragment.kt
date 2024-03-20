package com.yanuar.githubliteandroid.view.fragment

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yanuar.githubliteandroid.R
import com.yanuar.githubliteandroid.databinding.ActivityMainBinding
import com.yanuar.githubliteandroid.databinding.FragmentDetailUserBinding
import com.yanuar.githubliteandroid.viewmodel.DetailUserViewModel

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailUserBinding.inflate(inflater,container,false)
        return _binding?.root
    }
}