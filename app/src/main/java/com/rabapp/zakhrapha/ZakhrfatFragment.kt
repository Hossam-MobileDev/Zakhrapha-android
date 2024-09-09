package com.rabapp.zakhrapha

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.textfield.TextInputLayout

private const val PREFS_NAME = "app_prefs"
private const val RETURN_COUNT_KEY = "return_count"
class ZakhrfatFragment() : Fragment(),SettingsFragment.OnThemeChangedListener {
    // TODO: Rename and change types of parameters
   // private val favoritesViewModel: SharedViewModel by viewModels()
    //private lateinit var clearButton: ImageButton
    private lateinit var favoriteViewModel: HistoryViewModel

    var interstitialAd: InterstitialAd? = null
    private var copyCount = 0

    //val defaultText = "أكتب هنا للزخرفة"
    var decoratedtext = "أكتب هنا للزخرفة"
    private val arabicLetters = listOf(
        "ا", "ب", "ج", "د", "ه", "و", "ز", "ح", "ط",
        "ي", "ك", "ل", "م", "ن", "س", "ع", "ف", "ص",
        "ق", "ر", "ش", "ت", "ث", "خ", "ذ", "ض", "ظ", "غ"
    )
    private val decorationArrays = listOf(
        listOf("ا", "ب̷🌹", "ج̷", "د̷", "ه", "ۆ̷", "ز̷", "ح̷", "ط̷", "ي🌹", "گ̷", "ل̷🌹", "م", "ن̷🌹", "س🌹", "ع̷ٍ", "ف̷", "ص̷", "ق̷", "ر̷", "ش", "ت̷🌹", "ث̷", "خ̷", "ذ̷", "ض", "ظ̷", "غ̷"),
        listOf("ا", "بـ☻ـ", "جـ☻ـ", "د", "هـ☻ـ", "ۅ", "ڒٍِ", "حـ☻ـ", "طـ☻ـ", "يـ☻ـ", "كـ☻ـ", "لـ☻ـ", "مـ☻ـ", "نـ☻ـ", "سـ☻ـ", "عـ☻ـ", "فـ☻ـ", "صـ☻ـ", "قـ☻ـ", "ر", "شـ☻ـ", "تـ☻ـ", "ثـ☻ـ", "خـ☻ـ", "ذّ", "ض", "ظـ☻ـ", "غـ☻ـ"),
        listOf("ا", "بـ❁", "جـ❁", "دٍ", "هّ", "وٌ", "زٍ", "حـ❁", "طـ❁", "ـيـ❁ـ", "كـ❁", "لَ", "مـ❁", "نـ❁ـ", "سـ❁ـ", "عـ❁", "ـفـ❁ـ", "ـصـ❁ـ", "قـ❁", "رٍ", "شـ❁", "ـتـ❁ـ", "ثـ❁", "خـ❁", "ذِ", "ضـ❁", "ـظـ❁ـ", "ـغـ❁ـ"),

        listOf(
            "ا", "ب̯͡", "ج̯͡", "د̯͡", "ہ̯͡", "ۆ̯͡", "ز̯͡", "ح̯͡", "ط̯͡", "ي̯͡",
            "ك̯͡", "ل̯͡", "م̯͡", "ن̯͡", "س̯͡", "ع̯͡", "ف̯͡", "ص̯͡", "ق̯͡", "ر̯͡",
            "ش̯͡", "ت̯͡", "ث̯͡", "خ̯͡", "ذ̯͡", "ض", "ظ̯͡", "غ̯͡"),
        listOf(
            "ا", "بـ♥̨̥̬̩", "جـ♥̨̥̬̩", "د", "هـ♥̨̥̬̩", "و", "ز", "حـ♥̨̥̬̩", "ط♥̨̥̬̩",
            "ي", "گ♥̨̥̬̩", "ل", "مـ♥̨̥̬̩", "ن", "س", "ع", "ف", "ص", "قـ♥̨̥̬̩", "ر",
            "ش", "تـ♥̨̥̬̩", "ثـ♥̨̥̬̩", "خ", "ذ", "ض", "ظ♥̨̥̬̩", "غ♥̨̥"
        ),
        listOf(
            "ا", "ب♠ـ", "جٍ ♠ـ", "د̀́", "ه̀́ـ ♠ـ", "ۈ̀́", "ز̀́", "ح̀́ ♠ـ", "طط̀́ ♠ـ",
            "ي̀́♠ـ", "ك̀́ ♠ـ", "ـل̀́", "م̀́ ♠ـ", "ن̀́ ♠ــ,", "س̀́سًٌُُ♠", "ـع̀́ ♠", "ـف̀́",
            "ص̀́", "ق̀́", "ر̀́", "ش̀́شُ", "ت♠ــ,", "ث", "خ̀́ ♠ـ", "ذ̀́،", "ض", "ظ̀́ ♠ـ", "غ̀́"
        ),

        listOf(
            "ا", "ب̀́ ــ✲ـ", "ج̀́ ــ✲ـ", "د̀́", "ه̀́ـ ــ✲", "ـۈ̀́", "ز", "ح̀́ ــ✲ـ", "طط̀́ ــ✲ـ",
            "ي̀́ــ✲ــﮯ", "ك̀́ ــ✲ـ", "ل̀́", "م̀́ ــ✲ـ", "ن̀́ ــ✲ــﮯ", "ســ✲ـ", "ع̀́ ــ✲ـ",
            "ف̀́ ــ✲ـ", "ص̀́ ــ✲ــﮯ", "ق̀́ ــ✲", "ـر", "ش̀́ ــ✲ـ", "ت̀́ ــ✲ــﮯ", "ث̀́ ــ✲ـ",
            "خ̀́ ــ✲", "ـذ", "ضــ✲", "ـظ̀́ ــ✲ـ", "غ̀́ ــ✲ــﮯ"
        ),
        listOf(
            "ا", "ب ͊ ͋ ͌", "ج ͊ ͋ ͌", "د", "ه ͊ ͋ ͌", "و", "ز", "ح ͊ ͋ ͌", "طط̀́ ͊ ͋ ͌",
            "يـﮯ", "ك ͊ ͋ ͌", "ل", "م ͊ ͋ ͌", "ن ͊ ͋ ͌ـﮯ", "س", "ع̀́", "ف̀́", "ص", "ق",
            "ر", "ش", "ت ͊ ͋ ͌ـﮯ", "ث", "خ ͊ ͋ ͌", "ذ", "ض", "ظ̀́ ͊ ͋ ͌", "غ̀́ ـﮯ"
        ),
        listOf(
            "ا", "بـޢޢـ", "جـޢޢــ", "ډ", "هـޢޢـ", "ۅ", "ڒٍ", "حـޢޢــ",
            "طـޢޢــ", "يـޢޢـ", "كـޢޢـ", "لـޢޢــ", "مـޢޢــ", "نـޢޢـ", "سـޢޢـ",
            "عـޢޢــ", "فـޢޢـ", "صـޢޢـ", "قـޢޢــ", "ر", "شـޢޢــ", "ـتـޢޢــ",
            "ثـޢޢــ", "خـޢޢـ", "ڏ", "ض", "ظـޢޢـ", "غـޢޢـ"
        ),
        listOf(
            "ا", "بّےـ", "جَےـ", "د", "هےـِ", "وُ", "ز", "حًےـ", "ط",
            "يّےـّےـّ", "كےـ", "لَ", "مِےـ", "نٌےـ", "سًےـ", "عَےـ", "ف",
            "صِےـ", "ق", "ر", "شّےـ", "تُےـ", "ثًےـ", "خٌےـ", "ذ", "ضےـےـ",
            "ظٌےـ", "غّےـ"
        ),

        listOf(
            "ا", "ٻ", "ﭳ", "ڍ", "ﮧ", "وٌ", "ڒ ـ", "ح", "طُـ", "ﮱ",
            "ڲ", "ڷ", "مُ", "ڹ", "ڛ", "عُ", "ڣ", "ڝ", "ڦ", "ږ", "ڜ",
            "ټ", "ٿ", "څ", "ڏ", "ض", "ڟ", "ڠ"
        ),
        listOf(
            "ا", "بـ,ـ", "جـ,ـ", "ډ", "هـ,ـ", "ۅ", "ڒٍ", "حـ,ـ", "طـ,ـ",
            "يـ,ـ", "كـ,ـ", "لـ,ـ", "مـ,ـ", "نـ,ـ", "سـ,ـ", "عـ,ـ", "فـ,ـ",
            "صـ,ـ", "قـ,ـ", "ر", "شـ,ـ", "تـ,ـ", "ثـ,ـ", "خـ,ـ", "ڏ", "ض",
            "ظـ,ـ", "غـ,ـ"
        ),
        listOf(
            "ا", "بہ", "جہ", "د", "ه", "و", "ز", "حہ", "طہ",
            "يہ", "كہ", "ل", "مہ", "نہ", "سہ", "عہ", "فہ", "صہ",
            "قہ", "ر", "شہ", "تہ", "ثہ", "خہ", "ذ", "ض", "ظہ", "غہ"
        ),

        listOf(
            "ا", "ب", "ج̶", "ـد", "ه", "و", "ڒ", "ح", "ـطـ", "يے", "ك", "ل",
    "ﻣ̲", "ن", "ﺳ̶̲ـ", "ﻋ̲", "فـ", "ﺻ̶ـ", "ق", "ـر", "ﺷ̶͠ـ", "ﭠ̲",
    "ث", "ﺧ̝̚ـ", "ذ", "ﺿ̲ـ", "ظـ", "ﻏ̲"
    ),
        listOf(
            "ا", "بـ♥", "جـ☠ـ", "د♥", "هـ☠ـ", "و", "ز♥", "حــ☠ـ", "طـ☠ـ",
            "يے", "كـ☠ـ", "لـ☠ـ", "مـ☠", "ـنـ☠ـے", "سـ♥", "عـ☠ـ", "ف",
            "ص", "ق", "ر", "شـ", "تـ♥ـ☠ـے", "ثُ", "خـ♥ـ☠ـ", "ـذ", "ض",
            "ظـ☠ـ", "غــ☠ـے"
        ),
        listOf(
            "ٵ", "ڀـ، ೋ", "ج", "د", "ه", "ۅ", "ݱــے", "ح", "طـ، ೋ",
            "ۑــے", "گ", "ڸـ، ೋ", "م", "ݩــے", "سـ، ೋ", "ع",
            "فـ، ೋ", "ص", "قـ، ೋ", "ݛ", "شـ، ೋ", "تـ، ೋــے", "ٿ",
            "خ", "ڋ", "ض", "ڟ", "غ"
        ),
        listOf(
            "ا", "بــ ͜ ͡ــ", "جــ ͜ ͡ــ", "د", "هــ ͜ ͡ــ", "و",
            "ز", "حــ ͜ ͡ــ", "طــ ͜ ͡ــ", "يــ ͜ ͡ـــﮯ", "ڪــ ͜ ͡ــ",
            "ل", "مــ ͜ ͡ــ", "نــ ͜ ͡ــ", "ســ ͜ ͡ــ", "عــ ͜ ͡ــ",
            "فــ ͜ ͡ــ", "صــ ͜ ͡ــ", "قــ ͜ ͡ــ", "ر", "شــ ͜ ͡ــ",
            "تــ ͜ ͡ــ", "ثــ ͜ ͡ــ", "خــ ͜ ͡ــ", "ذ", "ضــ ͜ ͡ــ",
            "ظــ ͜ ͡ــ", "غــ ͜ ͡ــ"
        ),
        listOf(
            "ٵ", "ݕ", "ج", "ݚ❀ے", "ۿ", "ۆ❀", "ۯ❀ے", "ح",
            "ط❀", "ۑ", "ك", "ڵ❀", "م❀", "ن❀ے", "س", "ع", "ف",
            "ص", "ق", "ڕ❀", "ۺ", "ٺ❀ے", "ٽ", "خ", "ۮ❀",
            "ض", "ظ", "غ"
        ),
        listOf("ا", "ب̯͡", "ج̯͡", "د̯͡", "هஹ", "وஹ", "ز̯͡", "ح̯͡", "ط̯͡", "يஹے", "ك̯͡ஹ", "ل̯͡ஹ", "م̯͡ஹ", "نஹ", "س", "ع̯͡", "ف̯͡ஹ", "ص̯͡", "ق̯͡ஹ", "ر̯͡", "ش", "ت̯͡", "ث̯͡", "خ̯͡", "ذ̯͡", "ض", "ظ̯͡ஹ", "غ̯͡"),
        listOf("ٵ̍", "ب", "چۚ", "د", "ه", "ﯣ", "ڙ", "حۡ", "ط", "ې", "ڪ", "ڵ", "مۘ", "نۨ", "ڛۜـ", "ﻋـ", "ڣ", "ڝ", "ڦ", "ڕ", "ڜ", "ټ", "ٽ", "څۡ", "ڌ", "ڞ", "ڟ", "ڠ"),
        listOf("ا", "ﭓ", "ﭸ", "ﮃ", "ﮪ", "ۈ", "ژ", "ﺣ̭͠", "طَ", "ې", "ﮖ", "ڷ", "ﻣ̝̚", "ڻ", "ﺳ̭͠", "ﻋ̝̚", "ﻓ̲̣̐", "ﺼ", "ڨ", "ڔ", "ﺷ͠", "ٺ", "ﺛ", "ﺧ", "ڈ", "ض", "ڟ", "ﻏ̣̐"),
        listOf("ا", "بٍّ", "جِّ", "دٍّ", "هِّ", "وٍّ", "زٍّ", "حَّ", "طِّ", "يِّ", "ﮝ", "لِّ", "مِّ", "نِّ", "سَّ", "عَّ", "فٍّ", "صِّ", "قٍّ", "رِ", "شِّ", "تٍّ", "ثٍّ", "خِّ", "ذ ِّ", "ضِّ", "ظِّ", "غَّ"),
        listOf("ٵ̍", "بــ", "ڇۚــ", "د", "هــ", "ﯜ", "ڙ", "حۡــ", "طــ", "ېْۧ", "ڪــ", "ڶــ", "مۘــ", "نۨ", "ڛۜــ", "ﻋــ", "ڣــ", "ڝ", "ڦــ", "ڔ", "ڜــ", "ټ", "ٽــ", "څۡــ", "ڌ", "ڞــ", "ڟــ", "ڠ"),
        listOf("ا", "ب̷🔥", "ج̷", "د̷", "ه", "ۆ̷", "ز̷", "ح̷", "ط̷", "ي🔥", "گ̷", "ل̷🔥", "م", "ن̷🔥", "س🔥", "ع̷ٍ", "ف̷", "ص̷", "ق̷", "ر̷", "ش", "ت̷🔥", "ث̷", "خ̷", "ذ̷", "ض", "ظ̷", "غ̷"),
        listOf("ا", "بـ♣︎", "جـ♣︎", "دٍ", "هّ", "وٌ", "زٍ", "حـ♣︎", "طـ♣︎", "ـيـ♣︎ـ", "كـ♣︎", "لَ", "مـ♣︎", "نـ♣︎ـ", "سـ♣︎ـ", "عـ♣︎", "ـفـ♣︎ـ", "ـصـ♣︎ـ", "قـ♣︎", "رٍ", "شـ♣︎", "ـتـ♣︎ـ", "ثـ♣︎", "خـ♣︎", "ذِ", "ضـ♣︎", "ـظـ♣︎ـ", "ـغـ♣︎ـ"),
        listOf("ا", "بـ❁", "جـ❁", "دٍ", "هّ", "وٌ", "زٍ", "حـ❁", "طـ❁", "ـيـ❁ـ", "كـ❁", "لَ", "مـ❁", "نـ❁ـ", "سـ❁ـ", "عـ❁", "ـفـ❁ـ", "ـصـ❁ـ", "قـ❁", "رٍ", "شـ❁", "ـتـ❁ـ", "ثـ❁", "خـ❁", "ذِ", "ضـ❁", "ـظـ❁ـ", "ـغـ❁ـ"),
        listOf("ا", "بـٌـٌٌـٌٌٌـٌٌـٌ", "ـٌجـ,ـ", "ـڊ", "ﮩ", "وُ", "ڒٍ", "حـًـًًـًًًـًًـً", "طُـٌـٌٌـٌ", "ي", "كُ", "لُـِـِِـِِِـِِـِ", "مـْـْْـْ", "نـِِـِـ", "ڛـ,ـ", "عٌـِـِِـِـ", "فُـ,ـ", "صُـ,ـ", "قٌـ,ـ", "ر", "شُـُـُُـُ", "تـٌـٌٌـ", "ثُ", "ځـٌٌـٌٌ", "ڏ", "ض", "ظً", "غٍـُـُُـُُُـُُُُـُُُـُُـُ"),
        listOf("ا", "بـ♔ـ", "جـ♔", "دٍ", "هّ", "وٌ", "زٍ", "حـ♔", "طـ♔ـ", "ـيـ♔ـ", "كـ♔ـ", "لَ", "مـ♔ـ", "نـ♔ـ", "سـ♔ـ", "عـ♔ـ", "ـفـ♔ـ", "ـصـ♔ـ", "قـ♔ـ", "رٍ", "شـ♔ـ", "ـتـ♔ـ", "ثـ♔ـ", "خـ♔ـ", "ذِ", "ضـ♔", "ـظـ♔ـ", "ـغـ♔ـ"),
        listOf("ا", "بـ༈ۖ҉ـ", "جـ༈ۖ҉ـ", "د", "هـ༈ۖ҉ـ", "ؤُ", "ز", "حـ༈ۖ҉ـ", "ط", "يـ༈ۖ҉ـ", "كـ༈ۖ҉ـ", "لَ", "مـ༈ۖ҉ـ", "نـ༈ۖ҉ـ", "سـ༈ۖ҉ـ", "عـ༈ۖ҉ـ", "فـ༈ۖ҉ـ", "صـ༈ۖ҉ـ", "قـ༈ۖ҉ـ", "ر", "شـ༈ۖ҉ـ", "تـ༈ۖ҉ـ", "ثـ༈ۖ҉ـ", "خـ༈ۖ҉ــ", "ذ", "ضـ༈ۖ҉ــ༈ۖ҉ـ", "ظـ༈ۖ҉ـ", "غـ༈ۖ҉ـ"),
        listOf("ا", "ب̷", "ج̷", "د̷🌵", "ه", "ۆ̷🌵", "ز̷🌵", "ح̷", "ط̷", "ي", "گ̷", "ل̷🌵", "م", "ن̷🌵", "س", "ع̷ٍ", "ف̷", "ص̷", "ق̷", "ر🌵", "ش", "ت̷🌵", "ث̷", "خ̷", "ذ̷", "ض", "ظ̷", "غ̷"),
        listOf("ا", "بـ≋ـ", "جـ≋ـ", "دٍ", "هّ", "وٌ", "زٍ", "حـ≋ـ", "طـ≋ـ", "يـ≋ـ", "كـ≋ـ", "لَ", "مـ≋ـ", "نـ≋ـ", "سـ≋ـ", "عـ≋ـ", "ـفـ≋ـ", "صـ≋ـ", "قـ≋ـ", "رٍ", "شـ≋ـ", "تـ≋ـ", "ثـ≋ـ", "خـ≋ـ", "ذِ", "ضـ≋ـ", "ظـ≋ـ", "غـ≋ـ"),
        listOf("ا", "بـ☪ـ", "جـ☪ـ", "دٍ", "هّ", "وٌ", "زٍ", "حـ☪ـ", "طـ☪ـ", "يـ☪ـ", "كـ☪ـ", "لَ", "مـ☪ـ", "نـ☪ـ", "سـ☪ـ", "عـ☪ـ", "فـ☪ـ", "صـ☪ـ", "قـ☪ـ", "رٍ", "شـ☪ـ", "تـ☪ـ", "ثـ☪ـ", "خـ☪ـ", "ذِ", "ضـ☪ـ", "ظـ☪ـ", "غـ☪ـ"),
        listOf("ا", "ب̮̭", "ج", "د̖͍ ̟", "ه", "و̻͇", "ز ͎", "ح͇", "ط̮͖", "ي̯ͅ ̖", "ك", "ل̹", "م", "ن ͕̹", "س͔ͅ", "ع͖", "ف͓̺", "ص͔ ̠̫", "ق̤", "ر", "ش", "ت̘ ̟̯", "ث", "خ", "ذ ̜̪", "ض͓͎", "ظ͙̖", "غ"),
        listOf("ا", "بـ☕︎ـ", "جـ☕︎ـ", "دٍ", "هّ", "وٌ", "زٍ", "حـ☕︎ـ", "طـ☕︎ـ", "يـ☕︎ـ", "كـ☕︎ـ", "لَ", "مـ☕︎ـ", "نـ☕︎ـ", "سـ☕︎ـ", "عـ☕︎ـ", "فـ☕︎ـ", "صـ☕︎ـ", "قـ☕︎ـ", "رٍ", "شـ☕︎ـ", "تـ☕︎ـ", "ثـ☕︎ـ", "خـ☕︎ـ", "ذِ", "ضـ☕︎ـ", "ظـ☕︎ـ", "غـ☕︎ـ"),
        listOf("ا", "بــ,ٰ♗ـ", "جــ,ٰ♗ـ", "د", "هہـــ,ٰ♗ـ", "و", "ز", "حــ,ٰ♗ـ", "طــ,ٰ♗ـ", "ي", "كــ,ٰ♗ـ", "ل", "مــ,ٰ♗ـ", "نــ,ٰ♗ـ", "س", "؏ــ,ٰ♗ـ", "ف", "ص", "ق", "ر", "ش", "تــ,ٰ♗ــ,ٰٓ", "ث", "خــ,ٰ♗ـ", "ذ", "ض", "ظــ,ٰ♗ـ", "غ"),
        listOf("ا", "ب̀́ ـ⚡ـ", "ج̀́ ـ⚡ـ", "د̀́", "ه̀́ـ ـ⚡ـ", "ۈ̀́", "ز", "ح̀́ ـ⚡ـ", "طط̀́ ـ⚡ـ", "ي̀́ـ⚡ـ", "ك̀́ ـ⚡ـ", "ل̀́", "م̀́ ـ⚡ـ", "ن̀́ ـ⚡ــﮯ", "سـ⚡ـ", "ع̀́ ـ⚡ـ", "ف̀́", "ص̀́", "ق̀́", "ر", "ش̀́", "ت̀́ ـ⚡ـ", "ث̀́", "خ̀́ ـ⚡ـ", "ذ", "ض", "ظ̀́ ـ⚡ـ", "غ̀́ ـ⚡ــﮯ"),
        listOf("ا", "ب⚙", "ج⚙", "د", "ه̀́ـ ⚙", "ۈ̀́", "ز", "ح⚙", "ط⚙", "ي̀́⚙ـﮯ", "ك⚙", "ل", "م̀́ ⚙", "ن̀́ ⚙", "س", "ع", "ف", "ص", "ق", "ر", "ش", "ت⚙", "ث", "خ⚙", "ذ", "ض", "ظ", "غ"),
        listOf("ا", "بـۗـۗـۙ", "جـۗـۗـۙ", "د", "ه", "و", "ز", "حـۗـۗـۙ", "طـۗـۗـۙ", "ي", "كـۗـۗـۙ", "ل", "مـۗـۗـۙ", "نـۗـۗـۙ", "سـۗـۗـۙ", "عـۗـۗـۙ", "فـۗـۗـۙ", "صـۗـۗـۙ", "قـۗـۗـۙ", "ر", "شـۗـۗـۙ", "تـۗـۗـۙ", "ثـۗـۗـۙ", "خـۗـۗـۙ", "ذ", "ضـۗـۗـۙ", "ظـۗـۗـۙ", "غـۗـۗـۙ"),
        listOf("ا", "بـ❊ـ", "جـ❊ـ", "دٍ", "هّ", "وٌ", "زٍ", "حـ❊ـ", "طـ❊ـ", "يـ❊ـ", "كـ❊ـ", "لَ", "مـ❊ـ", "نـ❊ـ", "سـ❊ـ", "عـ❊ـ", "فـ❊ـ", "صـ❊ـ", "قـ❊ـ", "رٍ", "شـ❊ـ", "تـ❊ـ", "ثـ❊ـ", "خـ❊ـ", "ذِ", "ضـ❊ـ", "ظـ❊ـ", "غـ❊ـ"),
        listOf("ٵ", "ݕ❣", "ج❣", "ݚ", "ۿ", "ۆ", "ۯ", "ح", "ط❣", "ۑے", "ك", "ڵ❣", "ݦ", "ݩ", "س", "ع", "ف❣", "ص", "ق❣", "ڕ", "ۺ", "ٺ❣", "ٽ❣", "خ", "ۮ", "ض", "ظ", "غ"),
        listOf("ا", "ب", "ج☃", "دے", "ه", "و", "ڒے", "ح☃", "ط☃", "ي", "ك", "ل", "ﻣ̲", "نے", "ﺳ̶̲", "ﻋ̲", "ف☃", "ﺻ̶", "ق☃", "ر", "ش", "ﭠ̲ے", "ث", "ﺧ̝̚", "ذے", "ﺿ̲", "ظ☃", "ﻏ̲"),
        listOf("أً̜̌", "ب", "جٍ", "ﮃ", "ﮩ", "ۈ", "ڒٍ", "حـًـًًـًًًـًًـً", "طـ.ـ", "يہ", "گ", "لہ", "مـ", "نہ", "سہٰ", "عـُـُُـُُُـُُُُـُُُـُُـُ", "فُـ,ـ", "ـصہ", "قـ.ــ", "رٍ", "شُـُـُُـُ", "تـ,", "ثـ", "ځـٌٌـٌٌ", "ڏ", "ضہٰ", "ظـ.ـ", "غـُـُُـُُُـُُُـُُُـُُـُ")


    )

