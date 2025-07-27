package com.sparrow.amp2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.sparrow.amp2.ui.navigation.ProductShowcaseNavigation
import com.sparrow.amp2.ui.theme.ProductShowcaseTheme
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        
        // Set Firebase Auth language to avoid locale warnings
        try {
            val firebaseAuth = FirebaseAuth.getInstance()
            val locale = Locale.getDefault()
            val languageCode = if (locale.language.isNotEmpty()) {
                locale.language
            } else {
                "en" // Default to English if locale is not available
            }
            firebaseAuth.setLanguageCode(languageCode)
        } catch (e: Exception) {
            // Fallback: set to English if there's any issue
            FirebaseAuth.getInstance().setLanguageCode("en")
        }
        
        enableEdgeToEdge()
        setContent {
            ProductShowcaseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    ProductShowcaseNavigation(navController = navController)
                }
            }
        }
    }
}