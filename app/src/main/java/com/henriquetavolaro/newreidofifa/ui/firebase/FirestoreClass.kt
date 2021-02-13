package com.henriquetavolaro.newreidofifa.ui.firebase

import android.app.Activity
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.henriquetavolaro.newreidofifa.ui.Constants
import com.henriquetavolaro.newreidofifa.ui.activities.MainActivity
import com.henriquetavolaro.newreidofifa.ui.activities.SignInActivity
import com.henriquetavolaro.newreidofifa.ui.activities.SignUpActivity
import com.henriquetavolaro.newreidofifa.ui.models.User
import com.henriquetavolaro.newreidofifa.ui.profile.ProfileFragment

class FirestoreClass {

    private val firestore = FirebaseFirestore.getInstance()

    fun registerUser(activity: SignUpActivity, userInfo: User) {
        firestore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegisteredSuccess()
            }
    }

    fun loadUserDataOnProfile(fragment: Fragment) {
        firestore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                val loggedUser = document.toObject(User::class.java)

                when(fragment) {
                    is ProfileFragment -> {
                        fragment.setUserDataInUI(loggedUser!!)
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("TAG", "Error ssssss", e)

            }
    }

    fun loadUserData(activity: Activity) {
        firestore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                val loggedUser = document.toObject(User::class.java)

                when (activity) {
                    is SignInActivity -> {
                        activity.signInSuccess(loggedUser!!)
                    }
                    is MainActivity -> {
                        activity.updateNavigationUserDetails(loggedUser!!)
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("TAG", "Error signing in", e)

            }
    }

    fun getCurrentUserID(): String {

        var currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

}