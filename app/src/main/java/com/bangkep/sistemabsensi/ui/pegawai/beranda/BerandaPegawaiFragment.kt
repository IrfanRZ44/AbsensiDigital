package com.bangkep.sistemabsensi.ui.pegawai.beranda

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bangkep.sistemabsensi.R
import com.bangkep.sistemabsensi.base.BaseFragmentBind
import com.bangkep.sistemabsensi.databinding.FragmentBerandaPegawaiBinding
import com.bangkep.sistemabsensi.utils.Constant
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class BerandaPegawaiFragment : BaseFragmentBind<FragmentBerandaPegawaiBinding>() {
    private lateinit var viewModel: BerandaPegawaiViewModel

    override fun getLayoutResource(): Int = R.layout.fragment_beranda_pegawai

    override fun myCodeHere() {
        init()
    }

    private fun init() {
        bind.lifecycleOwner = this
        viewModel = BerandaPegawaiViewModel(findNavController(),
            bind.btnApel, savedData, activity
        ) { context?.let { checkPermissionStorage(it) } }

        bind.viewModel = viewModel
        setData()
        viewModel.getHariKerja()
    }

    @SuppressLint("SimpleDateFormat")
    private fun setData(){
        viewModel.isDatang.value = false
        viewModel.isPulang.value = false
        viewModel.isApel.value = false
        viewModel.tglKeterangan.value = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constant.dateFormat4))
        } else {
            SimpleDateFormat(Constant.dateFormat4).format(Date())
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
                viewModel.navigateRequest()
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
                viewModel.navigateRequest()
            }
            else ->
                viewModel.message.value = "Mohon izinkan penyimpanan dan camera"

        }
    }
}

