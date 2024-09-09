package com.rabapp.zakhrapha

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AsmaaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AsmaaFragment() : Fragment() {
    private lateinit var favoriteViewModel: HistoryViewModel
    private val decoratedNames = mutableListOf<String>()
    private lateinit var adapter: AsmaaViewAdapter
    private var copyCount = 0

    val defaulttext = "اسم"
    val arrNamePattern = listOf(
        "꧁...꧂",
        "の⫷♥̨̥̬̩ ... ♥̨̥̬̩⫸气",
        ",-*'^'~*-.,_,.-*~🎀 ... 🎀~*-.,_,.-*~'^'*-,",
        "⫷♥̨̥̬̩ ... ♥̨̥̬̩⫸",
        " (◔◡◔)♥ ... ♥(◔◡◔)",
        " 🐢  🎀...🎀  🐢",
        "░꧁༒☬ ... ☬༒꧂░",
        "••._.•• ... ••._.••",
        " ♥(◔っ◔ ... ◔っ◔)♥",
        "🐙 ⋆ 🐐  🎀 ...  🎀  🐐 ⋆ 🐙",
        " 父༺ ... ༻ᴳᵒᵈツ",
        "`•.,¸¸,.•´¯🎀  ... 🎀   ¯´•.,¸¸,.•`",
        "♧☹ ... 💜♝",
        " ♛『...』♛",
        "`•.¸¸.•´´¯`••._.• ... •._.••`¯´´•.¸¸.•`",
        " ♜々... ★♜",
        "░▒▓█►─═  ... ═─◄█▓▒░",
        "░꧁༒☬...☬༒꧂░",
        " ˜”*°•.˜”*°• ... •°*”˜.•°*”˜",
        "卍...爪ツ",
        "]|I{•------» ... «------•}I|[",
        "¸,ø¤º°`°º¤ø,¸¸,ø¤º°  ... °º¤ø,¸¸,ø¤º°`°º¤ø,¸",
        "🐻  ᯽ ...  ᯽  🐻",
        "✴  ᯽ ...  ᯽  ✴",
        "｡  ᯽ ...  ᯽  ｡",
        "*  ᯽ ...  ᯽  *",
        ".🌠  ᯽ ...  ᯽  🌠.",
        "✿ᴰᵃʳᴋ᭄♝  ...  ሃ❀",
        "♟◺  ...  ®⛛",
        "☸...☸",
        "™ⓥ  ...  ⚑ᐖ",
        "☸...",
        "«-(¯`v´¯)-« ... »-(¯`v´¯)-»",
        "🍃🌺🍃...🍃🌺🍃",
        "•°¤(¯`°(☺)(( ... ))(☺)°´¯)¤°•",
        "✿◕ ‿ ◕✿... ✿◕ ‿ ◕✿",
        "🌳🌻🌳...🌳🌻🌳",
        "●○●○●○●...●○●○●○●",
        "❝¸¸.....¸¸❝",
        "ꕥꕦꕧꕦꕥ...ꕥꕦꕧꕦꕥ",
        "🌲🌼🌲...🌲🌼🌲",
        "ꔵꔶꔵꔶ...ꔵꔶꔵꔶ",
        "✦⊱⊰✦...✦⊱⊰✦",
        "ꘐꘑꘐꘑ...ꘐꘑꘐꘑ",
        "🍀🌹🍀...🍀🌹🍀",
        "✿⊱⊰✿...✿⊱⊰✿",
        "ꗂꗃꗂꗃ...ꗂꗃꗂꗃ",
        "ꔷꔸꔷꔸ...ꔷꔸꔷꔸ",
        "✪✧✪✧✪...✪✧✪✧✪",
        "🌾🌷🌾...🌾🌷🌾",
        "۞༓۞༓۞...۞༓۞༓۞",
        "❉✺❉...❉✺❉",
        "꒡꒢꒡꒢...꒡꒢꒡꒢",
        "❂⊱⊰❂...❂⊱⊰❂",
        "✵⊱⊰✵...✵⊱⊰✵",
        "🌿🌷🌿...🌿🌷🌿",
        "۩♥¤...¤♥۩",
        "ꖜꖝꖜꖝ...ꖜꖝꖜꖝ",
        "☜♥☞ º°”˜`”°º☜( ... )☞ º°”˜`”°☜♥☞",
        "ꔴꔵꔴꔵ...ꔴꔵꔴꔵ",
        "🌾🌿✿🌻...🌾🌿✿🌻",
        "✧⊱⊰✧...✧⊱⊰✧"
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

        val view = inflater.inflate(R.layout.fragment_asmaa, container, false)
        val asmaaedittext = view.findViewById<EditText>(R.id.asmaedittext)
        val imageclose = view.findViewById<ImageView>(R.id.clearButton)
        favoriteViewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)

        imageclose.setOnClickListener{
            asmaaedittext.text.clear()
            updateDecoratedNames("")
            decoratedNames.clear()
            adapter.notifyDataSetChanged()  // This will only affect AsmaaFragment

        }
        asmaaedittext.setText("اسم")
        asmaaedittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateDecoratedNames(s.toString())

            }
            override fun afterTextChanged(s: Editable?) {}
        })
        val zakhrfatrecyclerview = view.findViewById<RecyclerView>(R.id.recyclerasmaa)
        zakhrfatrecyclerview.layoutManager = LinearLayoutManager(context)
      adapter = AsmaaViewAdapter(decoratedNames,onFavoriteClicked={
              text->handleFavoriteClick(text)},onCopyClicked = {text->handleCopyClick(text)},requireContext(),
          favoriteViewModel
      )
        updateDecoratedNames(defaulttext)
        zakhrfatrecyclerview.adapter = adapter

        return view
    }

    private fun updateDecoratedNames(name: String) {
        decoratedNames.clear()
        for (pattern in arrNamePattern) {
            decoratedNames.add(pattern.replace("...", name))
        }
        adapter.notifyDataSetChanged()
    }
    private fun handleFavoriteClick(text: String) {
        // Add logic to handle favorite action
        // adapter.toggleFavorite(text)
        // favoritesViewModel.addFavorite(text)
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

}