    lateinit var adapter: ZakharefViewAdapter
    private lateinit var editextzakhrfat: EditText

    private var selectedDecorationIndex = 0
    //private val database: AppDatabase? = AppDatabase.getDatabase(requireActivity())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

        MobileAds.initialize(requireContext())
       // loadInterstitialAd()// Initialize once in onCreate
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      //  MobileAds.initialize(requireContext())


       // interstitialAd?.show(requireActivity())
      //  updateReturnCount(requireContext())
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_zakhrfat, container, false)
        val zakhrfatrecyclerview = view.findViewById<RecyclerView>(R.id.recyclerzakharef)
        //val buttonad = view.findViewById<Button>(R.id.showad)
/*
        buttonad.setOnClickListener {    interstitialAd?.show(requireContext() as Activity)}
*/
        favoriteViewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)
        zakhrfatrecyclerview.layoutManager = LinearLayoutManager(context)
        //clearButton = view.findViewById(R.id.clearButton)
      /*  clearButton.setOnClickListener {
            editextzakhrfat.text.clear()
        }*/
        adapter = ZakharefViewAdapter(emptyList(),
            onFavoriteClicked={
                    text->handleFavoriteClick(text)}, onCopyClicked = {text->handleCopyClick(text)},requireContext(),
            favoriteViewModel



         ){ index->
            val currentText = editextzakhrfat.text.toString()
            decoratedtext = applyDecoration(currentText, index)
             updateDecoratedText(decoratedtext)
       }
