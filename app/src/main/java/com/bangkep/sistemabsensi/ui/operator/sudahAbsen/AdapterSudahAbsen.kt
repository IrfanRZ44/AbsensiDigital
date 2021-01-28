package com.bangkep.sistemabsensi.ui.operator.sudahAbsen

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.request.CachePolicy
import com.bangkep.sistemabsensi.R
import com.bangkep.sistemabsensi.model.ModelAbsensi
import com.bangkep.sistemabsensi.utils.onClickFoto
import kotlinx.android.synthetic.main.item_verify_absen.view.*

class AdapterSudahAbsen(
    private val listAfiliasi: ArrayList<ModelAbsensi>,
    private val getDataPegawai: (AppCompatTextView, String, ModelAbsensi, AppCompatButton) -> Unit,
    private val navController: NavController
) : RecyclerView.Adapter<AdapterSudahAbsen.AfiliasiHolder>() {

    inner class AfiliasiHolder(private val itemV: View) :
        RecyclerView.ViewHolder(itemV) {
        @SuppressLint("SetTextI18n")
        fun bindAfiliasi(itemData: ModelAbsensi) {

            getDataPegawai(itemV.textNama, itemData.nip, itemData, itemV.btnKonfirmasi)
            itemV.textNip.text = itemData.nip
            itemV.textTanggal.text = itemData.date_created
            itemV.textJenis.text = "Absen : ${itemData.jenis}"

            when (itemData.status) {
                "1" -> {
                    itemV.btnKonfirmasi.visibility = View.GONE
                    itemV.textStatus.text = "Status : Dikonfirmasi"
                    itemV.textStatus.setTextColor(Color.GREEN)
                }
                "2" -> {
                    itemV.btnKonfirmasi.visibility = View.GONE
                    itemV.textStatus.text = "Status : Ditolak"
                    itemV.textStatus.setTextColor(Color.RED)
                }
                else -> {
                    itemV.btnKonfirmasi.visibility = View.VISIBLE
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
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AfiliasiHolder {
        return AfiliasiHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_verify_absen,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = listAfiliasi.size
    override fun onBindViewHolder(holder: AfiliasiHolder, position: Int) {
        holder.bindAfiliasi(listAfiliasi[position])
    }
}
