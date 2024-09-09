package com.rabapp.zakhrapha

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ShapeActivity : AppCompatActivity() {
    private val arrMenuName = arrayOf(
        "غاضب", "مزاج سئ", "دُبٌّ", "توسل", "محتار", "وجه باكي", "وجه لطيف",
        "وجه راقص", "وجه مذمم", "وجه شرير", "خائب الأمل", "وجه أليف", "وجه قيبيح",
        "وجه يأكل", "وجه شيطاني", "وجه متحمس", "سعيد", "اهلا ومرحبا", "وجه مشفق",
        "وجه مخفي", "عناق", "متوتر", "وجه فخور", "وجه يركض", "نظارات", "حزين",
        "خائف", "مصدوم", "وجه قوي", "قلب", "رمز نجمي", "شكر", "هذا هوا", "تفكير",
        "اعجاب", "متعب", "افعل ما بوسعي", "يوني كود", "تقئ", "مخيف", "غمز", "جدول متقلب"
    )

    private val arrMenuIcon = arrayOf(
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
        setContentView(R.layout.activity_shape)
        val recyclershapes: RecyclerView = findViewById(R.id.collection)
        recyclershapes.layoutManager = GridLayoutManager(this,3)

        recyclershapes.adapter = ShapesAdapter(arrMenuName,arrMenuIcon){
                position ->   showBottomSheetDialog(position)

        }


    }

    private fun showBottomSheetDialog(position: Int) {

    }
}