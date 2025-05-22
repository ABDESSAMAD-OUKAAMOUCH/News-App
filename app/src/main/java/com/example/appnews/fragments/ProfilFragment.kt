package com.example.appnews.fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.appnews.HomeActivity
import com.example.appnews.Models.User
import com.example.appnews.R
import com.example.appnews.dataBase.NewsDatabase
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream

class ProfileFragment : Fragment() {

    private lateinit var nameEditText: EditText
    private lateinit var emailTextView: TextView
    private lateinit var photoImageView: CircleImageView
    private lateinit var saveButton: Button

    private var currentUser: User? = null
    private var savedImagePath: String? = null

    // Image Picker Launcher
    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            saveImageToInternalStorage(it)?.let { path ->
                savedImagePath = path
                photoImageView.setImageURI(Uri.fromFile(File(path)))
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profil, container, false)
        nameEditText = view.findViewById(R.id.editTextName)
        emailTextView = view.findViewById(R.id.textViewEmail)
        photoImageView = view.findViewById(R.id.imageViewPhoto)
        saveButton = view.findViewById(R.id.buttonSave)

        loadUserData()

        photoImageView.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

        saveButton.setOnClickListener {
            saveUpdatedUser()
        }

        return view
    }

    private fun loadUserData() {
        CoroutineScope(Dispatchers.IO).launch {
            val db = NewsDatabase.getDatabase(requireContext())
            val account = com.google.android.gms.auth.api.signin.GoogleSignIn.getLastSignedInAccount(requireContext())
            val userEmail = account?.email ?: return@launch

            currentUser = db.userDao().getUser(userEmail)
            Log.d("PROFILE", "Loaded user: $currentUser")

            currentUser?.let {
                withContext(Dispatchers.Main) {
                    nameEditText.setText(it.displayName)
                    emailTextView.text = it.email

                    val photoUrl = it.photoUrl
                    if (!photoUrl.isNullOrEmpty()) {
                        val isUrl = photoUrl.startsWith("http://") || photoUrl.startsWith("https://")
                        val imageSource = if (isUrl) photoUrl else File(photoUrl)

                        Glide.with(requireContext())
                            .load(imageSource)
                            .placeholder(R.drawable.profilepicture)
                            .error(R.drawable.profilepicture)
                            .into(photoImageView)
                    } else {
                        photoImageView.setImageResource(R.drawable.profilepicture)
                    }
                }
            }
        }
    }


    private fun saveUpdatedUser() {
        val updatedName = nameEditText.text.toString()
        CoroutineScope(Dispatchers.IO).launch {
            currentUser?.let {
                it.displayName = updatedName
                savedImagePath?.let { path -> it.photoUrl = path } // يستبدل الرابط بمسار محلي
                NewsDatabase.getDatabase(requireContext()).userDao().updateUser(it)

                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Profile updated", Toast.LENGTH_SHORT).show()
                    (activity as? HomeActivity)?.updateNavigationHeader()
                }
            }
        }
    }


    private fun saveImageToInternalStorage(uri: Uri): String? {
        return try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val fileName = "profile_${System.currentTimeMillis()}.jpg"
            val file = File(requireContext().filesDir, fileName)
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            outputStream.close()
            inputStream?.close()
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
