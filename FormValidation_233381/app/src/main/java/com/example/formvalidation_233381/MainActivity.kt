package com.example.formvalidation_233381

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.formvalidation_233381.databinding.ActivityMainBinding
import android.util.Patterns
import androidx.appcompat.app.AlertDialog
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.formvalidation_233381.ui.theme.FormValidation_233381Theme

class MainActivity : ComponentActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        val genderSpinner: Spinner = findViewById(R.id.genderSpinner)
        val genderOptions = resources.getStringArray(R.array.gender_options)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genderOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genderSpinner.adapter = adapter

        val defaultOption = "Select a genre"
        val optionsWithDefault = listOf(defaultOption) + genderOptions.toList()
        val adapterWithDefault = ArrayAdapter(this, android.R.layout.simple_spinner_item, optionsWithDefault.toTypedArray())
        adapterWithDefault.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genderSpinner.adapter = adapterWithDefault

        emailFocusListener()
        passwordFocusListener()
        phoneFocusListener()
        usernameFocusListener()



        binding.submitButton.setOnClickListener { submitForm() }
    }

    private fun submitForm() {
        binding.emailContainer.helperText = validEmail()
        binding.passwordContainer.helperText = validPassword()
        binding.phoneContainer.helperText = validPhone()
        binding.usernameContainer.helperText = validUsername()

        val validEmail = binding.emailContainer.helperText == null
        val validPassword = binding.passwordContainer.helperText == null
        val validPhone = binding.phoneContainer.helperText == null
        val validUsername = binding.usernameContainer.helperText == null
        val validGender = validGender()

        if (validEmail && validPassword && validPhone && validUsername && validGender)
            resetForm()
        else
            invalidForm()
    }

    private fun validGender(): Boolean {
        val selectedGenre = binding.genderSpinner.selectedItem as String
        return selectedGenre != "Select a genre"
    }

    private fun invalidForm() {
        var message = ""
        if (binding.usernameContainer.helperText != null)
            message += "\n\nUsername: " + binding.usernameContainer.helperText
        if(binding.emailContainer.helperText != null)
            message += "\n\nEmail: " + binding.emailContainer.helperText
        if(binding.passwordContainer.helperText != null)
            message += "\n\nPassword: " + binding.passwordContainer.helperText
        if(binding.phoneContainer.helperText != null)
            message += "\n\nPhone: " + binding.phoneContainer.helperText
        if (!validGender()) {
            message += "\n\nGender: Please select a gender"
        }

        AlertDialog.Builder(this)
            .setTitle("Invalid Form")
            .setMessage(message)
            .setPositiveButton("Okay") { _,_ ->
                // do nothing
            }
            .show()
    }

    private fun resetForm() {
        var message = "Email: " + binding.emailEditText.text
        message += "\nPassword: " + binding.passwordEditText.text
        message += "\nPhone: " + binding.phoneEditText.text
        message += "\nUsername: " + binding.usernameEditText.text
        message += "\nGender: " + binding.genderSpinner.selectedItem.toString()

        AlertDialog.Builder(this)
            .setTitle("Form submitted")
            .setMessage(message)
            .setPositiveButton("Okay") { _, _ ->

                binding.emailEditText.text = null
                binding.passwordEditText.text = null
                binding.phoneEditText.text = null
                binding.usernameEditText.text = null

                binding.usernameContainer.helperText = getString(R.string.required)
                binding.emailContainer.helperText = getString(R.string.required)
                binding.passwordContainer.helperText = getString(R.string.required)
                binding.phoneContainer.helperText = getString(R.string.required)

                binding.genderSpinner.setSelection(0)
            }
            .show()
    }

    private fun emailFocusListener() {
        binding.emailEditText.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.emailContainer.helperText = validEmail()
            }
        }
    }

    private fun usernameFocusListener() {
        binding.usernameEditText.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.usernameContainer.helperText = validUsername()
            }
        }
    }

    private fun validUsername(): String? {
        val usernameText = binding.usernameEditText.text.toString()
        if (usernameText.isBlank()) {
            return "Username is required"
        }
        return null
    }

    private fun validEmail(): String? {
        val emailText = binding.emailEditText.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            return "Invalid Email Address"
        }
        return null
    }

    private fun passwordFocusListener() {
        binding.passwordEditText.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.passwordContainer.helperText = validPassword()
            }
        }
    }

    private fun validPassword(): String? {
        val passwordText = binding.passwordEditText.text.toString()
        if (passwordText.length < 8) {
            return "Minimum 8 Character Password"
        }
        if (!passwordText.matches(".*[A-Z].*".toRegex())) {
            return "Must Contain 1 Upper-case Character"
        }
        if (!passwordText.matches(".*[a-z].*".toRegex())) {
            return "Must Contain 1 Lower-case Character"
        }
        if (!passwordText.matches(".*[@#\$%^&+=].*".toRegex())) {
            return "Must Contain 1 Special Character (@#\$%^&+=)"
        }

        return null
    }

    private fun phoneFocusListener() {
        binding.phoneEditText.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.phoneContainer.helperText = validPhone()
            }
        }
    }

    private fun validPhone(): String? {
        val phoneText = binding.phoneEditText.text.toString()
        if (!phoneText.matches(".*[0-9].*".toRegex())) {
            return "Must be all Digits"
        }
        if (phoneText.length != 10) {
            return "Must be 10 Digits"
        }
        return null
    }
}
