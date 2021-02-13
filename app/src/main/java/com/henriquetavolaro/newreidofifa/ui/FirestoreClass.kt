package com.henriquetavolaro.newreidofifa.ui

import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.henriquetavolaro.newreidofifa.SignInActivity
import java.security.AccessController.getContext

class FirestoreClass {

    private val firestore = FirebaseFirestore.getInstance()

    fun registerUser(activity: SignUpActivity, userInfo: User){
        firestore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegisteredSuccess()
            }
    }

    fun signInUser(activity: SignInActivity){
        firestore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                val loggedUser = document.toObject(User::class.java)
                activity.signInSuccess(loggedUser!!)
            }
            .addOnFailureListener{e->
                Log.e("TAG", "Error signing in", e)

            }
    }

    fun getCurrentUserID() : String {

        var currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if(currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

}