package com.eypates.firebase.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.eypates.firebase.R
import com.eypates.firebase.databinding.ActivityDashboardBinding
import com.eypates.firebase.fragment.ExploreFragment
import com.eypates.firebase.fragment.PhotoFragment
import com.eypates.firebase.fragment.ProfileFragment

class DashboardActivity : AppCompatActivity() {
    private lateinit var layoutBnd: ActivityDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutBnd = ActivityDashboardBinding.inflate(layoutInflater).apply {
            lifecycleOwner = this@DashboardActivity
        }
        setContentView(layoutBnd.root)

        openFragment(ExploreFragment())

        layoutBnd.dashboardBtnExplore.setOnClickListener { openFragment(ExploreFragment()) }

        layoutBnd.dashboardBtnAddPhoto.setOnClickListener { openFragment(PhotoFragment()) }

        layoutBnd.dashboardBtnProfile.setOnClickListener { openFragment(ProfileFragment()) }

    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView, fragment)
        transaction.commit()
    }
}