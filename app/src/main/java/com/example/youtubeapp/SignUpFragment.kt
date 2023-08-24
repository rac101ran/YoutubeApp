package com.example.youtubeapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.youtubeapp.databinding.FragmentSignUpBinding
import com.example.youtubeapp.utils.ProgressBarLoading
import com.example.youtubeapp.viewmodel.VideoViewModel
import kotlinx.coroutines.launch


class SignUpFragment : Fragment() {

    private lateinit var binding : FragmentSignUpBinding
    private lateinit var viewModelCustomer : VideoViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelCustomer = ViewModelProvider(this)[VideoViewModel::class.java]
        val barLoading = ProgressBarLoading(requireActivity())
        binding.userSignUp.setOnClickListener {
            barLoading.startLoadingDialog()
            viewModelCustomer.viewModelScope.launch {

                val response = viewModelCustomer.createSignUpRequest(name = binding.fullNameUserId.text.toString(),
                    user_email = binding.userEmailId.text.toString(), password = binding.userPasswordId.text.toString())

                if(response!=null) {
                    Toast.makeText(context,response.message, Toast.LENGTH_SHORT).show()
                    if(response.status == 201) {
                        val sharedPreferences = context?.getSharedPreferences("com.example.youtubeapp", Context.MODE_PRIVATE)
                        val editor = sharedPreferences?.edit()

                        editor?.putString("user_email", response.data.user_email)
                        editor?.putString("user_password", binding.userPasswordId.text.toString())
                        editor?.putString("user_name", response.data.user_name)
                        editor?.putInt("id",response.data.id)
                        editor?.putBoolean("session", true)
                        editor?.apply()

                        startActivity(Intent(context,HomePage::class.java))
                    }else {
                        barLoading.dismissDialog()
                        Toast.makeText(context,response.message, Toast.LENGTH_SHORT).show()
                    }
                }else {
                    barLoading.dismissDialog()
                    Toast.makeText(context,"could not sign up", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}