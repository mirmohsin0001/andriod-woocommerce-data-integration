package com.sparrow.amp2.data.auth

import android.app.Activity
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.*
import com.google.firebase.FirebaseException
import com.sparrow.amp2.ui.profile.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


interface AuthRepository {
    val currentUser: Flow<User?>
    val isLoggedIn: Flow<Boolean>
    
    suspend fun signInWithGoogle(idToken: String): Result<User>
    suspend fun signInWithPhoneNumber(phoneNumber: String, activity: Activity): Result<String>
    suspend fun verifyPhoneNumberWithCode(verificationId: String, code: String): Result<User>
    suspend fun signOut(): Result<Unit>
    fun getCurrentUser(): User?
}

class AuthRepositoryImpl : AuthRepository {
    
    private val firebaseAuth = FirebaseAuth.getInstance()
    
    private val _currentUser = MutableStateFlow<User?>(null)
    override val currentUser: Flow<User?> = _currentUser.asStateFlow()
    
    private val _isLoggedIn = MutableStateFlow(false)
    override val isLoggedIn: Flow<Boolean> = _isLoggedIn.asStateFlow()
    
    private var storedVerificationId: String? = null
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null
    
    init {
        // Set language code to prevent locale warnings
        try {
            firebaseAuth.setLanguageCode("en")
        } catch (e: Exception) {
            // Ignore if setting language fails
        }
        
        // Listen to Firebase Auth state changes
        firebaseAuth.addAuthStateListener { auth ->
            val firebaseUser = auth.currentUser
            if (firebaseUser != null) {
                _currentUser.value = firebaseUser.toUser()
                _isLoggedIn.value = true
            } else {
                _currentUser.value = null
                _isLoggedIn.value = false
            }
        }
    }
    
    override suspend fun signInWithGoogle(idToken: String): Result<User> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = firebaseAuth.signInWithCredential(credential).await()
            val firebaseUser = result.user ?: throw Exception("Authentication failed")
            
            val user = firebaseUser.toUser()
            _currentUser.value = user
            _isLoggedIn.value = true
            
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun signInWithPhoneNumber(phoneNumber: String, activity: Activity): Result<String> {
        return try {
            kotlinx.coroutines.suspendCancellableCoroutine { continuation ->
                val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        // Auto-verification completed, sign in the user
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                val result = firebaseAuth.signInWithCredential(credential).await()
                                val firebaseUser = result.user ?: throw Exception("Authentication failed")
                                val user = firebaseUser.toUser()
                                _currentUser.value = user
                                _isLoggedIn.value = true
                                continuation.resume("Auto-verified and signed in") { }
                            } catch (e: Exception) {
                                continuation.resume(Result.failure<String>(e).getOrThrow()) { }
                            }
                        }
                    }
                    
                    override fun onVerificationFailed(e: FirebaseException) {
                        continuation.resume(Result.failure<String>(e).getOrThrow()) { }
                    }
                    
                    override fun onCodeSent(
                        verificationId: String,
                        token: PhoneAuthProvider.ForceResendingToken
                    ) {
                        storedVerificationId = verificationId
                        resendToken = token
                        continuation.resume("Verification code sent") { }
                    }
                }
                
                val options = PhoneAuthOptions.newBuilder(firebaseAuth)
                    .setPhoneNumber(phoneNumber)
                    .setTimeout(60L, java.util.concurrent.TimeUnit.SECONDS)
                    .setActivity(activity)
                    .setCallbacks(callbacks)
                    .build()
                
                PhoneAuthProvider.verifyPhoneNumber(options)
            }
            Result.success("Verification code sent")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun verifyPhoneNumberWithCode(verificationId: String, code: String): Result<User> {
        return try {
            val credential = PhoneAuthProvider.getCredential(verificationId, code)
            val result = firebaseAuth.signInWithCredential(credential).await()
            val firebaseUser = result.user ?: throw Exception("Authentication failed")
            
            val user = firebaseUser.toUser()
            _currentUser.value = user
            _isLoggedIn.value = true
            
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun signOut(): Result<Unit> {
        return try {
            firebaseAuth.signOut()
            _currentUser.value = null
            _isLoggedIn.value = false
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override fun getCurrentUser(): User? {
        return firebaseAuth.currentUser?.toUser()
    }
    
    fun getStoredVerificationId(): String? = storedVerificationId
    
    private fun FirebaseUser.toUser(): User {
        return User(
            id = uid,
            name = displayName ?: "User",
            email = email,
            phoneNumber = phoneNumber,
            photoUrl = photoUrl?.toString(),
            isEmailVerified = isEmailVerified,
            isPhoneVerified = phoneNumber != null
        )
    }
}
