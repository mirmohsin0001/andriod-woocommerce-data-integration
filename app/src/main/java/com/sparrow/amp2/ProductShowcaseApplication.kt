package com.sparrow.amp2

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale

class ProductShowcaseApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        
        // Set Firebase Auth language explicitly to prevent locale warnings
        try {
            val firebaseAuth = FirebaseAuth.getInstance()
            val locale = Locale.getDefault()
            val languageCode = when {
                locale.language.isNotEmpty() -> locale.language
                else -> "en" // Default to English
            }
            firebaseAuth.setLanguageCode(languageCode)
        } catch (e: Exception) {
            // Fallback to English
            FirebaseAuth.getInstance().setLanguageCode("en")
        }
    }
}
