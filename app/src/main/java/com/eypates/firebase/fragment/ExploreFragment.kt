package com.eypates.firebase.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.eypates.firebase.adapter.DataAdapter
import com.eypates.firebase.databinding.FragmentExploreBinding
import com.eypates.firebase.model.DataModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage

class ExploreFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseFirestore

    var dataList = mutableListOf<DataModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        database = FirebaseFirestore.getInstance()

    }

    private lateinit var layoutBnd: FragmentExploreBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layoutBnd = FragmentExploreBinding.inflate(layoutInflater).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        val layoutManager = LinearLayoutManager(activity)
        layoutBnd.exploreFRecyclerView.layoutManager = layoutManager

        dataDownload()

        return layoutBnd.root
    }

    @Suppress("UNREACHABLE_CODE")
    private fun dataDownload() {

        database.collection("Post").orderBy("dateTime", Query.Direction.DESCENDING).addSnapshotListener { value, exception ->
            if (exception != null) {
                Toast.makeText(activity, exception.localizedMessage, Toast.LENGTH_LONG).show()
                return@addSnapshotListener
            }

            dataList.clear()

            if (value != null) {
                if (!value.isEmpty) {

                    val list = value.documents.map { x -> DataModel(url = x.get("url") as String, email = x.get("email") as String, comment = x.get("comment") as String, dateTime = x.get("dateTime") as Timestamp) }

                    dataList.addAll(list)

                    val adapter = DataAdapter(dataList)
                    layoutBnd.exploreFRecyclerView.adapter = adapter

                }
            }
        }
    }
}