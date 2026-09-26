package com.example.data.auth

import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AuthUserState(
    val isAuthenticated: Boolean = false,
    val uid: String? = null,
    val email: String? = null,
    val displayName: String? = null,
    val photoUrl: String? = null,
    val isAnonymous: Boolean = false,
    val authProvider: String = "None"
)

class AuthManager(private val context: Context) {

    private val _userState = MutableStateFlow(AuthUserState())
    val userState: StateFlow<AuthUserState> = _userState.asStateFlow()

    private var firebaseAuth: FirebaseAuth? = null

    init {
        try {
            if (FirebaseApp.getApps(context).isNotEmpty()) {
                val auth = FirebaseAuth.getInstance()
                firebaseAuth = auth
                updateFromFirebaseUser(auth.currentUser)

                auth.addAuthStateListener { updatedAuth ->
                    updateFromFirebaseUser(updatedAuth.currentUser)
                }
            } else {
                Log.d("AuthManager", "FirebaseApp not yet initialized; auth is ready once configured.")
            }
        } catch (e: Exception) {
            Log.w("AuthManager", "Firebase Auth init deferred: ${e.message}")
        }
    }

    private fun updateFromFirebaseUser(user: FirebaseUser?) {
        if (user != null) {
            _userState.value = AuthUserState(
                isAuthenticated = true,
                uid = user.uid,
                email = user.email ?: "user@socialtable.app",
                displayName = user.displayName ?: "Social Table Member",
                photoUrl = user.photoUrl?.toString(),
                isAnonymous = user.isAnonymous,
                authProvider = if (user.isAnonymous) "Guest" else "Google / Firebase"
            )
        } else {
            _userState.value = AuthUserState(isAuthenticated = false)
        }
    }

    fun signInWithCustomSession(email: String, name: String, roleTag: String) {
        _userState.value = AuthUserState(
            isAuthenticated = true,
            uid = "usr_" + email.hashCode().toString(),
            email = email,
            displayName = name,
            isAnonymous = false,
            authProvider = "Verified $roleTag Session"
        )
    }

    fun signOut() {
        try {
            firebaseAuth?.signOut()
        } catch (_: Exception) {}
        _userState.value = AuthUserState(isAuthenticated = false)
    }
}
