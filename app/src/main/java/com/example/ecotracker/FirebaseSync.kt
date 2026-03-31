package com.example.ecotracker

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseSync {

    fun isAvailable(context: Context): Boolean {
        return FirebaseApp.initializeApp(context) != null
    }

    fun registerUser(
        context: Context,
        name: String,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (!isAvailable(context)) {
            onError(context.getString(R.string.firebase_not_configured))
            return
        }

        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid
                if (uid == null) {
                    onError("Failed to resolve user id.")
                    return@addOnSuccessListener
                }
                saveUserProfile(context, uid, name, email)
                onSuccess()
            }
            .addOnFailureListener { ex ->
                onError(mapAuthError(context, ex))
            }
    }

    fun signInUser(
        context: Context,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (!isAvailable(context)) {
            onError(context.getString(R.string.firebase_not_configured))
            return
        }

        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                hydrateLearningProgress(context) {
                    onSuccess()
                }
            }
            .addOnFailureListener { ex ->
                onError(mapAuthError(context, ex))
            }
    }

    fun sendPasswordReset(
        context: Context,
        email: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (!isAvailable(context)) {
            onError(context.getString(R.string.firebase_not_configured))
            return
        }

        FirebaseAuth.getInstance()
            .sendPasswordResetEmail(email)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { ex -> onError(mapAuthError(context, ex)) }
    }

    fun hasAuthenticatedUser(context: Context): Boolean {
        if (!isAvailable(context)) return false
        return FirebaseAuth.getInstance().currentUser != null
    }

    fun signOut() {
        FirebaseAuth.getInstance().signOut()
    }

    fun saveOnboardingAnswers(
        context: Context,
        answers: OnboardingAnswers,
        payloadJson: String,
        completed: Boolean
    ) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        if (!isAvailable(context)) return

        FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .collection("profile")
            .document("onboarding")
            .set(
                buildOnboardingMap(uid, answers, payloadJson, completed)
            )
            .addOnSuccessListener { HomeCacheStore.invalidate(context) }
    }

    fun saveOnboardingPayload(context: Context, payloadJson: String) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        if (!isAvailable(context)) return

        FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .collection("profile")
            .document("onboarding")
            .set(
                mapOf(
                    "userId" to uid,
                    "payload" to payloadJson,
                    "onboardingCompleted" to false,
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            )
            .addOnSuccessListener { HomeCacheStore.invalidate(context) }
    }

    fun syncLearningProgress(context: Context) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        if (!isAvailable(context)) return

        val completedIds = LearningProgressStore.getCompletedIds(context).toList()
        FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .collection("profile")
            .document("learning")
            .set(
                mapOf(
                    "completedLessonIds" to completedIds,
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            )
    }

    fun fetchHomeData(
        context: Context,
        forceRefresh: Boolean = false,
        onResult: (HomeData) -> Unit
    ) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid.isNullOrBlank() || !isAvailable(context)) {
            onResult(HomeRepository.defaultHomeData())
            return
        }

        if (!forceRefresh) {
            val cached = HomeCacheStore.read(context, uid)
            if (cached != null && !HomeCacheStore.isExpired(cached.cachedAtMs)) {
                onResult(cached)
                return
            }
        }

        val accountRef = FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .collection("profile")
            .document("account")

        val onboardingRef = FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .collection("profile")
            .document("onboarding")

        accountRef.get().addOnSuccessListener { accountSnap ->
            onboardingRef.get().addOnSuccessListener { onboardingSnap ->
                val firstNameFromAccount = accountSnap.getString("name")
                    ?.trim()
                    ?.split(" ")
                    ?.firstOrNull()
                    ?.takeIf { it.isNotBlank() }
                val firstName = firstNameFromAccount ?: onboardingSnap.getString("firstName").orEmpty()
                val onboardingCompleted = onboardingSnap.getBoolean("onboardingCompleted") ?: false

                val data = HomeRepository.defaultHomeData(
                    firstName = firstName,
                    onboardingCompleted = onboardingCompleted
                )
                HomeCacheStore.write(context, uid, data)
                onResult(data)
            }.addOnFailureListener {
                onResult(HomeRepository.defaultHomeData())
            }
        }.addOnFailureListener {
            onResult(HomeRepository.defaultHomeData())
        }
    }

    private fun hydrateLearningProgress(context: Context, onDone: () -> Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: run {
            onDone()
            return
        }
        if (!isAvailable(context)) {
            onDone()
            return
        }

        FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .collection("profile")
            .document("learning")
            .get()
            .addOnSuccessListener { snap ->
                val ids = snap.get("completedLessonIds") as? List<*>
                val parsed = ids?.filterIsInstance<String>()?.toSet().orEmpty()
                LearningProgressStore.setCompletedIds(context, parsed)
                onDone()
            }
            .addOnFailureListener { onDone() }
    }

    private fun saveUserProfile(context: Context, uid: String, name: String, email: String) {
        FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .collection("profile")
            .document("account")
            .set(
                mapOf(
                    "name" to name,
                    "email" to email,
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            )
            .addOnSuccessListener { HomeCacheStore.invalidate(context) }
    }

    private fun buildOnboardingMap(
        uid: String,
        answers: OnboardingAnswers,
        payloadJson: String,
        completed: Boolean
    ): Map<String, Any?> {
        return mapOf(
            "userId" to uid,
            "firstName" to answers.firstName,
            "lastName" to answers.lastName,
            "country" to answers.country,
            "drivesCar" to answers.drivesCar,
            "drivingFrequency" to answers.drivingFrequency.orEmpty(),
            "carType" to answers.carType.orEmpty(),
            "diet" to answers.diet,
            "buildingType" to answers.buildingType,
            "bedrooms" to answers.bedrooms,
            "peopleExcludingSelf" to answers.peopleExcludingSelf,
            "goals" to answers.goals,
            "onboardingCompleted" to completed,
            "payload" to payloadJson,
            "updatedAt" to FieldValue.serverTimestamp()
        )
    }

    private fun mapAuthError(context: Context, throwable: Throwable): String {
        return when (throwable) {
            is FirebaseAuthUserCollisionException -> context.getString(R.string.firebase_error_user_exists)
            is FirebaseAuthInvalidCredentialsException -> context.getString(R.string.firebase_error_invalid_credentials)
            is FirebaseAuthInvalidUserException -> context.getString(R.string.firebase_error_user_not_found)
            is FirebaseAuthException -> {
                when (throwable.errorCode) {
                    "ERROR_WEAK_PASSWORD" -> context.getString(R.string.firebase_error_weak_password)
                    "ERROR_INVALID_EMAIL" -> context.getString(R.string.error_invalid_email)
                    "ERROR_WRONG_PASSWORD" -> context.getString(R.string.firebase_error_wrong_password)
                    "ERROR_USER_NOT_FOUND" -> context.getString(R.string.firebase_error_user_not_found)
                    else -> throwable.localizedMessage ?: context.getString(R.string.firebase_error_generic)
                }
            }
            else -> throwable.localizedMessage ?: context.getString(R.string.firebase_error_generic)
        }
    }
}
