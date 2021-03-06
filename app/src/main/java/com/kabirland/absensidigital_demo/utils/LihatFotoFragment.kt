package com.kabirland.absensidigital_demo.utils

import android.content.pm.ActivityInfo
import coil.api.load
import coil.request.CachePolicy
import com.kabirland.absensidigital_demo.R
import com.kabirland.absensidigital_demo.base.BaseFragmentBind
import com.kabirland.absensidigital_demo.databinding.FragmentLihatFotoBinding

class LihatFotoFragment : BaseFragmentBind<FragmentLihatFotoBinding>() {
    override fun getLayoutResource(): Int = R.layout.fragment_lihat_foto

    override fun myCodeHere() {
        init()
    }

    private fun init(){
        bind.lifecycleOwner = this
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val urlFoto= this.arguments?.getString("urlFoto")

        bind.imgFoto.load(urlFoto) {
            crossfade(true)
            placeholder(R.drawable.ic_camera_white)
            error(R.drawable.ic_camera_white)
            fallback(R.drawable.ic_camera_white)
            memoryCachePolicy(CachePolicy.ENABLED)
        }
    }
}