zakhrfatrecyclerview.adapter = adapter
editextzakhrfat = view.findViewById(R.id.zahkrafatedittext)
        val textInputLayout = view.findViewById<TextInputLayout>(R.id.text_input_layout)
        textInputLayout.setEndIconOnClickListener {
            editextzakhrfat.setText("") // Clear the EditText
            adapter.updateItemsList(emptyList()) // Clear the RecyclerView list
        }
        editextzakhrfat.setOnClickListener {
            // Clear the EditText when clicked
            //editextzakhrfat.setText("")
            editextzakhrfat.text?.clear() // Clear the editable content
            editextzakhrfat.requestFocus()

           // Toast.makeText(context, "EditText clicked", Toast.LENGTH_SHORT).show()


            // Clear the adapter list (reset it with an empty list)
           // adapter.updateItemsList(emptyList())  // You need a method in your adapter to update the list
        }


        editextzakhrfat.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)

        editextzakhrfat.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val currentText = s.toString()
                    updateDecoratedText(currentText)


                }

                override fun afterTextChanged(s: Editable?) {
                    //clearButton.visibility = if (s?.isNotEmpty() == true) View.VISIBLE else View.GONE
                }
            }
        )

       // val defaultText = "زخرفات"
        editextzakhrfat.setText(decoratedtext)
        
        return view
    }

    /*private fun saveToHistory(item: String) {
        val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        val historyItem = HistoryItem(item, currentDate)
        database?.historyDao()?.insert(historyItem)
    }*/
    private fun loadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(requireContext(), "ca-app-pub-1439642083038769/2356781271"
                , adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    // The ad was successfully loaded
                    interstitialAd = ad
                    Log.d("AdDisplay", "Ad Loaded")

                    // Set FullScreenContentCallback
                    interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            // Called when ad is dismissed
                            Log.d("AdDisplay", "Ad Dismissed")
                            // Optionally, load a new ad
                            loadInterstitialAd()
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            // Called when ad fails to show
                            Log.d("AdDisplay", "Ad Failed to Show: ${adError.message}")
                        }

                        override fun onAdShowedFullScreenContent() {
                            // Called when ad is shown
                            Log.d("AdDisplay", "Ad Shown")
                        }
                    }
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    // Handle the error
                    interstitialAd = null
                    Log.d("AdDisplay", "Ad Failed to Load: ${error.message}")
                }
            })
    }
    fun shouldShowReturnAd(context: Context): Boolean {
        val returnCount = getReturnCount(context)
        return returnCount >= 2
    }

    private fun getReturnCount(context: Context): Int {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(RETURN_COUNT_KEY, 0)
    }

    private fun updateReturnCount(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val returnCount = sharedPreferences.getInt(RETURN_COUNT_KEY, 0)
        val editor = sharedPreferences.edit()
        editor.putInt(RETURN_COUNT_KEY, returnCount + 1)
        editor.apply()
    }


    override fun onResume() {
        super.onResume()
        // Update the return count on resuming the app
       // updateReturnCount(requireContext())
        //updateReturnCount(requireContext())
        // Example condition to show the ad

       /* if (shouldShowReturnAd(requireContext())) {
            interstitialAd?.let { ad ->

                ad.show(requireActivity())
                resetReturnCount(requireContext()) // Reset the return count after showing the ad

            }
        }*/
    }
    override fun onPause() {
        super.onPause()

        // Increment return count when app goes into the background
       // updateReturnCount(requireContext())
    }
    fun resetReturnCount(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(RETURN_COUNT_KEY, 0)
        editor.apply()
    }
    private fun handleFavoriteClick(text: String) {

        ToastUtils.showCustomToast(requireContext(),
            "تمت اضافة الزخرفة الي المفضلة")    }

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
    private fun updateDecoratedText(text: String) {
        // Decorate the text based on the selected decoration index
        val decoratedTexts = mutableListOf<String>()

        if (text.isNotEmpty()) {
            // Apply each decoration to the text
            for (i in decorationArrays.indices) {
                val decoratedText = applyDecoration(text, i)
                decoratedTexts.add(decoratedText)
            }
            decoratedTexts.add("【【【【【【【【【【$text░░░░░░░░░░░")
            // Add additional decorations
            decoratedTexts.add("`•.,¸¸,.•´¯   🎀$text🎀   ¯´•.,¸¸,.•`")
            decoratedTexts.add("*•.¸♡  $text  ♡¸.•*")  // Fancy decoration with hearts
            decoratedTexts.add("✿´¯`•.¸¸✿  $text  ✿´¯`•.¸¸✿")  // Flowers decoration
            decoratedTexts.add("❖━━━✦❘༻༺❘✦━━━❖  $text  ❖━━━✦❘༻༺❘✦━━━❖") // Intricate symbols
            decoratedTexts.add("♔.•°  $text  °•.♔") // Crown and dots decoration
            decoratedTexts.add("➶➶➶➶➶➶➶➶ $text ➷➷➷➷➷➷➷➷") // Arrows decoration

            // Optionally, add more decorated variants dynamically
            decoratedTexts.add("🌸🌸🌸 $text 🌸🌸🌸")  // Flowers decoration
            // Update RecyclerView Adapter with new decorated texts
            adapter.updateData(decoratedTexts)
        }
    }
    private fun applyDecoration(inputText: String, decorationIndex: Any): String {
        val selectedDecorationArray = decorationArrays[decorationIndex as Int]
        val decoratedText = StringBuilder()

        for (character in inputText) {
            val index = arabicLetters.indexOf(character.toString())
            if (index != -1) {
                decoratedText.append(selectedDecorationArray[index])
            } else {
                decoratedText.append(character)
            }
        }

        return decoratedText.toString()
    }

    override fun onThemeChanged(isDarkTheme: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkTheme) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
        //requireActivity().recreate()
    }
}