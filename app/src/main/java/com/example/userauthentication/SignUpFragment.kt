package com.example.userauthentication

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.example.userauthentication.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth


class SignUpFragment : Fragment() {
   private lateinit var binding: FragmentSignUpBinding
   private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()


        binding.signupButton.setOnClickListener {
            val email = binding.signupEmail.text.toString()
            val password = binding.signupPassword.text.toString()
            val cpassword = binding.signupConfirm.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && cpassword.isNotEmpty()){
                if (password ==cpassword){
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                        if (it.isSuccessful){
                            val homeFragment = HomeFragment()
                            val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
                            transaction.replace(R.id.fragmentContainerView, homeFragment)
                            transaction.commit()
                        }else{
                            Toast.makeText(requireActivity(), "Failed", Toast.LENGTH_LONG).show()

                        }
                    }
                }else{
                    Toast.makeText(requireActivity(), "Passowrd does not match", Toast.LENGTH_LONG).show()

                }
            }else{
                Toast.makeText(requireActivity(), "Field cannot be empty", Toast.LENGTH_LONG).show()

            }
        }
        binding.loginRedirectText.setOnClickListener {
            val loginFragment = LoginFragment()
            val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, loginFragment)
            transaction.commit()
        }

    }


}