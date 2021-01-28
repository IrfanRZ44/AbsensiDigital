package com.bangkep.sistemabsensi.ui.operator.belumAbsen

import com.bangkep.sistemabsensi.R
import com.bangkep.sistemabsensi.base.BaseFragmentBind
import com.bangkep.sistemabsensi.databinding.FragmentBelumAbsenBinding


class BelumAbsenFragment : BaseFragmentBind<FragmentBelumAbsenBinding>() {
    private lateinit var viewModel: BelumAbsenViewModel

    override fun getLayoutResource(): Int = R.layout.fragment_belum_absen

    override fun myCodeHere() {
        init()
    }

    private fun init() {
        bind.lifecycleOwner = this
        viewModel = BelumAbsenViewModel()
        bind.viewModel = viewModel
    }

}

