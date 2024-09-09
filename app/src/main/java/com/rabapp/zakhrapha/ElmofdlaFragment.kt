package com.rabapp.zakhrapha

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ElmofdlaFragment : Fragment() {

   // private val favoritesViewModel: SharedViewModel by viewModels()
   private lateinit var favoriteViewModel: HistoryViewModel
    private lateinit var favAdapter: FavouriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_elmofdla, container, false)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Reference to the RecyclerView
         favoriteViewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerelmofdla)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        favAdapter = FavouriteAdapter(mutableListOf(), favoriteViewModel)
        recyclerView.adapter = favAdapter

        favoriteViewModel.allFavoriteItems?.observe(viewLifecycleOwner, { favItems ->
            favAdapter.updateList(favItems) // Update the adapter's list with new data
        })
        /*favoriteViewModel.allfavoriteitems?.observe(viewLifecycleOwner, { favItems ->
            val favAdapter = FavouriteAdapter(favItems,favoriteViewModel)

            recyclerView.adapter = favAdapter

        })*/
    }
}