package com.bangkep.sistemabsensi.ui.operator.beranda

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.bangkep.sistemabsensi.R
import com.bangkep.sistemabsensi.base.BaseFragmentBind
import com.bangkep.sistemabsensi.databinding.FragmentBerandaOperatorBinding
import com.bangkep.sistemabsensi.ui.general.adapter.SectionsPagerAdapter
import com.bangkep.sistemabsensi.ui.operator.belumAbsen.BelumAbsenFragment
import com.bangkep.sistemabsensi.ui.operator.sudahAbsen.SudahAbsenFragment
import com.bangkep.sistemabsensi.utils.Constant

class BerandaOperatorFragment : BaseFragmentBind<FragmentBerandaOperatorBinding>() {
    private lateinit var viewModel: BerandaOperatorViewModel
    override fun getLayoutResource(): Int = R.layout.fragment_beranda_operator

    override fun myCodeHere() {
        supportActionBar?.show()
        init()
    }

    private fun init() {
        bind.lifecycleOwner = this
        viewModel = BerandaOperatorViewModel()
        bind.viewModel = viewModel
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewPager(bind.viewPager)
        bind.tabs.setupWithViewPager(bind.viewPager)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Suppress("DEPRECATION")
    private fun setupViewPager(pager: ViewPager) {
        val adapter = SectionsPagerAdapter(childFragmentManager)
        adapter.addFragment(SudahAbsenFragment(), Constant.sudahAbsen)
        adapter.addFragment(BelumAbsenFragment(), Constant.belumAbsen)

        pager.adapter = adapter
    }
}

