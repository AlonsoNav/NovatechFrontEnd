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
import com.app.novatech.model.User
import com.app.novatech.util.LoginController
import com.google.gson.Gson
import com.google.gson.JsonParser

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
            val pupOk = Dialog(this)
            val bindingPupOk = PopupOkBinding.inflate(layoutInflater)
            pupOk.setContentView(bindingPupOk.root)
            pupOk.setCancelable(true)
            pupOk.window?.setBackgroundDrawableResource(android.R.color.transparent)
            pupOk.window?.attributes?.windowAnimations = R.style.CustomDialogAnimation
            val okBtn = bindingPupOk.pupOkBtn
            try{
                LoginController.loginAttempt(binding.loginEmail.text.toString(),
                    binding.loginPassword.text.toString()) {
                    val jsonObject = JsonParser().parse(it.body?.string()).asJsonObject
                    Log.d("colaborador", jsonObject.asJsonObject.toString())
                    if(it.isSuccessful){
                        val user = Gson().fromJson(jsonObject.getAsJsonObject("colaboradorFinal")
                            .toString(), User::class.java)
                        val intent = Intent(this, Menu::class.java)
                        intent.putExtra("user", user)
                        startActivity(intent)
                    }else{
                        runOnUiThread {
                            bindingPupOk.pupYesNoDescription.text = jsonObject.get("message").asString
                            okBtn.setOnClickListener{
                                pupOk.dismiss()
                            }
                            pupOk.show()
                        }
                    }
                }
            }catch (e: Exception){
                runOnUiThread {
                    bindingPupOk.pupYesNoDescription.text = getString(R.string.failed_server)
                    okBtn.setOnClickListener{
                        pupOk.dismiss()
                    }
                    pupOk.show()
                }
            }
        }
    }
}