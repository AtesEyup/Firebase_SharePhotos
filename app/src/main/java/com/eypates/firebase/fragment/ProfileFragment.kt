package com.eypates.firebase.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.eypates.firebase.databinding.FragmentProfileBinding
import com.eypates.firebase.ui.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private lateinit var layoutBnd: FragmentProfileBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layoutBnd = FragmentProfileBinding.inflate(layoutInflater).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        val auth = FirebaseAuth.getInstance()

        layoutBnd.profileFLblEmail.text = auth.currentUser!!.email

        layoutBnd.profileFLblLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
        }

        return layoutBnd.root
    }
}