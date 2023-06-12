package com.example.e_waste.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.e_waste.model.Property
import com.example.e_waste.utils.Constants.APARTMENT
import com.example.e_waste.utils.Resource
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

class FirebaseViewModel: ViewModel() {
    private val _addProperty = MutableLiveData<Resource<String>>()
    val addProperty: LiveData<Resource<String>>
        get() = _addProperty

    private val _getProperty = MutableLiveData<Resource<List<Property>>>()
    val getProperty: LiveData<Resource<List<Property>>>
        get() = _getProperty

    private val _deleteProperty = MutableLiveData<Resource<String>>()
    val deleteProperty: LiveData<Resource<String>>
        get() = _deleteProperty

    private val _addVarieties = MutableLiveData<Resource<String>>()
    val addVarieties: LiveData<Resource<String>>
        get() = _addVarieties

    private val _getVarieties = MutableLiveData<Resource<List<Property>>>()
    val getVarieties: LiveData<Resource<List<Property>>>
        get() = _getVarieties

    private val _deleteVarieties = MutableLiveData<Resource<String>>()
    val deleteVarieties: LiveData<Resource<String>>
        get() = _deleteVarieties
    
    // Fav
    private val _addFav = MutableLiveData<Resource<String>>()
    val addFav: LiveData<Resource<String>>
        get() = _addFav

    private val _getFav = MutableLiveData<Resource<List<Property>>>()
    val getFav: LiveData<Resource<List<Property>>>
        get() = _getFav

    private val _deleteFav = MutableLiveData<Resource<String>>()
    val deleteFav: LiveData<Resource<String>>
        get() = _deleteFav

    private val auth = Firebase.auth
    private val allPropertiesCollectionRef = Firebase.firestore.collection("allProperties")
    private val varietiesCollectionRef = Firebase.firestore.collection("varieties")

    private val favCollectionRef = Firebase.firestore.collection("users")
        .document(auth.currentUser?.uid!!).collection("fav")


    init {
        getVarieties()
        getProperty(APARTMENT)
        getFav()
    }


    fun addProperty(property: HashMap<String, String>){
        _addProperty.value = Resource.Loading()
        allPropertiesCollectionRef.add(property)
            .addOnSuccessListener {
                _addProperty.value = Resource.Success("Successfully added data!")
            }
            .addOnFailureListener{
                _addProperty.value = Resource.Error(message = it.message.toString())
            }
    }

    fun getProperty(type: String){
        _getProperty.value = Resource.Loading()
        allPropertiesCollectionRef
            .whereEqualTo("type",type).addSnapshotListener { querySnapshot, error ->
                error?.let {
                    _getProperty.value = Resource.Error(message = it.message.toString())
                }
                querySnapshot?.let {
                    _getProperty.value = Resource.Success(it.toObjects())
                }
            }
    }

    fun deleteProperty(property: Property){
        _deleteProperty.value = Resource.Loading()
        allPropertiesCollectionRef
            .whereEqualTo("url", property.url)
            .whereEqualTo("name", property.name)
            .whereEqualTo("description", property.description)
            .get()
            .addOnSuccessListener {
                if (it.documents.isNotEmpty()){
                    it.forEach { document->
                        allPropertiesCollectionRef.document(document.id).delete()
                            .addOnSuccessListener {
                                _deleteProperty.value = Resource.Success("Removed from Property!")
                            }
                            .addOnFailureListener { error->
                                _deleteProperty.value = Resource.Error(message = error.message.toString())
                            }
                    }
                }
            }
    }

    fun addVarieties(property: HashMap<String, String>){
        _addVarieties.value = Resource.Loading()
        varietiesCollectionRef.add(property)
            .addOnSuccessListener {
                _addVarieties.value = Resource.Success("Successfully added data!")
            }
            .addOnFailureListener{
                _addVarieties.value = Resource.Error(message = it.message.toString())
            }
    }

    fun getVarieties(){
        _getVarieties.value = Resource.Loading()
        varietiesCollectionRef.addSnapshotListener { querySnapshot, error ->
            error?.let {
                _getVarieties.value = Resource.Error(message = it.message.toString())
            }
            querySnapshot?.let {
                _getVarieties.value = Resource.Success(it.toObjects())
            }
        }
    }

    fun deleteVarieties(property: Property){
        _deleteVarieties.value = Resource.Loading()
        varietiesCollectionRef
            .whereEqualTo("url", property.url)
            .whereEqualTo("name", property.name)
            .whereEqualTo("description", property.description)
            .get()
            .addOnSuccessListener {
                if (it.documents.isNotEmpty()){
                    it.forEach { document->
                        varietiesCollectionRef.document(document.id).delete()
                            .addOnSuccessListener {
                                _deleteVarieties.value = Resource.Success("Removed from varieties!")
                            }
                            .addOnFailureListener { error->
                                _deleteVarieties.value = Resource.Error(message = error.message.toString())
                            }
                    }
                }
            }
    }


    fun addFav(property: HashMap<String, String>){
        _addFav.value = Resource.Loading()
        favCollectionRef.add(property)
            .addOnSuccessListener {
                _addFav.value = Resource.Success("Added to fav!")
            }
            .addOnFailureListener{
                _addFav.value = Resource.Error(message = it.message.toString())
            }
    }

    fun getFav(){
        _getFav.value = Resource.Loading()
        favCollectionRef.addSnapshotListener { querySnapshot, error ->
            error?.let {
                _getFav.value = Resource.Error(message = it.message.toString())
            }
            querySnapshot?.let {
                _getFav.value = Resource.Success(it.toObjects())
            }
        }
    }

    fun deleteFav(property: Property){
        _deleteFav.value = Resource.Loading()
        favCollectionRef
            .whereEqualTo("url", property.url)
            .whereEqualTo("name", property.name)
            .whereEqualTo("description", property.description)
            .get()
            .addOnSuccessListener {
                if (it.documents.isNotEmpty()){
                    it.forEach { document->
                        favCollectionRef.document(document.id).delete()
                            .addOnSuccessListener {
                                _deleteFav.value = Resource.Success("Removed from fav!")
                            }
                            .addOnFailureListener { error->
                                _deleteFav.value = Resource.Error(message = error.message.toString())
                            }
                    }
                }
            }
    }
}