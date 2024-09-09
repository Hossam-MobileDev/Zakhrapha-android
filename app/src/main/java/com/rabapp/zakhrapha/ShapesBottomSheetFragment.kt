package com.rabapp.zakhrapha

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ShapesBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var arrMenuName: List<String>
    private lateinit var categoryMap: Map<String, List<String>>
    private lateinit var favoriteViewModel: HistoryViewModel

    private val arrAngry = listOf(
        "(⩺_⩹)", "༽◺_◿༼", "(◣_◢)", "(¬▂¬)", "(눈_눈)", "(¬､¬)", "(`A´)", "(⸅⸟⸄)", "(⩺︷⩹)",
        "▽д▽）", "-`д´-", "(’益’)", "(⋋▂⋌)", "〴⋋_⋌〵", "ヽ(ｏ`皿′ｏ)ﾉ", "ᕙ( ︡’︡益’︠)ง",
        "(◣_(◣_(◣_◢)_◢)_◢)", "٩ (╬ʘ益ʘ╬) ۶", "( ง `ω´ )۶", "۹( ÒہÓ )۶", "(╬≖_≖)",
        "୧༼ ಠ益ಠ ༽୨", "(｀д´)ゝ", "⸨◺_◿⸩", "(⦩_⦨)", "-(`෴´)-", "┗(｀o ´)┓", "꒰｡•`ｪ´•｡꒱۶"
    )

    private val arrBadMood = listOf(
        "(⏕)", "(▪‗▪)", "(ᕵ﹏ᕴ)", "(ˑ…ˑ)", "=(ˑ‗ˑ)=", "(ᕘ⸟ᕚ)", "(⸅⸟⸄)", "(⊳…⊲)", "(⁍﹏⁌)",
        "(~_~)", "(⟢﹏⟣)", "（＞_＜）", "(ò‗ó)", "(ò…ó)", "(Ф_Ф)", "(ᖛ‗ᖜ)", "ᕴｰᴥｰᕵ", "༽◺_◿༼", "Ψ(`_´ # )↝", "☜(`o´)"
    )

    private val arrBear = listOf(
        "ʕ⁀ᴥ⁀ʔ", "・㉨・", "ᵔᴥᵔ", "ʕ•ᴥ•ʔ", "ʕ·ᴥ·ʔ", "ˁ˙˟˙ˀ", "❃ႣᄎႣ❃", "ʕᵔᴥᵔʔ", "ʕᵕᴥᵕʔ",
        "ʢᵕ﹏ᵕʡ", "ʕ￫ᴥ￩ʔ", "(๏㉨๏)", "(ó㉨ò)", "ʢᵕᴗᵕʡ", "ʕ◉ᴥ◉ʔ", "ʕ・㉨・ʔ", "ʕ≧ᴥ≦ʔ",
        "ʕ•㉨•ʔ", "xʕ≧ᴥ≦ʔx", "(✪㉨✪)", "ʕ∙ჲ∙ʔ", "ʕʽɞʼʔ", "[◉㉨◉]", "ʕºᴥºʔ", "ʕᵔ㉨ᵔʔ", "ʕ*ᴗ*ʔ",
        "ʕ•⊖•ʔ", "ʕᵒᴥᵒʔ", "ʕ•ᴗ•ʔ", "ʕ⁀㉨⁀ʔ"
    )

    private val arrBeg = listOf(
        "(人ﾟ ∀ﾟ)", "( uωu人 )", "人⍲‿⍲人", "(º人º)", "(ㆆ_ㆆ)", "(ㆆ﹏ㆆ)𐄿", "(ˇ𐐿ˇ)", "᭥(º෴º)𐃳",
        "(＾𐋄＾)", "(ᵕ﹏ᵕ)", "(⊙﹏⊙)", "᭥(⊙︷⊙)𐃷", "(Ф﹏Ф)", "(⸌人⸍)", "(⸌﹏⸍)", "(^人^)", "(⊙人⊙)",
        "（×÷×）人", "(ㆆ～ㆆ)", "(ᵕ人ᵕ)", "(⋆﹏⋆)", "（´Д｀人）", "( 人ゝд∩ )", "༼つ ◕_◕ ༽つ", "(･ω･)𐃳",
        "(･⏠･)", "(⪩﹏⪨)", "（○´Δ｀人）", "(人εﾟ ●)", "(⊙﹇⊙)", "(ˇ⏠ˇ)", "(꙳人꙳)", "(^⌒^)", "(Ф⌒Ф)",
        "(𐩐皿𐩐)", "ヽ(⸄﹏⸅)𐃷", "ཀ(⸄︷⸅)ཫ", "o(´д｀o)"
    )

    private val arrConfused = listOf(
        "(ↁ_ↁ)", "（▽ д▽）", "┐(ﾟ ～ﾟ )┌", "(⊙_◎)", "ఠ_ఠ", "●.◉", "(^^ゞ", "ಠ_ರೃ", "℃ↂ_ↂ",
        "ɾ◉⊆◉ɹ", "(。ヘ°)", "(⊙_☉)", "(＾＾；)", "(☉_☉)", "(♠_♦)", "(ﾟｰﾟ;", "(C_C)", "◔_◔",
        "(゜-゜)", "(◑○◑)", "(・・；)", "⁀⊙﹏☉⁀", "(゜。゜)", "(・・)", "(´エ｀；)", "(‘◇’)", "（・∩・）",
        "(・∧‐)ゞ", "(⊙＿⊙)", "(◎_◎;)", "(●__●)", "(ﾟヘﾟ)", "٩◔̯◔۶", "?_?", "ฅ(ٛ•௰• ٛ )",
        "（◎0◎）ᵒᵐᵍᵎᵎᵎ", "(゜◇゜)ゞ", "(‘-‘*)ゞ", "(ﾟρﾟ*)", "(‘-’*)", "┌( •́ ⌂ •̀ )┐",
        "༼ ͒ ͓ ͒༽", "༼ ͒ ̶ ͒༽", "(> 囗＜?)", "(ﾟДﾟ ?)"
    )

    private val arrCryFace = listOf(
        "(ಥ _ʖಥ)", "(TдT)", "أ‿أ", "(-̥̥̥̥̥̥̥̥̥̥̥̥̥̥̥̥̥̥̥̥̥̥̥̥̥᷄_-̥̥̥̥̥̥̥̥̥̥̥̥̥̥̥̥̥̥̥̥̥̥̥̥̥᷅ )",
        "(；﹏；)", "(ToT)", "(┳Д┳)", "(ಥ﹏ಥ)", "（；へ：）", "(T＿T)", "（πーπ）", "(Ｔ▽Ｔ)", "(⋟﹏⋞)", "（ｉДｉ）",
        "(´Д⊂ヽ", "(;Д;)", "（>﹏<）", "╥﹏╥", "(つ﹏⊂)", "༼☯﹏☯༽", "(ノ﹏ヽ)", "(ノAヽ)", "(╥_╥)",
        "(T⌓T)", "(༎ຶ⌑༎ຶ)", "(☍﹏⁰)｡", "(ಥ_ʖಥ)", "(つд⊂)", "(≖͞_≖̥)", "(இ﹏இ`｡)", "༼ಢ_ಢ༽", "༼ ༎ຶ ෴ ༎ຶ༽"
    )

    private val arrCuteFace = listOf(
        "༼☯﹏☯༽", "ヽ(͡◕ ͜ʖ ͡◕)ﾉ", "❁(◕‿◕)❁", "(◕‿◕)", "˚₊· ͟͟͞͞(º ̆̆º)", "◕‿◕", "(✿ ♥‿♥)", "(◕◡◕)",
        "◕‿◕", "•ᴗ•", "●⌒●", "(✧ω✧)", "(｡♥‿♥｡)", "(⊙﹏⊙)", "ヽ(´∇｀)ノ", "(ღ✪v✪)ღ", "ᕕ( ᐛ )ᕗ", "(⁄ ⁄•⁄ω⁄•⁄ ⁄)", "(▰˘︹˘▰)", "ʕ•ᴥ•ʔ", "(｡･ω･｡)"
    )

    private val arrSmileFace = listOf(
        "(´• ᵕ •`)", "◝(⑅•ᴗ•⑅)◜", "╰(°▽°)╯", "(*^ω^*)", "(*˘︶˘*)", "(。-ω-)♡", "ᵕ ᵕ", "（*＾3＾)/~☆", "(｡•́︿•̀｡)",
        "(≧ω≦)", "ʕ•ᴥ•ʔ", "（＾_＾）", "（⌒_⌒）", "(⌒ω⌒)", "ヾ(＾∇＾)", "╰(°▽°)╯", "(*´∇｀*)", "(*´ω`*)", "(ღ✪v✪)ღ",
        "(∗ ❛ ⌓ ❛ ∗)", "（＾－＾）", "( *⌒∇⌒*)", "◟(• ε •)◞", "✧(｡♡‿♡｡)", "ヽ(＾Д＾)ﾉ", "(*^▽^*)", "ヽ(＾Д＾)ﾉ", "╰(°▽°)╯"
    )

    private val arrHappyFace = listOf(
        "(╯°□°)╯", "(◕‿◕)", "(/^-^(^ ^*)/", "(^-^)v", "(´▽`ʃ♡ƪ)", "(*≧ω≦)", "＼(＾▽＾)／", "(＾ω＾)", "(˘⌣˘)", "(o^▽^o)",
        "(* >ω<)", "(✿^‿^)", "ヽ(＾Д＾)ﾉ", "(*^▽^*)", "(o^▽^o)", "╰(°▽°)╯", "(*^ω^*)", "＼(＾▽＾)／", "(＾◇^)", "(⌒▽⌒)"
    )

    private val arrMoo = listOf(
        "(｡･ω･｡)", "(≧∇≦)", "(o•ω•o)", "(ゝ∀･)", "(っ◕‿◕)っ", "୧(˃◡˂)୨", "(￣ω￣)", "（＾ν＾）", "(*^_^*)", "＼(＾▽＾)／",
        "(ﾟヮﾟ)", "(*≧▽≦)", "(☆∀☆)", "(¬‿¬)", "(｡♥‿♥｡)", "（╹◡╹）", "ヽ(*⌒∇⌒*)ﾉ", "(⌒‿⌒)", "(ﾟ▽ﾟ)", "o(〃＾▽＾〃)o",
        "(˘⌣˘)", "ᕕ( ᐛ )ᕗ", "(♡´∀`♡)", "(ღ✪v✪)ღ", "（＾ω＾）", "(*>ω<*)", "(*´ω`*)", "(*＾3＾/)", "(≧∇≦)"
    )

    private val arrPlay = listOf(
        "(｡•̀ᴗ-)✧", "⎛⎝(✿╹◡╹)⎠⎞", "( ̩˃̣̣̩(˃̣̣̩)", "o(〃＾▽＾〃)o", "ԅ(¯﹃¯ԅ)", "(～￣▽￣)～", "( ͡° ͜ʖ ͡°)", "(⊙_☉)",
        "(´• ᵕ •`)", "o(╥﹏╥)o", "(o˘◡˘o)", "( ˘ ³˘)❤", "(*≧ω≦)", "(๑˃̵ᴗ˂̵)و", "(╯°□°）╯", "(ʘ‿ʘ)", "(o•̀ᴗ-)✧", "(ﾉ◕ヮ◕)ﾉ*:･ﾟ✧",
        "(´｡• ᵕ •｡`)", "ʕ•ᴥ•ʔ", "(≧◡≦)", "ʕ•ᴥ•ʔ", "(≧∇≦)", "(｡♥‿♥｡)", "(*^ω^*)", "(╹◡╹)", "Σ(っﾟДﾟ)っ"
    )

    private val arrConfusedFace = listOf(
        "(⊙_☉)", "（＞人＜；）", "（˃̣̣̩⌓˂̣̣̩）", "(⊙_☉)", "( •́ ̯•̀ )", "（*＾3＾)/~☆", "(・_・ヾ", "（/_＼）", "（・◇・）", "( °д°)",
        "(｡•́︿•̀｡)", "(ಠ_ಠ)", "（・_・）", "(⩺_⩹)", "o(ﾟДﾟ)o", "（*゜ー゜*）", "(。・_・。)", "(ㅇ_ㅇ)", "（°Д°）", "(/ω＼)"
    )

    private val arrFear = listOf(
        "(ﾟДﾟ)", "(；￣Д￣)", "(」ﾟﾛﾟ)」", "（￣□￣；）", "(ﾟдﾟ)", "(⊙_☉)", "(・_・)", "((( ；ﾟДﾟ)))", "(；¬д¬)", "（（(ﾟДﾟ；））",
        "（￣ー￣）", "(•́ ̯•̀)", "（°ο°）", "（・◇・）", "(´•_•`)", "（；へ：）", "（・∀・）", "(❛‿❛)", "（¯︶¯）", "((( ；ﾟДﾟ)))"
    )

    private val arrOthers = listOf(
        "(╯°□°)╯", "(¬‿¬)", "(✪‿✪)", "(≧∇≦)", "(º ˃̣̣̩⌓˂̣̣̩)", "( ˘ ³˘)❤", "(⊙_☉)", "( ¯•¯)", "(˘︹˘)", "ʕ•ᴥ•ʔ",
        "(｡•́︿•̀｡)", "(✧ω✧)", "✧(｡♡‿♡｡)", "╰(°▽°)╯", "(⁄ ⁄•⁄ω⁄•⁄ ⁄)", "(⌐■_■)", "(｡•́︿•̀｡)", "（￣ー￣）", "(/^-^(^ ^*)/", "(´• ᵕ •`)"
    )
    val arraysList = listOf(
        arrAngry,
        arrBadMood,
        arrBear,
        arrBeg,
        arrConfused,
        arrCryFace,
        arrCuteFace,
        arrSmileFace,
        arrHappyFace,
        arrMoo,
        arrPlay,
        arrConfusedFace,
        arrFear,
        arrOthers
        // Add other arrays as needed, ensuring the order matches arrMenuName
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arrMenuName = arguments?.getStringArrayList("menuList") ?: listOf()
        val position = arguments?.getInt("position") ?: 0  // Retrieve the position

        val view = inflater.inflate(R.layout.fragment_shapes_bottom_sheet, container, false)
        /*    categoryMap = mapOf(
            arrMenuName[0] to arrAngry,
            arrMenuName[1] to arrBadMood,
            arrMenuName[2] to arrBear,
            arrMenuName[3] to arrBeg,
            // Continue mapping the rest
        )*/
        val centerTextView = view.findViewById<TextView>(R.id.center_text)
        centerTextView.text = arrMenuName[position]

        categoryMap = arrMenuName.indices.associate { index ->
            arrMenuName[index] to arraysList.getOrNull(index).orEmpty()
        }
        favoriteViewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)

        val recyclershape: RecyclerView = view.findViewById(R.id.recyclerkalematwashar)
        val allItemsToShow = categoryMap.flatMap { it.value }
        val imageclose: ImageView = view.findViewById(R.id.left_image)
        imageclose.setOnClickListener {
            parentFragmentManager.beginTransaction().remove(this).commit()
        }
            val adapter = ElashkalDetailAdapter(allItemsToShow,favoriteViewModel
            ,onFavoriteClicked={
                        text->handleFavoriteClick(text)
                }, onCopyClicked = {text->handleCopyClick(text)})
            recyclershape.layoutManager = LinearLayoutManager(context)
            recyclershape.adapter = adapter

            return view
        }

    private fun handleFavoriteClick(text: String) {
        ToastUtils.showCustomToast(requireContext(),
                "تمت اضافة الزخرفة الي المفضلة")

    }
    private fun handleCopyClick(text: String) {
        val context = requireContext()

        if (CopyCountManager.updateCopyCount(context)) {
            // Perform copy logic
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("label", text)
            clipboard.setPrimaryClip(clip)

            // Get the remaining copy count
            val remainingCount = CopyCountManager.getCopyCount(context)

            // Show a custom toast with the remaining count
            DialogUtils.showCustomToast(context, "تم النسخ متبقي لديك: $remainingCount")

            // If the copy limit is reached, show the reward dialog
            if (remainingCount == 0) {
                DialogUtils.showCopyLimitDialog(context, text)
            }
        } else {
            // Show the dialog when copy limit is reached
            DialogUtils.showCopyLimitDialog(context, text)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Retrieve data from arguments and use it
      //  val selIndex = arguments?.getInt("selIndex") ?: 0
      //  val strTitle = arguments?.getString("strTitle") ?: ""

        // Use selIndex and strTitle as needed
    }
    fun getFaces(category: String): List<String>? {
        return categoryMap[category]
    }
    override fun onStart() {
        super.onStart()
        val bottomSheetDialog = dialog as BottomSheetDialog?
        bottomSheetDialog?.let {
            val bottomSheet =
                it.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let { sheet ->
                val behavior = BottomSheetBehavior.from(sheet)

                // Set the initial state
                behavior.state = BottomSheetBehavior.STATE_EXPANDED

                // Add top margin (adjust this value as needed)
                val layoutParams = sheet.layoutParams as CoordinatorLayout.LayoutParams
                layoutParams.topMargin = 30 // Adjust margin value as needed
                sheet.layoutParams = layoutParams

                // Optional: Prevent collapsing beyond the desired height
                behavior.isHideable = false
                behavior.isDraggable = true
            }

        }
    }}



