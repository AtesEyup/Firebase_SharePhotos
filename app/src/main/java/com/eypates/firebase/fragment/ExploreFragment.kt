package com.eypates.firebase.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.eypates.firebase.adapter.DataAdapter
import com.eypates.firebase.databinding.FragmentExploreBinding
import com.eypates.firebase.fragment.vm.ExploreVM
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage

class ExploreFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage

    private lateinit var exploreVM: ExploreVM
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()

        exploreVM = ViewModelProvider(this)[ExploreVM::class.java]

    }

    private lateinit var layoutBnd: FragmentExploreBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layoutBnd = FragmentExploreBinding.inflate(layoutInflater).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        val layoutManager = LinearLayoutManager(activity)
        layoutBnd.exploreFRecyclerView.layoutManager = layoutManager

        val decorator = DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        layoutBnd.exploreFRecyclerView.addItemDecoration(decorator)

        exploreVM.dataDownload()

        lsData()
        return layoutBnd.root
    }

    private fun lsData() {

        exploreVM.lsData.observe(viewLifecycleOwner) { observe ->
            observe.let {
                val adExplore = DataAdapter(it)
                layoutBnd.exploreFRecyclerView.adapter = adExplore
            }
        }

        exploreVM.errorMessage.observe(viewLifecycleOwner) { observe ->
            observe.let {
                if (it != "") Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

    }
}