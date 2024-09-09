package com.rabapp.zakhrapha

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
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

class KalematwAshaarBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var arrMenuName: List<String>
    private lateinit var categoryMap: Map<String, List<String>>
    private lateinit var favoriteViewModel: HistoryViewModel
    private var copyCount = 0
    var sentencesArray : List<List<String>> = listOf(
        listOf(
            "عيدٌ بِأَيَّةِ حالٍ عُدتَ يا عيدُ / بِما مَضى أَم بِأَمرٍ فيكَ تَجديدُ",
            "أَمّا الأَحِبَّةُ فَالبَيداءُ دونَهُمُ / فَلَيتَ دونَكَ بيداً دونَها بيدُ",
            "لَولا العُلى لَم تَجُب بي ما أَجوبُ بِها / وَجناءُ حَرفٌ وَلا جَرداءُ قَيدودُ",
            "وَكانَ أَطيَبَ مِن سَيفي مُضاجَعَةً / أَشباهُ رَونَقِهِ الغيدُ الأَماليدُ",
            "لَم يَترُكِ الدَهرُ مِن قَلبي وَلا كَبِدي / شَيْئاً تُتَيِّمُهُ عَينٌ وَلا جيدُ",
            "يا ساقِيَيَّ أَخَمرٌ في كُؤوسِكُما / أَم في كُؤوسِكُما هَمٌّ وَتَسهيدُ ",
            "أَصَخرَةٌ أَنا مالي لا تُحَرِّكُني / هَذي المُدامُ وَلا هَذي الأَغاريدُ",
            "إِذا أَرَدتُ كُمَيتَ اللَونِ صافِيَةً / وَجَدتُها وَحَبيبُ النَفسِ مَفقودُ",
            "ماذا لَقيتُ مِنَ الدُنيا وَأَعجَبُهُ / أَنّي بِما أَنا باكٍ مِنهُ مَحسودُ",
            "أَمسَيتُ أَروَحَ مُثرٍ خازِناً وَيَداً / أَنا الغَنِيُّ وَأَموالي المَواعيدُ",
            "إِنّي نَزَلتُ بِكَذّابينَ ضَيفُهُمُ / عَنِ القِرى وَعَنِ التَرحالِ مَحدودُ",
            "مُالُيّ خِلُق حتّى اعّيّشَ رًبُيّ الُمُؤٌتّ حَقً ؤٌانَا بُطِالُبُ بُحَقًيّ / الذّوْقُ زَهْرَهْ ..لآ تُنْبَتُ فِـي كُلّ الحَدَآئِقْ ..",
            "هجرتِ فلم نجد ظلًّا يقينا / أحُلْمًا كان عطفُكِ أم يقينا؟",
            "أهجرًا في الصبابة بعد هجرٍ / أرى أيامَه لا ينتهينا",
            "لقد أسرفتِ فيه وجُرتِ حتى / على الرَّمق الذي أبقيتِ فينا",
            "كأن قلوبنا خُلِقَتْ لأمرٍ/ فمذْ أبصرنَ من نهوى نسينا ",
            "شُغِلْنَ عن الحياة ونِمْنَ عنها / وبِتن بمنْ نحبُّ موكلينا ",
            "فإن مُلِئت عروق من دماءٍ / فإنَّا قد ملأناها حنينَا!"
        ),
        listOf(
            "ليس اليتيم من مات والده إن اليتيم يتيم العلم والأدب .",
            "أول العلم الصمت والثاني الاستماع والثالث الحفظ والرابع العمل والخامس نشره .",
            "النجاح لا يحتاج إلى أقدام بل إلى إقدام .",
            "إن الشجرة المثمرة هي التي يهاجمها الناس.",
            "بالابتسامة تذلل الصعاب.",
            "من لم يتعلم في صغره لم يتقدم في كبره.",
            "من فتح مدرسة أقفل سجنا.",
            "اطلبوا العلم من المهد إلى اللحد.",
            "العلم في الصغر كالنقش على الحجر.",
            "العلم دواء لسموم الخرافات.",
            "ما كل ما يتمنى المرء يدركه, تأتى الرياح بما لا تشتهى السفن.",
            "لكل داء دواء يستطب به، إلا الحماقة أعيت من يداويها.",
            "كل المصائب قد تمر على الفتى، فتهون، غير شماتة الأعداء.",
            "لا بد من فقد ومن فاقد، هيهات ما في الناس من خالد.",
            "رب رمیة من غیر رام.",
            "عداوة العاقل أقل ضررا من مودة ألجاھل.",
            "لا تجادل بليغا ولا سفيها، فالبليغ يغلبك والسفيه يؤذيك.",
            "الكلمة كالرصاصة، إذا خرجت فات الأوان على إرجاعها.",
            "إذا أردت أن تطاع فأمر بما يستطاع.",
            "في سعة الأخلاق كنوز الأرزاق.",
            "إذا المرء لم يدنس من اللؤم عرضه فكل رداء يرتديه جميل.",
            "لا يمكن للإنسان أن يصبح عالما قبل أن يكون إنسانا.",
            "من راقب الناس مات هما.",
            "بلاء الإنسان من اللسان.",
            "ما تريد نيله بالإرهاب يسهل عليك نيله بالابتسام.",
            "ليس كل ما في القلب يحكي، فبعض الصمت أجمل.",
            "كثرة اللوم و العتاب تورث البغضاء .",
            "ليكن وجهك باسما وكلامك لينا، تكن أحب إلي الناس ممن يعطيهم الذهب والفضة.",
            "نفس جميلة في جسد جميل هو المثل الأعلى للجمال.",
            "النجاح مسألة حظ , وأسأل كل فأشل",
            "وراء كل رجل عظيم .. أم و أب عظيمان .",
            "كل إناء ينضح بما فيه .",
            "من لا خير فيه لأهله ، لا خير فيه لأحد .",
            "من يستعجل قطف العنب قبل نضوجه ، يأكله حامضا .",
            "دوام الحال من المحال .",
            "من استهان بالوقت نبذه الزمن .",
            "يهب الله كل طائر رزقه ولكنه لا يلقيه له في العش .",
            "لولا العمل لأنقرض الناس .",
            "إذا شاورت العاقل ، صار عقله لك.",
            "من يستيقظ متأخرا يركض طوال النهار.",
            "صادق نفسك فهي الوحيدة التي لن تخونك مهما فعلت بها ودائما ما تدلك على ما بداخلك بكل صدق وبدون رياء.",
            "لو سكتت الأسود عن زئيرها لأقبلت الغربان بنعيقها.",
            "الكثير من الشيء يقلل من ثمنه إلا العقل الناضج كلما زادت ثقافته ندر وعلا شأنه.",
            "افة الحديث الكذب.",
            "كلما ارتفع الشريف تواضع، وكلما ارتفع الحقير تكبر.",
            "كـل شـيء إذا كثر رخص إلا الأدب فـأنه إذا كثر غلا.",
            "أن توقد شمعه خيرا من أن تلعن الظلام.",
            "ليست حقيقة الإنسان بما يظهره لك، بل بما لإيستطيع إن يظهره، لذلك إذا أردت أن تعرفه، فلا تصغ إلى مائقوله بل إلى ما لا يقوله.",
            "ما أنبل القلب الحزين الذي لايمنعه حزنه على أن ينشد أغنية مع القلوب الفرحة.",
            "أعز مكان في الدنيا سرج سابح وخير جليس في الزمان كتاب.",
            "إذا سمعت الرجل يقول فيك من الخير ما ليس فيك فلا تأمن أن يقول فيك من الشر ما ليس فيك.",
            "العلم زين فكن للعلم مكتسبا وكن له طالبا ما عشت مقتبسا.",
            "أطلب من العلوم علما ينفعك ينفي الأذى والعيب ثم يرفعك.",
            "لا تقل يا رب عندي هم كبير، ولكن قل يا هم عندي رب كبير.",
            "في كثير من الأحيان، خسارة معركة تعلمك كيف تربح الحرب.",
            "لا يجب أن تقول كل ما تعرف ولكن يجب أن تعرف كل ما تقول.",
            "لا تبصق في البئر، فقد تشرب منه يوما.",
            "ليست الألقاب هي التي تكسب المجد بل الناس من يكسبون الألقاب مجدا.",
            "يوجد كثير من المتعلمين، ولكن قلة منهم مثقفون.",
            "ليس من الصعب أن تضحي من أجل صديق ولكن من الصعب أن تجد الصديق الذي يستحق التضحية.",
            "إن مفتاح الفشل هو محاولة إرضاء كل شخص تعرفه.",
            "إذا ركلك أحد من خلفك، فاعلم أنك في المقدمة.",
            "من أحب الله ، رأى كل شيء جميلا.",
            "رحم الله امرئ عرف قدر نفسه.",
"   الإهانة لا تأتيك ممن ينتقدك، بل من حكمك الذي يجعلك تعت  "

            )

    )





                    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_kalematw_ashaar_bottom_sheet, container, false)
        // Inflate the layout for this fragment
        val recyclervkalematwash:RecyclerView = view.findViewById(R.id.recyclerkalematwashar)
        val imageclose:ImageView = view.findViewById(R.id.left_image)
        imageclose.setOnClickListener {     parentFragmentManager.beginTransaction().remove(this).commit()
        }
        val position = arguments?.getInt("position") ?: 0  // Retrieve the position
        val centerTextView = view.findViewById<TextView>(R.id.center_text)

        arrMenuName = arguments?.getStringArrayList("menuList")?.toList() ?: listOf()
        categoryMap = arrMenuName.indices.associate { index ->
            arrMenuName[index] to (sentencesArray.getOrNull(index) ?: listOf())
        }
            val allItemsToShow = categoryMap.flatMap { it.value }
        favoriteViewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)

        val adapter = KalematwAsharDetailAdapter(allItemsToShow,onFavoriteClicked={
                text->handleFavoriteClick(text)
        }, onCopyClicked = {text->handleCopyClick(text)},requireContext(),favoriteViewModel)
        recyclervkalematwash.layoutManager = LinearLayoutManager(context)

        recyclervkalematwash.adapter = adapter
        centerTextView.text = arrMenuName[position]

        return view
        }

    private fun handleFavoriteClick(text: String) {
        ToastUtils.showCustomToast(requireContext(),
            "تمت اضافة الزخرفة الي المفضلة")
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the ViewModel

        // Now you can safely use favoriteViewModel
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




