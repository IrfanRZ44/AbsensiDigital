package com.kabirland.absensidigital_demo.ui.pegawai.absensi

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.kabirland.absensidigital_demo.R
import com.kabirland.absensidigital_demo.base.BaseFragmentBind
import com.kabirland.absensidigital_demo.databinding.FragmentAbsensiBinding
import com.kabirland.absensidigital_demo.utils.Constant
import com.bumptech.glide.Glide


class AbsensiFragment : BaseFragmentBind<FragmentAbsensiBinding>() {
    private lateinit var viewModel: AbsensiViewModel

    override fun getLayoutResource(): Int = R.layout.fragment_absensi

    override fun myCodeHere() {
        setHasOptionsMenu(true)
        init()

        bind.cardFoto.setOnClickListener {
            val bundle = Bundle()
            val fragmentTujuan = AbsensiFragment()
            bundle.putString(Constant.reffIdHari, viewModel.idHari)
            bundle.putString(Constant.reffIdAbsen, viewModel.idAbsensi)
            bundle.putString(Constant.reffJenis, viewModel.jenisAbsensi)
            fragmentTujuan.arguments = bundle
            val navOption = NavOptions.Builder().setPopUpTo(R.id.navCamera, true).build()
            findNavController().navigate(R.id.navCamera,bundle, navOption)
        }
    }

    private fun init() {
        bind.lifecycleOwner = this
        viewModel = AbsensiViewModel(activity, findNavController(), savedData)
        bind.viewModel = viewModel
        bind.btnAbsen.isEnabled = false
        viewModel.idHari = this.arguments?.getString(Constant.reffIdHari)
        viewModel.idAbsensi = this.arguments?.getString(Constant.reffIdAbsen)
        viewModel.jenisAbsensi = this.arguments?.getString(Constant.reffJenis)
        val foto = this.arguments?.getString(Constant.reffFoto)
        val foto2 = this.arguments?.getString(Constant.reffFoto2)

        if (!foto2.isNullOrEmpty()){
            Glide.with(this).load(foto2).into(bind.imgFoto)
            viewModel.foto.value = Uri.parse(foto2)
            bind.btnAbsen.isEnabled = true
        }
        else if (!foto.isNullOrEmpty()){
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
}

