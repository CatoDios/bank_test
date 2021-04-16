package com.example.banktest.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.banktest.databinding.DetailFragmentBinding
import com.example.domain.User

class UserDetailsFragment  : Fragment (){
    private lateinit var binding: DetailFragmentBinding
    private val args: UserDetailsFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DetailFragmentBinding.inflate(inflater)
        retainInstance = true
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.let {
            val user : User = args.userDetails
            showData(user)
        }
    }

    private fun showData(user: User) {
        with(binding){
            tvName.text = user.name
            tvUsername.text = user.username
            tvAddress.text = user.address.street + " "+ user.address.city
            tvEmail.text = user.email
            tvPhone.text = user.phone
            tvWeb.text = user.website

            tvCompanyName.text = user.company.name
            tvCompanyCatchPhrase.text = user.company.catchPhrase
            tvBs.text = user.company.bs
        }
    }

}