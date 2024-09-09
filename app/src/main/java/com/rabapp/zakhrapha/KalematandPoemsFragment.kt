package com.rabapp.zakhrapha

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


/**
 * A simple [Fragment] subclass.
 * Use the [KalematandPoemsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class KalematandPoemsFragment : Fragment() {
   // private lateinit var favoriteViewModel: HistoryViewModel

    private val arrMenuName = listOf(
        "شِعْرُ قَدِيم", "حِكَم", "أَمْثَالَ شَعْبِيَّة", "حِكَم عنْ الْحَبِّ",
        "حِكَم عنْ الْأَخْلَاقِ", "حِكَم عَنْ بُرٍّ الْوَالِدَيْن", "حِكَم عَنْ النَّظَافَة",
        "حِكَم عَنْ الْحَيَاةِ", "الثِّقَة بِالنَّفْس", "بَايو سوشيال ميديا",
        "رَمَضَانَيْات", "بايو رُومَأَنْسَى", "الْمُتَنَبِّي", "أحمد شوقي", "امْرُؤٌ الْقَيْس"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_kalematand_poems, container, false)
        val kalematrecyclerview = view.findViewById<RecyclerView>(R.id.recyclerkalemat)
        kalematrecyclerview.adapter = KalematViewAdapter(arrMenuName){
                position ->
            openBottomSheet(position)
        }
        kalematrecyclerview.layoutManager = GridLayoutManager(context, 2)
      //  favoriteViewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)

        return view    }

    private fun openBottomSheet(position: Int) {
        val fragment = KalematwAshaarBottomSheetFragment()
        val bundle = Bundle()
        bundle.putStringArrayList("menuList", ArrayList(arrMenuName))
        bundle.putInt("position", position)  // Add position to the bundle

        fragment.arguments = bundle
        fragment.show(parentFragmentManager, fragment.tag)
    }

}