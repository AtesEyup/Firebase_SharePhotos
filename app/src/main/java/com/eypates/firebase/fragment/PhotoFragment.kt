package com.eypates.firebase.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.eypates.firebase.databinding.FragmentPhotoBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class PhotoFragment : Fragment() {

    var imgUri: Uri? = null
    var selectedImage: Bitmap? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        database = FirebaseFirestore.getInstance()

    }

    private var READ_STORAGE_REQUEST_CODE = 1
    private var MEDIA_REQUEST_CODE = 2

    private lateinit var layoutBnd: FragmentPhotoBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        layoutBnd = FragmentPhotoBinding.inflate(layoutInflater).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        layoutBnd.photoFBtnSelectPhoto.setOnClickListener {

            if (Build.VERSION.SDK_INT >= 33) {
                if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED)
                    ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_MEDIA_IMAGES), READ_STORAGE_REQUEST_CODE)
                else
                    startActivityForResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), MEDIA_REQUEST_CODE)
            } else {
                if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), READ_STORAGE_REQUEST_CODE)
                else
                    startActivityForResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), MEDIA_REQUEST_CODE)
            }
        }

        layoutBnd.photoFBtnAgain.setOnClickListener {

            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), READ_STORAGE_REQUEST_CODE)
            else
                startActivityForResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), MEDIA_REQUEST_CODE)
        }

        layoutBnd.photoFBtnShare.setOnClickListener {

            val imageUUID = UUID.randomUUID()

            val imageReferance = storage.reference.child("images").child("${imageUUID}.jpg")

            imageReferance.putFile(imgUri!!)
                .addOnSuccessListener {
                    Toast.makeText(activity, "Fotoğraf Başarılı Bir Şekilde Yüklendi", Toast.LENGTH_LONG).show()
                    layoutBnd.photoFLayoutMain.visibility = View.VISIBLE
                    layoutBnd.photoFProgressBar.visibility = View.GONE
                    layoutBnd.photoFLayoutSecond.visibility = View.GONE

                    // resmi db ye kayıt etmek
                    imageReferance.downloadUrl
                        .addOnSuccessListener {
                            val url = it.toString()
                            val email = auth.currentUser!!.email.toString()
                            val comment = layoutBnd.photoFTxtDescription.text.toString()
                            val dateTime = Timestamp.now()

                            val postHashMap = hashMapOf<String, Any>()
                            postHashMap["url"] = url
                            postHashMap["email"] = email
                            postHashMap["comment"] = comment
                            postHashMap["dateTime"] = dateTime

                            database.collection("Post").add(postHashMap)
                                .addOnSuccessListener {
                                    Toast.makeText(activity, "Fotoğraf Db'ye Yüklendi", Toast.LENGTH_LONG).show()
//                                    findNavController().navigate(R.id.action_photoFragment_to_exploreFragment)
                                }
                        }

                }
                .addOnProgressListener {
                    layoutBnd.photoFProgressBar.visibility = View.VISIBLE
                }
                .addOnFailureListener {
                    Toast.makeText(activity, it.localizedMessage, Toast.LENGTH_LONG).show()
                    layoutBnd.photoFProgressBar.visibility = View.GONE
                }

        }

        return layoutBnd.root

    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == READ_STORAGE_REQUEST_CODE) {
            if (grantResults.isNotEmpty()) {

                if (Build.VERSION.SDK_INT >= 33) {
                    if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED)
                        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_MEDIA_IMAGES), READ_STORAGE_REQUEST_CODE)
                    else
                        startActivityForResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), MEDIA_REQUEST_CODE)
                } else {
                    if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), READ_STORAGE_REQUEST_CODE)
                    else
                        startActivityForResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), MEDIA_REQUEST_CODE)
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MEDIA_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {

            imgUri = data.data

            if (imgUri != null) {
                if (Build.VERSION.SDK_INT >= 28) {

                    val source = ImageDecoder.createSource(activity?.contentResolver!!, imgUri!!)
                    selectedImage = ImageDecoder.decodeBitmap(source)
                    layoutBnd.photoFImgPhoto.setImageBitmap(selectedImage)

                } else {
                    selectedImage = MediaStore.Images.Media.getBitmap(activity?.contentResolver, imgUri)
                    layoutBnd.photoFImgPhoto.setImageBitmap(selectedImage)
                }

                layoutBnd.photoFLayoutMain.visibility = View.GONE
                layoutBnd.photoFLayoutSecond.visibility = View.VISIBLE

            }
        }
    }
}