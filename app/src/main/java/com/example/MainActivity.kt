package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.data.auth.AuthManager
import com.example.data.firestore.FirestoreManager
import com.example.data.local.SocialDiningDatabase
import com.example.data.repository.SocialDiningRepository
import com.example.ui.screens.MainAppScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.SocialDiningViewModel

class SocialDiningViewModelFactory(
    private val repository: SocialDiningRepository,
    private val authManager: AuthManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SocialDiningViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SocialDiningViewModel(repository, authManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val authManager = AuthManager(applicationContext)
        val firestoreManager = FirestoreManager(applicationContext)
        val database = SocialDiningDatabase.getDatabase(applicationContext, lifecycleScope)
        val repository = SocialDiningRepository(database.dao(), firestoreManager)
        val factory = SocialDiningViewModelFactory(repository, authManager)
        val viewModel = ViewModelProvider(this, factory)[SocialDiningViewModel::class.java]

        setContent {
            MyApplicationTheme {
                MainAppScreen(viewModel = viewModel)
            }
        }
    }
}
