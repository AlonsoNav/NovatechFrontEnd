package com.app.novatech.ui

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.novatech.R
import com.app.novatech.databinding.ActivityLoginBinding
import com.app.novatech.databinding.PopupOkBinding
import com.app.novatech.util.LoginController

class Login : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        binding.loginBtn.setOnClickListener{
            LoginController.loginAttempt(binding.loginEmail.text.toString(),
                binding.loginPassword.text.toString(), this) {
                if(it.isSuccessful){
                    Log.i("Json de colaborador", it.body?.string().toString());
                    val intent = Intent(this, Menu::class.java)
                    startActivity(intent)
                }else{
                    runOnUiThread {
                        val pupOk = Dialog(this)
                        val bindingPupOk = PopupOkBinding.inflate(layoutInflater)
                        pupOk.setContentView(bindingPupOk.root)
                        pupOk.setCancelable(true)
                        pupOk.window?.setBackgroundDrawableResource(android.R.color.transparent)
                        pupOk.window?.attributes?.windowAnimations = R.style.CustomDialogAnimation
                        val okBtn = bindingPupOk.pupOkBtn
                        bindingPupOk.pupYesNoDescription.text = getString(R.string.popup_ok_login)
                        okBtn.setOnClickListener{
                            pupOk.dismiss()
                        }
                        pupOk.show()
                    }
                }
            }
        }
    }
}