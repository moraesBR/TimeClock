package com.materdei.timeclock.adpters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.materdei.timeclock.R
import com.materdei.timeclock.dto.RegisterDetails
import com.materdei.timeclock.security.FirebaseAuthentication
import com.materdei.timeclock.utils.PunchCard

class PunchAdapter (private var punchCards: List<RegisterDetails>) :
    RecyclerView.Adapter<PunchAdapter.DataViewHolder>() {

    class DataViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val punchLabel: LinearLayout
        var nameWorker: TextView
        val dateInfo: TextView
        val timeInfo: TextView
        val punchImage: ImageView

        init {
            punchLabel = itemView.findViewById(R.id.punchLabel)
            nameWorker = itemView.findViewById(R.id.nameWorker)
            dateInfo = itemView.findViewById(R.id.dateInfo)
            timeInfo = itemView.findViewById(R.id.timeInfo)
            punchImage = itemView.findViewById(R.id.punchImage)
        }
    }

    override fun getItemCount() = punchCards.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.punch_item,parent,false)
        return DataViewHolder(view)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val data = punchCards[position]
        when (data.punch){
            PunchCard.IN.value -> {
                holder.punchLabel.setBackgroundResource(R.color.dark_blue_color)
                holder.punchImage.setImageResource(R.drawable.gowork)
            }
            PunchCard.OUT.value -> {
                holder.punchLabel.setBackgroundResource(R.color.red_color)
                holder.punchImage.setImageResource(R.drawable.gohome)
            }
        }
        FirebaseAuthentication.firebaseUser?.let {
            holder.nameWorker.text = it.displayName
        }
        holder.dateInfo.text = data.date
        holder.timeInfo.text = data.time.replace("-",":")
    }
}