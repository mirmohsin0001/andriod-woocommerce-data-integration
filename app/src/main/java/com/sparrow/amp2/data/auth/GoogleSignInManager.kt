package com.sparrow.amp2.data.auth

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.sparrow.amp2.R

class GoogleSignInManager(private val context: Context) {
    
    private var googleSignInClient: GoogleSignInClient
    private var onSignInResult: ((Result<GoogleSignInAccount>) -> Unit)? = null
    
    init {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        
        googleSignInClient = GoogleSignIn.getClient(context, gso)
    }
    
    fun signIn(
        activity: AppCompatActivity,
        onResult: (Result<GoogleSignInAccount>) -> Unit
    ) {
        onSignInResult = onResult
        
        val signInIntent = googleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    
    fun handleSignInResult(data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        handleSignInResult(task)
    }
    
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            onSignInResult?.invoke(Result.success(account))
        } catch (e: ApiException) {
            onSignInResult?.invoke(Result.failure(e))
        }
    }
    
    fun signOut(onComplete: () -> Unit) {
        googleSignInClient.signOut().addOnCompleteListener {
            onComplete()
        }
    }
    
    fun revokeAccess(onComplete: () -> Unit) {
        googleSignInClient.revokeAccess().addOnCompleteListener {
            onComplete()
        }
    }
    
    companion object {
        const val RC_SIGN_IN = 9001
    }
}
