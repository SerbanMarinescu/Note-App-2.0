package com.example.newnotesapp.presentation.util

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navArgument
import com.example.newnotesapp.R
import com.example.newnotesapp.presentation.util.ui.theme.NewNotesAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class BiometricAuthenticatorActivity : FragmentActivity() {

    private val biometricManager by lazy {
        BiometricManager(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.biometric_activity_layout)

        val noteId = intent.getStringExtra("NoteId")
        val noteColor = intent.getIntExtra("NoteColor", 0)

        val customCanvasView: CustomCanvasView = findViewById(R.id.Note)
        customCanvasView.noteColor = noteColor

        lifecycleScope.launch {
            biometricManager.biometricResult.collectLatest { result ->
                when(result) {
                    is BiometricResult.AuthenticationError -> {
                        Toast.makeText(this@BiometricAuthenticatorActivity, result.error, Toast.LENGTH_SHORT).show()
                        setResult(RESULT_CANCELED)
                        finish()
                    }
                    BiometricResult.AuthenticationFailed -> Unit
                    BiometricResult.AuthenticationNotSet -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                                putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED, BIOMETRIC_STRONG)
                            }
                            this@BiometricAuthenticatorActivity.startActivity(enrollIntent)
                        }
                    }
                    BiometricResult.AuthenticationSuccess -> {
                        Toast.makeText(this@BiometricAuthenticatorActivity, "Access Granted!", Toast.LENGTH_SHORT).show()
                        val data = Intent().apply {
                            putExtra("AUTH_SUCCESS", true)
                            putExtra("NoteId", noteId)
                            putExtra("NoteColor", noteColor)
                        }
                        setResult(RESULT_OK, data)
                        finish()
                    }
                    BiometricResult.FeatureUnavailable -> {
                        Toast.makeText(this@BiometricAuthenticatorActivity, "Biometric Feature Not Available", Toast.LENGTH_SHORT).show()
                        setResult(RESULT_CANCELED)
                        finish()
                    }
                    BiometricResult.HardwareUnavailable -> {
                        Toast.makeText(this@BiometricAuthenticatorActivity, "Hardware Unavailable", Toast.LENGTH_SHORT).show()
                        setResult(RESULT_CANCELED)
                        finish()
                    }
                }
            }
        }
        biometricManager.showBiometricPrompt(
            "Verify Identity",
            "Check Fingerprint"
        )
    }
}
