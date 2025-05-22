package com.example.appnews.fragments

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.appnews.R

class AboutUsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_about_us, container, false)

        val aboutTextView = view.findViewById<TextView>(R.id.aboutTextView)
        aboutTextView.text = getAboutUsText()

        return view
    }

    private fun getAboutUsText(): String {
        return """
            Welcome to AppNews!

            This application was developed as part of an advanced Android development project.

            🔹 Features implemented:
            - Google Sign-In Integration
            - Navigation Drawer with custom header
            - Fragments with FrameLayout
            - Room Database for local storage
            - Retrofit for REST API calls
            - TabLayout for organized views
            - Profile editing and image uploading
            - Saved image handling using internal storage
            - Glide for image loading
            - Kotlin Coroutines for async tasks

            📫 Contact us:
            Email: support@appnews.com
            Website: www.appnews.com
            
            Thank you for using our app!
        """.trimIndent()
    }
}
