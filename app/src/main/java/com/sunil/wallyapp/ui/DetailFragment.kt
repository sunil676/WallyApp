package com.sunil.wallyapp.ui

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.sunil.wallyapp.R
import com.sunil.wallyapp.data.model.Photos
import com.sunil.wallyapp.databinding.FragmentDetailBinding
import com.sunil.wallyapp.service.DownloadNotificationService
import com.sunil.wallyapp.utils.AppConstant.PROGRESS_UPDATE


class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val args: ProfileFragmentArgs by navArgs()
    private var imageUrl: String? = null
    private val PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // shared transition
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        setUpView(args.content)

        return binding.root
    }

    private fun setUpView(photos: Photos) {
        Glide.with(requireActivity())
            .load(photos.urls?.regular)
            .into(binding.imageDetail)

        Glide.with(requireActivity())
            .load(photos.user?.profileImage?.medium)
            .into(binding.profilePic)

        binding.appCompatTextViewDes.text = photos.description
        binding.appCompatTextViewAltDes.text = photos.altDescription

        //Load animation
        val slideDown: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_down)
        val slideLeft: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_left)

        // Start animation
        binding.appCompatTextViewDes.startAnimation(slideDown)
        binding.appCompatTextViewAltDes.startAnimation(slideLeft)

        imageUrl = photos.urls?.regular

        binding.buttonDownload.setOnClickListener(View.OnClickListener {
            if (checkPermission()) {
                startImageDownload()
            } else {
                requestPermission()
            }
        })
        registerReceiver()
    }

    private fun startImageDownload() {
        val intent = Intent(requireContext(), DownloadNotificationService::class.java)
        intent.putExtra("URL", imageUrl)
        requireActivity().startService(intent)

    }

    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }
    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startImageDownload()
            } else {
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerReceiver() {
        val bManager = LocalBroadcastManager.getInstance(requireContext())
        val intentFilter = IntentFilter()
        intentFilter.addAction(PROGRESS_UPDATE)
        bManager.registerReceiver(mBroadcastReceiver, intentFilter)
    }

    private val mBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            if (intent.action == PROGRESS_UPDATE) {
                val downloadComplete = intent.getBooleanExtra("downloadComplete", false)
                //Log.d("API123", download.getProgress() + " current progress");
                if (downloadComplete) {
                    Toast.makeText(context, "File download completed", Toast.LENGTH_SHORT).show()
                    /*val file = File(Environment.getDataDirectory()
                            .absolutePath+ File.separator.toString() +
                            UUID.randomUUID().toString() +".jpg")*/
                }
            }
        }
    }
}