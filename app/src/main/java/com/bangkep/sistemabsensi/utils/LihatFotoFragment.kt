package com.bangkep.sistemabsensi.utils

import coil.api.load
import coil.request.CachePolicy
import com.bangkep.sistemabsensi.R
import com.bangkep.sistemabsensi.base.BaseFragmentBind
import com.bangkep.sistemabsensi.databinding.FragmentLihatFotoBinding

class LihatFotoFragment : BaseFragmentBind<FragmentLihatFotoBinding>() {
    override fun getLayoutResource(): Int = R.layout.fragment_lihat_foto

    override fun myCodeHere() {
        init()
        onCLick()
    }

    private fun init(){
        bind.lifecycleOwner = this
        val urlFoto= this.arguments?.getString("urlFoto")

        bind.imgFoto.load(urlFoto) {
            crossfade(true)
            placeholder(R.drawable.ic_camera_white)
            error(R.drawable.ic_camera_white)
            fallback(R.drawable.ic_camera_white)
            memoryCachePolicy(CachePolicy.ENABLED)
        }
    }

    private fun onCLick() {
    }
}