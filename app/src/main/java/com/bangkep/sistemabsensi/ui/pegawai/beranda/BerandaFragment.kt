package com.bangkep.sistemabsensi.ui.pegawai.beranda

import androidx.navigation.fragment.findNavController
import com.bangkep.sistemabsensi.R
import com.bangkep.sistemabsensi.databinding.FragmentBerandaBinding
import com.bangkep.sistemabsensi.base.BaseFragmentBind

class BerandaFragment : BaseFragmentBind<FragmentBerandaBinding>() {
    private lateinit var viewModel: BerandaViewModel

    override fun getLayoutResource(): Int = R.layout.fragment_beranda

    override fun myCodeHere() {
        init()
    }

    private fun init() {
        bind.lifecycleOwner = this
        viewModel = BerandaViewModel(context, findNavController(), bind.btnApel, savedData)
        bind.viewModel = viewModel
        viewModel.isDatang.value = false
        viewModel.isPulang.value = false
        viewModel.isApel.value = false
        viewModel.getHariKerja()
    }


}

