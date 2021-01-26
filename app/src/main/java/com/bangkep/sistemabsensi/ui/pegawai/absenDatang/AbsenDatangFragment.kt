package com.bangkep.sistemabsensi.ui.pegawai.absenDatang

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.fragment.findNavController
import com.bangkep.sistemabsensi.R
import com.bangkep.sistemabsensi.base.BaseFragmentBind
import com.bangkep.sistemabsensi.databinding.FragmentAbsenDatangBinding
import com.bangkep.sistemabsensi.utils.Constant
import com.bumptech.glide.Glide
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class AbsenDatangFragment : BaseFragmentBind<FragmentAbsenDatangBinding>() {
    private lateinit var viewModel: AbsenDatangViewModel
    private var imageFilePath: String? = null

    override fun getLayoutResource(): Int = R.layout.fragment_absen_datang

    override fun myCodeHere() {
        setHasOptionsMenu(true)
        init()
        bind.cardFoto.setOnClickListener {
            activity?.let {
                checkPermissionStorage(it)
            }
        }
    }

    private fun init() {
        bind.lifecycleOwner = this
        viewModel = AbsenDatangViewModel(activity, findNavController(), savedData)
        bind.viewModel = viewModel
        bind.btnAbsen.isEnabled = false
        viewModel.idHari = this.arguments?.getString(Constant.reffIdHari)
        viewModel.idAbsensi = this.arguments?.getString(Constant.reffIdAbsen)
        viewModel.jenisAbsensi = this.arguments?.getString(Constant.reffJenis)
        val foto = this.arguments?.getString(Constant.reffFoto)

        if (!viewModel.idAbsensi.isNullOrEmpty() && !foto.isNullOrEmpty()){
            Glide.with(this).load(foto).into(bind.imgFoto)
        }

        if (viewModel.jenisAbsensi == Constant.jenisMasuk){
            if (!viewModel.idAbsensi.isNullOrEmpty()){
                supportActionBar?.title = "Absensi Datang Ulang"
            }
            else{
                supportActionBar?.title = "Absensi Datang"
            }
        }
        else if (viewModel.jenisAbsensi == Constant.jenisPulang){
            if (!viewModel.idAbsensi.isNullOrEmpty()){
                supportActionBar?.title = "Absensi Pulang Ulang"
            }
            else{
                supportActionBar?.title = "Absensi Pulang"
            }
        }
        else if (viewModel.jenisAbsensi == Constant.jenisApel){
            if (!viewModel.idAbsensi.isNullOrEmpty()){
                supportActionBar?.title = "Absensi Apel Ulang"
            }
            else{
                supportActionBar?.title = "Absensi Apel"
            }
        }
    }

    private fun checkPermissionStorage(ctx: Context){
        val permissionStorage = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(ctx, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissionStorage, Constant.codeRequestStorage)
        }
        else{
            checkPermissionCamera(ctx)
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
            Constant.codeRequestStorage -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkPermissionCamera(context)
            }
            Constant.codeRequestCamera ->if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCameraIntent()
            }
            else ->
                viewModel.message.value = "Mohon izinkan penyimpanan dan camera"

        }
    }

    private fun openCameraIntent() {
        val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val act = activity

        if (act != null && pictureIntent.resolveActivity(act.packageManager) != null) {
            var photoFile : File? = null

            try {
                photoFile = createImageFile(act)
            } catch (ex: IOException) {
                viewModel.message.value = ex.message
            }

            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(act,"${Constant.appId}.provider", photoFile)
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(pictureIntent, Constant.codeRequestCamera)
            }
            else{
                viewModel.message.value = "Error, gagal memilih foto"
            }
        }
        else{
            viewModel.message.value = "Error, perangkat Anda tidak support untuk mengambil gambar"
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(act: Activity): File {
        val timeStamp = SimpleDateFormat(Constant.dateFormat3, Locale.getDefault()).format(Date())
        val imageFileName = "IMG_" + timeStamp + "_"
        val storageDir = act.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(imageFileName, ".jpg", storageDir)

        imageFilePath = image.absolutePath
        return image
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.codeRequestCamera && resultCode == RESULT_OK) {
            viewModel.foto.value = Uri.parse(imageFilePath)
            Glide.with(this).load(imageFilePath).into(bind.imgFoto)
            bind.btnAbsen.isEnabled = true
        }
    }
}

