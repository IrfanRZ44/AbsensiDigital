package com.bangkep.sistemabsensi.ui.operator.detailPegawai

import android.annotation.SuppressLint
import androidx.navigation.fragment.findNavController
import com.bangkep.sistemabsensi.R
import com.bangkep.sistemabsensi.base.BaseFragmentBind
import com.bangkep.sistemabsensi.databinding.FragmentDetailPegawaiBinding
import com.bangkep.sistemabsensi.utils.Constant

class DetailPegawaiFragment : BaseFragmentBind<FragmentDetailPegawaiBinding>() {
    private lateinit var viewModel: DetailPegawaiViewModel
    override fun getLayoutResource(): Int = R.layout.fragment_detail_pegawai

    override fun myCodeHere() {
        init()
    }

    private fun init() {
        bind.lifecycleOwner = this
        viewModel = DetailPegawaiViewModel(findNavController(), activity, savedData)
        bind.viewModel = viewModel

        setData()
    }

    @SuppressLint("SetTextI18n")
    private fun setData(){
        viewModel.dataUser.value = this.arguments?.getParcelable(Constant.reffPegawai)
        viewModel.idHari.value = this.arguments?.getString(Constant.reffIdHari)
        viewModel.dataNipNama.value = "${viewModel.dataUser.value?.username} - ${viewModel.dataUser.value?.nama}"
        viewModel.jenisAbsen.value = Constant.pegawai
    }
}

