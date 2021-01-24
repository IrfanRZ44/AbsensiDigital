package com.bangkep.sistemabsensi.ui.pegawai.absenDatang

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.navigation.fragment.findNavController
import com.bangkep.sistemabsensi.R
import com.bangkep.sistemabsensi.base.BaseFragmentBind
import com.bangkep.sistemabsensi.databinding.FragmentAbsenDatangBinding
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView


class AbsenDatangFragment : BaseFragmentBind<FragmentAbsenDatangBinding>() {
    private lateinit var viewModel: AbsenDatangViewModel

    override fun getLayoutResource(): Int = R.layout.fragment_absen_datang

    override fun myCodeHere() {
        setHasOptionsMenu(true)
        init()
        bind.cardFoto.setOnClickListener {
            onClickFoto()
        }
    }

    private fun init() {
        bind.lifecycleOwner = this
        viewModel = AbsenDatangViewModel(activity, findNavController(), savedData)
        bind.viewModel = viewModel
        bind.btnAbsen.isEnabled = false
        viewModel.idHari = this.arguments?.getString("id_hari")
    }

    private fun onClickFoto(){
        context?.let {
            CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(it, this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)

            if (resultCode == RESULT_OK){
                val imageUri = result.uri
                viewModel.foto.value = imageUri
                bind.imgFoto.setBackgroundResource(android.R.color.transparent)
                bind.btnAbsen.isEnabled = true
            }
        }
    }
}

