package com.bangkep.sistemabsensi.ui.pegawai.riwayat

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.request.CachePolicy
import com.bangkep.sistemabsensi.R
import com.bangkep.sistemabsensi.model.ModelAbsensi
import com.bangkep.sistemabsensi.utils.onClickFoto
import kotlinx.android.synthetic.main.item_absen.view.*

class AdapterDataRiwayat(
    private val listAfiliasi: ArrayList<ModelAbsensi>,
    private val onClik: (ModelAbsensi) -> Unit,
    private val navController: NavController
) : RecyclerView.Adapter<AdapterDataRiwayat.AfiliasiHolder>() {

    inner class AfiliasiHolder(private val itemV: View) :
        RecyclerView.ViewHolder(itemV) {
        @SuppressLint("SetTextI18n")
        fun bindAfiliasi(
            itemData: ModelAbsensi,
            onClik: (ModelAbsensi) -> Unit) {

            itemV.textTanggal.text = itemData.date_created
            itemV.textJenis.text = "Absen : ${itemData.jenis}"
            when (itemData.status) {
                "1" -> {
                    itemV.textStatus.text = "Status : Dikonfirmasi"
                    itemV.textStatus.setTextColor(Color.GREEN)
                }
                "2" -> {
                    itemV.textStatus.text = "Status : Ditolak"
                    itemV.textStatus.setTextColor(Color.RED)
                }
                else -> {
                    itemV.textStatus.text = "Status : Belum dikonfirmasi"
                    itemV.textStatus.setTextColor(Color.BLUE)
                }
            }
            itemV.imgFoto.load(itemData.img) {
                crossfade(true)
                placeholder(R.drawable.ic_camera_white)
                error(R.drawable.ic_camera_white)
                fallback(R.drawable.ic_camera_white)
                memoryCachePolicy(CachePolicy.ENABLED)
            }

            itemV.imgFoto.setOnClickListener {
                onClickFoto(itemData.img,
                    navController)
            }

            itemV.setOnClickListener {
                onClik(itemData)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AfiliasiHolder {
        return AfiliasiHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_absen,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = listAfiliasi.size
    override fun onBindViewHolder(holder: AfiliasiHolder, position: Int) {
        holder.bindAfiliasi(listAfiliasi[position], onClik)
    }
}
