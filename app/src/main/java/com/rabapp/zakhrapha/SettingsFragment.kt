package com.rabapp.zakhrapha

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory

class SettingsFragment : BottomSheetDialogFragment() {
    private lateinit var reviewManager: ReviewManager
    interface OnThemeChangedListener {
        fun onThemeChanged(isDarkTheme: Boolean)
    }

    private var listener: OnThemeChangedListener? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = parentFragment as? OnThemeChangedListener
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
    // Use a companion object for factory methods
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.FullScreenBottomSheetDialog)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onStart() {
        super.onStart()

        val dialog = dialog as? BottomSheetDialog
        dialog?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED
        dialog?.behavior?.skipCollapsed = true
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        val imagerate: ImageView? = view?.findViewById(R.id.rateimg)
        val imagegmail: ImageView? = view?.findViewById(R.id.idgmail)
        val imagetheme: ImageView? = view?.findViewById(R.id.imgtheme)
        val imagalertgmail:ImageView? = view?.findViewById(R.id.alertgmail)
        val imaggmail:ImageView? = view?.findViewById(R.id.gmag)
        val imagshare:ImageView? = view?.findViewById(R.id.shareapp)
imagshare?.setOnClickListener {  shareApp()}
        imaggmail?.setOnClickListener { sendEmail() }
        imagalertgmail?.setOnClickListener { sendEmail() }
        val imkitaba:ImageView? = view?.findViewById(R.id.imgIconLeftkitaba)
        imkitaba?.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse("https://apps.apple.com/us/app/%D9%83%D8%AA%D8%A7%D8%A8%D8%A9-%D8%B9%D9%84%D9%89-%D8%A7%D9%84%D8%B5%D9%88%D8%B1-%D8%AA%D8%B5%D9%85%D9%8A%D9%85-%D8%B5%D9%88%D8%B1/id958075714"))
            startActivity(intent);

        }
        val imgkortoba:ImageView? = view?.findViewById(R.id.imgweather)
        imgkortoba?.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse("https://apps.apple.com/us/app/weather-forecast-sun-app/id1492430426"))
            startActivity(intent);
           // Toast.makeText(requireContext(),"jjjjj",Toast.LENGTH_SHORT).show()

        }
        imagetheme?.setOnClickListener{
            val isCurrentlyDarkTheme = ThemeManager.isDarkThemeEnabled(requireContext())

            // Toggle the theme preference
            ThemeManager.setDarkThemeEnabled(requireContext(), !isCurrentlyDarkTheme)

            // Apply the theme change across the app
            AppCompatDelegate.setDefaultNightMode(
                if (!isCurrentlyDarkTheme) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )

            // Recreate the current activity to reflect the change
            activity?.recreate()  // This will restart the activity and apply the new theme

        }




        imagegmail?.setOnClickListener {
            sendEmail()
        }
        imagerate?.setOnClickListener {
            showRateApp()
        }
        // Inflate the layout for this fragment
        return view
    }


        private fun shareApp() {
            val packageName = requireContext().applicationContext.packageName  // Get your app's package name
            val appUrl = "https://play.google.com/store/apps/details?id=$packageName"  // Link to the Play Store

            // Intent to share app link
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out this app")
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Download this amazing app from the Play Store: $appUrl")

            try {
                startActivity(Intent.createChooser(shareIntent, "Share via"))
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(requireContext(), "No app available to share", Toast.LENGTH_SHORT).show()
            }
        }

    private fun setDarkTheme(enable: Boolean) {
        val mode = if (enable) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(mode)

        // Save the preference
        val sharedPref = requireContext().getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
        sharedPref.edit().putBoolean("is_dark_theme", enable).apply()

        // Recreate the activity to apply the new theme
        activity?.recreate()
    }

    private fun isDarkThemeEnabled(): Boolean {
        val sharedPref = requireContext().getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("is_dark_theme", false)
    }
    private fun showRateApp() {
        reviewManager = ReviewManagerFactory.create(requireContext())
        val request: Task<ReviewInfo> = reviewManager.requestReviewFlow()

        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Request succeeded and ReviewInfo is available
                val reviewInfo: ReviewInfo = task.result
                launchReviewFlow(reviewInfo)
            } else {
                // Request failed, fallback to Play Store link
                openPlayStoreForRating()
            }
        }
    }
    private fun launchReviewFlow(reviewInfo: ReviewInfo) {
        val reviewManager = ReviewManagerFactory.create(requireContext())  // Get the ReviewManager

        val flow: Task<Void> = reviewManager.launchReviewFlow(requireActivity(), reviewInfo)

        flow.addOnCompleteListener { reviewTask ->
            if (reviewTask.isSuccessful) {
                // The review flow completed successfully
                Toast.makeText(requireContext(), "Thank you for reviewing the app!", Toast.LENGTH_SHORT).show()
            } else {
                // Handle the error (e.g., network issues, the user canceled, etc.)
                Toast.makeText(requireContext(), "Review flow failed or was canceled.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Fallback method to open the Play Store directly
    private fun openPlayStoreForRating() {
        val playStoreUri = Uri.parse("market://details?id=${requireContext().packageName}")
        val playStoreIntent = Intent(Intent.ACTION_VIEW, playStoreUri)
        playStoreIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(playStoreIntent)
    }
    private fun sendEmail() {
        val recipientEmail = "mmadooooo30@gmail.com"
        val subject = "Feedback: App Suggestion"
        val body = "Hello,\n\nI would like to suggest..."

        // Create the intent with ACTION_SENDTO for the default email app
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$recipientEmail") // Only email apps should handle this
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }

        // Use context or requireActivity() to get the package manager
        val packageManager = context?.packageManager ?: requireActivity().packageManager

        // Check if there's an app that can handle this intent
        if (intent.resolveActivity(packageManager) != null) {
            try {
                // Attempt to open Gmail app directly if available
                val gmailIntent = Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(recipientEmail))
                    putExtra(Intent.EXTRA_SUBJECT, subject)
                    putExtra(Intent.EXTRA_TEXT, body)
                    type = "message/rfc822"
                    setPackage("com.google.android.gm") // Target Gmail specifically
                }

                // Check if Gmail is installed
                if (gmailIntent.resolveActivity(packageManager) != null) {
                    startActivity(gmailIntent)
                } else {
                    // If Gmail is not installed, fall back to the default email client
                    startActivity(intent)
                }
            } catch (e: ActivityNotFoundException) {
                // Handle the exception if no email clients are available
                Toast.makeText(context, "No email client found.", Toast.LENGTH_SHORT).show()
            }
        } else {
            // No email client is available
            Toast.makeText(context, "No email client found.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isEmailClientAvailable(): Boolean {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
        }
        return requireContext().packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).isNotEmpty()
    }

    private fun showEmailErrorDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("")
            .setMessage("الرجاء التحقق من تكوين البريد الإلكتروني والمحاولة مرة أخرى.")
            .setPositiveButton("أوكي") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}
