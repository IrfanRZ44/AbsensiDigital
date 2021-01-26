package com.bangkep.sistemabsensi.ui.pegawai.profil

import androidx.navigation.fragment.findNavController
import com.bangkep.sistemabsensi.R
import com.bangkep.sistemabsensi.base.BaseFragmentBind
import com.bangkep.sistemabsensi.databinding.FragmentProfilBinding

class ProfilFragment : BaseFragmentBind<FragmentProfilBinding>() {
    private lateinit var viewModel: ProfilViewModel

    override fun getLayoutResource(): Int = R.layout.fragment_profil

    override fun myCodeHere() {
        init()
    }

    private fun init() {
        bind.lifecycleOwner = this
        viewModel = ProfilViewModel(findNavController(), context, savedData)
        bind.viewModel = viewModel
        viewModel.setData()
    }

}

