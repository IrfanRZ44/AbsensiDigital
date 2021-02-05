package com.bangkep.sistemabsensi.ui.pegawai.riwayat

import android.app.DatePickerDialog
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.navigation.fragment.findNavController
import com.bangkep.sistemabsensi.R
import com.bangkep.sistemabsensi.base.BaseFragmentBind
import com.bangkep.sistemabsensi.databinding.FragmentRiwayatBinding
import com.bangkep.sistemabsensi.utils.Constant
import com.bangkep.sistemabsensi.utils.dismissKeyboard
import java.text.SimpleDateFormat
import java.util.*

class RiwayatFragment : BaseFragmentBind<FragmentRiwayatBinding>() {
    private lateinit var viewModel: RiwayatViewModel

    override fun getLayoutResource(): Int = R.layout.fragment_riwayat

    override fun myCodeHere() {
        setHasOptionsMenu(true)
        init()
        bind.swipeRefresh.setOnRefreshListener {
            bind.swipeRefresh.isRefreshing = false
            val nip = savedData.getDataUser()?.username

            if (!nip.isNullOrEmpty()){
                viewModel.getListRiwayat(nip)
            }
            else{
                viewModel.message.value = "Error, terjadi kesalahan database, mohon login ulang"
            }
        }
    }

    private fun init() {
        bind.lifecycleOwner = this
        viewModel = RiwayatViewModel(bind.rvData, context, findNavController())
        bind.viewModel = viewModel
        viewModel.initAdapter()

        val nip = savedData.getDataUser()?.username

        if (!nip.isNullOrEmpty()){
            viewModel.getListRiwayat(nip)
        }
        else{
            viewModel.message.value = "Error, terjadi kesalahan database, mohon login ulang"
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_search_date, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.actionSearch ->{
                activity?.let { dismissKeyboard(it) }
                val datePickerDialog: DatePickerDialog
                val localCalendar = Calendar.getInstance()

                try {
                    datePickerDialog = DatePickerDialog(activity ?: throw Exception("Error, mohon mulai ulang aplikasi"),
                        DatePickerDialog.OnDateSetListener { _, paramAnonymousInt1, paramAnonymousInt2, paramAnonymousInt3 ->
                            val dateSelected = Calendar.getInstance()
                            dateSelected[paramAnonymousInt1, paramAnonymousInt2] = paramAnonymousInt3
                            val dateFormatter = SimpleDateFormat(Constant.dateFormat1, Locale.US)
                            val datePicked = dateFormatter.format(dateSelected.time)
                            viewModel.listData.clear()
                            viewModel.adapter?.notifyDataSetChanged()

                            for (i in viewModel.listDataSearch.indices){
                                if (viewModel.listDataSearch[i].date_created.contains(datePicked)){
                                    viewModel.listData.add(viewModel.listDataSearch[i])
                                    viewModel.adapter?.notifyDataSetChanged()
                                }
                            }

                            if (viewModel.listData.size == 0){
                                viewModel.message.value = Constant.noData
                            }
                            else{
                                viewModel.message.value = ""
                            }
                        },
                        localCalendar[Calendar.YEAR]
                        ,
                        localCalendar[Calendar.MONTH]
                        ,
                        localCalendar[Calendar.DATE]
                    )

                    datePickerDialog.show()
                } catch (e: java.lang.Exception) {
                    viewModel.message.value = e.message
                }

                return false
            }
        }

        return super.onOptionsItemSelected(item)
    }
}