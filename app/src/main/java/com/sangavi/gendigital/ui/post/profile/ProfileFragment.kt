package com.sangavi.gendigital.ui.post.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sangavi.gendigital.MainActivity
import com.sangavi.gendigital.data.SharedPrefManager
import com.sangavi.gendigital.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userDetails = SharedPrefManager.getUserListUIData(requireContext())
        if(userDetails != null)
            binding.user = userDetails

        binding.tvLocateMe.setOnClickListener {
            pinLocationMap(userDetails?.lat, userDetails?.lng)
        }

        binding.ivLogout.setOnClickListener {
            SharedPrefManager.clearUserListUIData(requireContext())
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }

    private fun pinLocationMap(latitude: String?, longitude: String?) {
        // Create a Uri from an intent string. Open map using intent to pin a specific location (latitude, longitude)
        val mapUri = Uri.parse("https://maps.google.com/maps/search/$latitude,$longitude")
        val intent = Intent(Intent.ACTION_VIEW, mapUri)
        startActivity(intent)
    }
}