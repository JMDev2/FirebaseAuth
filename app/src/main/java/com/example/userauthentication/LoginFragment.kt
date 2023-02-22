package com.example.userauthentication

import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.example.userauthentication.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()



        //User Uathentication
        binding.loginButton.setOnClickListener {
            userAuthentication()
        }

        binding.forgotPassword.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            val view = layoutInflater.inflate(R.layout.forgot_password, null)
            val userEmail = view.findViewById<EditText>(R.id.editBox)
            builder.setView(view)
            val dialog = builder.create()

            view.findViewById<Button>(R.id.btnReset).setOnClickListener {
                compareEmail(userEmail)
                dialog.dismiss()
            }

            view.findViewById<Button>(R.id.btnCancel).setOnClickListener {
                dialog.dismiss()
            }

            if (dialog.window != null){
                dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            }

            dialog.show()

        }

        //redirecting to signup fragment
        binding.signupRedirectText.setOnClickListener {
            redirectingToSignUp()
        }
    }

    private fun redirectingToSignUp() {
        val signUpFragment = SignUpFragment()
        val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView, signUpFragment)
        transaction.commit()
    }


    //user authentication
    private fun userAuthentication() {

        val email = binding.loginEmail.text.toString()
        val password = binding.loginPassword.text.toString()


        if(email.isNotEmpty() && password.isNotEmpty()){
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        val homeFragment = HomeFragment()
                        val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
                        transaction.replace(R.id.fragmentContainerView, homeFragment)
                        transaction.commit()
                    }else{
                        Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }

        }else{
            Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show()

        }
    }


    //checking email validity
    private fun compareEmail(email: EditText){
        if(email.text.toString().isEmpty()){
            return
        }
        if (Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()){
            return
        }


        //reset pasword
        firebaseAuth.sendPasswordResetEmail(email.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    Toast.makeText(context, "Check your email", Toast.LENGTH_SHORT).show()

                }
        }
    }

}