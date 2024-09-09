package com.rabapp.zakhrapha

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class AshkalFragment : Fragment() {

    private val arrMenuName = listOf(
        "غاضب", "مزاج سئ", "دُبٌّ", "توسل", "محتار", "وجه باكي", "وجه لطيف",
        "وجه راقص", "وجه مذمم", "وجه شرير", "خائب الأمل", "وجه أليف", "وجه قيبيح",
        "وجه يأكل", "وجه شيطاني", "وجه متحمس", "سعيد", "اهلا ومرحبا", "وجه مشفق",
        "وجه مخفي", "عناق", "متوتر", "وجه فخور", "وجه يركض", "نظارات", "حزين",
        "خائف", "مصدوم", "وجه قوي", "قلب", "رمز نجمي", "شكر", "هذا هوا", "تفكير",
        "اعجاب", "متعب", "افعل ما بوسعي", "يوني كود", "تقئ", "مخيف", "غمز", "جدول متقلب"
    )

    private val arrMenuIcon = listOf(
        "(⩺_⩹)", "(⏕)", "ʕ⁀ᴥ⁀ʔ", "(人ﾟ ∀ﾟ)", "(ↁ_ↁ)", "(ಥ _ʖಥ)", "༼☯﹏☯༽",
        "ᕕ(⌐■_■)ᕗ ♪♬", "(´-ι_-｀)", "ψ(｀∇´)ψ", "(´д｀)", "▼・ᴥ・▼", "༼⌐ ■ل͟■ ༽",
        "🍦ԅ( ͒ ۝ ͒ )", "（=｀〜´=）", "( ★^O^★ )", "( ͡ᵔ ͜ʖ ͡ᵔ )", "( ♥ ͜ʖ ♥)",
        "(╥╯⌒╰╥๑)", "ﾍ(･ _|", "⊂(･ω･*⊂)", "◑.◑", "( - 、- )", "ᕕ༼✪ل͜✪༽ᕗ",
        "(⌐▨_▨)", "༼⍨༽", "(-@Д@)", "(☉̃ₒ☉)", "ᕦ(ò_óˇ)ᕤ", "( ♥ ͜ʖ ♥)", "❂",
        "♪(･ω･)ﾉ", "(´ε｀；)", "( ˇ÷ˇ　 )", "(*TーT)b", "(／０￣)", "(ง'̀-'́)ง",
        "☲", "（●≧艸≦", "( ◉◞౪◟◉)", "( ͡~ ͜ʖ ͡°)", "(╯ ͝° ͜ʖ͡°)╯︵ ┻━┻"
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
        val view = inflater.inflate(R.layout.fragment_ashkal, container, false)
        val ashkalrecyclerview = view.findViewById<RecyclerView>(R.id.ashkalrecycler)
        ashkalrecyclerview.adapter = AshkalnewViewAdapter(arrMenuName,arrMenuIcon){
                position ->
            openBottomSheet(position)
        }
        ashkalrecyclerview.layoutManager = GridLayoutManager(context, 3) // Number of columns in the grid

        return view
    }

    private fun openBottomSheet(position: Int) {
        val fragment = ShapesBottomSheetFragment()
        val bundle = Bundle()
        bundle.putStringArrayList("menuList", ArrayList(arrMenuName))
        bundle.putInt("position", position)
        fragment.arguments = bundle
        fragment.show(parentFragmentManager, fragment.tag)
    }
    }

