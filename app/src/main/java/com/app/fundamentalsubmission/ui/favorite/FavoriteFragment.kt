package com.app.fundamentalsubmission.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.fundamentalsubmission.ViewModelsFactory
import com.app.fundamentalsubmission.adapter.FavoriteAdapter
import com.app.fundamentalsubmission.databinding.FragmentFavoriteBinding
import com.app.fundamentalsubmission.di.Injection.messageError
import com.app.fundamentalsubmission.di.database.FavoriteEvent

class FavoriteFragment : Fragment() {

    private lateinit var bind: FragmentFavoriteBinding
    private val favoriteViewModel by viewModels<FavoriteViewModel> {
        ViewModelsFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        bind = FragmentFavoriteBinding.inflate(layoutInflater, container, false)

        val adapter = FavoriteAdapter()

        bind.apply {
            rv.layoutManager = LinearLayoutManager(requireActivity())
            rv.adapter = adapter

            favoriteViewModel.isLoading.observe(viewLifecycleOwner) {
                prgressBar.visibility = if (it) View.VISIBLE else View.GONE
            }

            fun updateEventList(events: List<FavoriteEvent>) {
                adapter.submitList(events)
            }

            favoriteViewModel.getAllFavorite().observe(viewLifecycleOwner){ data ->
                data?.let {
                    updateEventList(it)
                } ?: messageError(requireActivity(), "Failed to initialize data")
            }

        }

        return bind.root
    }

}