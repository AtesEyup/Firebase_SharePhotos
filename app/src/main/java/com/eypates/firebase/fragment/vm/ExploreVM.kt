package com.eypates.firebase.fragment.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eypates.firebase.model.DataModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ExploreVM : ViewModel() {

    val database = FirebaseFirestore.getInstance()
    val lsData = MutableLiveData<List<DataModel>>()
    val errorMessage = MutableLiveData<String>()

    fun dataDownload() {

        database.collection("Post").orderBy("dateTime", Query.Direction.DESCENDING).addSnapshotListener { value, exception ->
            if (exception != null) {
                errorMessage.value = exception.localizedMessage
                return@addSnapshotListener
            }

            if (value != null) {
                if (!value.isEmpty) {

                    val list = value.documents.map { x -> DataModel(url = x.get("url") as String, email = x.get("email") as String, comment = x.get("comment") as String, dateTime = (x.get("dateTime") as Timestamp).toDate().toLocaleString()) }
                    lsData.value = list
                }
            }
        }
    }
}