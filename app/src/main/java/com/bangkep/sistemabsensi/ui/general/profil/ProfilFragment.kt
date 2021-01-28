package com.bangkep.sistemabsensi.ui.general.profil

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.bangkep.sistemabsensi.R
import com.bangkep.sistemabsensi.databinding.FragmentProfilBinding
import com.bangkep.sistemabsensi.base.BaseFragmentBind
import com.bangkep.sistemabsensi.utils.Constant
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView

class ProfilFragment : BaseFragmentBind<FragmentProfilBinding>() {
    private lateinit var viewModel: ProfilViewModel

    override fun getLayoutResource(): Int = R.layout.fragment_profil

    override fun myCodeHere() {
        init()
    }

    private fun init() {
        bind.lifecycleOwner = this
        viewModel = ProfilViewModel(
            activity,
            savedData
        )
        bind.viewModel = viewModel
        viewModel.setData()

        bind.imgFoto.setOnClickListener {
            activity?.let {
                checkPermissionCamera(it)
            }
        }
    }

    private fun checkPermissionCamera(ctx: Context?){
        val permissionsCamera = arrayOf(Manifest.permission.CAMERA)

        if (ctx != null){
            if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(permissionsCamera, Constant.codeRequestCamera)
            }
            else{
                openCameraIntent()
            }
        }
        else{
            viewModel.message.value = "Error, mohon mulai ulang aplikasi"
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            Constant.codeRequestCamera ->if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCameraIntent()
            }
            else ->
                viewModel.message.value = "Mohon izinkan camera"

        }
    }

    private fun openCameraIntent() {
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

            if (resultCode == Activity.RESULT_OK){
                val imageUri = result.uri
                bind.imgFoto.setBackgroundResource(android.R.color.transparent)

                val idUser = savedData.getDataUser()?.idUser
                val nameFile = "${System.currentTimeMillis()}__${savedData.getDataUser()?.username}"
                val foto = imageUri.path
                viewModel.foto.value = imageUri

                val imagePath = imageUri.toString()
                val extension = imagePath.substring(imagePath.lastIndexOf("."))

                val urlFoto = Constant.reffUrlFotoProfil + nameFile + extension

                val dataUser = savedData.getDataUser()
                dataUser?.foto = urlFoto
                savedData.setDataObject(dataUser, Constant.reffUser)

                if (!idUser.isNullOrEmpty() && !foto.isNullOrEmpty()){
                    activity?.let { viewModel.uploadMultipart(idUser, nameFile, foto, it) }
                }
                else{
                    viewModel.message.value = "Error, gagal mengupload foto"
                }
            }
        }
    }
}

