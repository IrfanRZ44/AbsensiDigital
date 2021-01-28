package com.bangkep.sistemabsensi.ui.pegawai.beranda

import androidx.navigation.fragment.findNavController
import com.bangkep.sistemabsensi.R
import com.bangkep.sistemabsensi.base.BaseFragmentBind
import com.bangkep.sistemabsensi.databinding.FragmentBerandaPegawaiBinding

class BerandaPegawaiFragment : BaseFragmentBind<FragmentBerandaPegawaiBinding>() {
    private lateinit var viewModel: BerandaPegawaiViewModel

    override fun getLayoutResource(): Int = R.layout.fragment_beranda_pegawai

    override fun myCodeHere() {
        init()
    }

    private fun init() {
        bind.lifecycleOwner = this
        viewModel = BerandaPegawaiViewModel(findNavController(), bind.btnApel, savedData)
        bind.viewModel = viewModel
        viewModel.isDatang.value = false
        viewModel.isPulang.value = false
        viewModel.isApel.value = false
        viewModel.getHariKerja()
    }
}

