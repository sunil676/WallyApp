package com.sunil.wallyapp.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.sunil.wallyapp.data.model.Photos
import com.sunil.wallyapp.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val args: ProfileFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        setupUI(args.content)

        return binding.root
    }

    private fun setupUI(photos: Photos) {
        Glide.with(requireActivity())
            .load(photos.user?.profileImage?.large)
            .into(binding.imageProfile)

        binding.name.text = photos.user?.name

        // check the user is verified or not
        val prefs = requireActivity().getSharedPreferences(
            "USER_PREF", Context.MODE_PRIVATE
        )
        val phoneNumber = prefs.getString("phoneNumber", "")
        binding.phone.text = phoneNumber
    }
}