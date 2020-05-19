package com.sunil.wallyapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sunil.wallyapp.data.ApiHelperImpl
import com.sunil.wallyapp.data.RetrofitBuilder
import com.sunil.wallyapp.data.model.Photos
import com.sunil.wallyapp.databinding.FragmentMainBinding
import com.sunil.wallyapp.utils.Status
import com.sunil.wallyapp.utils.affectOnItemClicks
import com.sunil.wallyapp.viewmodel.MainViewModel
import com.sunil.wallyapp.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private var viewModel: MainViewModel? = null
    private lateinit var photoAdapter: PhotoAdapter
    private var contentList: List<Photos>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        viewModel = ViewModelProviders.of(
            requireActivity(),
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService))
        ).get(MainViewModel::class.java)

        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setupUI()
        setupObserver()

        return binding.root
    }

    private fun setupUI() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        photoAdapter = PhotoAdapter(arrayListOf())
        binding.recyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        //  binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.apply {
            adapter = photoAdapter
        }

        binding.recyclerView.affectOnItemClicks { position, view ->
            var content = contentList!![position]
        }

    }

    private fun setupObserver() {
        viewModel?.getUsersPhoto()?.observe(requireActivity(), Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    it.data?.let { usersphoto -> renderList(usersphoto) }
                    recyclerView.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                }
                Status.ERROR -> {
                    //Handle Error
                    progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun renderList(usersPhoto: List<Photos>) {
        contentList = usersPhoto
        photoAdapter.updateContent(usersPhoto)
    }


}