package com.materdei.timeclock.adpters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.materdei.timeclock.R
import com.materdei.timeclock.dto.RegisterDetails
import com.materdei.timeclock.dto.Registers
import com.materdei.timeclock.dto.SingleRegister

class RegisterAdapter(dataList: MutableList<RegisterDetails>) :
    RecyclerView.Adapter<RegisterAdapter.DataViewHolder>() {

    private var registers: Registers = Registers()
    private var punchCards: List<SingleRegister>

    init {
        registers.update(dataList)
        punchCards = registers.toList()
    }

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val collapsedInfoImg: ImageView
        private val punchRecycleView: RecyclerView
        private val collapsedBtn: ConstraintLayout
        val dateTextView: TextView
        val workedHourTextView: TextView

        init {
            collapsedInfoImg = itemView.findViewById(R.id.collapsedInfoImg)
            dateTextView = itemView.findViewById(R.id.dateTextView)
            workedHourTextView = itemView.findViewById(R.id.workedHourTextView)
            punchRecycleView = itemView.findViewById(R.id.punchRecycleView)
            collapsedBtn = itemView.findViewById(R.id.collapsedBtn)
            collapsedBtn.setOnClickListener {
                if (punchRecycleView.isVisible) {
                    punchRecycleView.visibility = View.GONE
                    collapsedInfoImg.setImageResource(R.drawable.ic_is_not_collapsed)
                } else {
                    punchRecycleView.visibility = View.VISIBLE
                    collapsedInfoImg.setImageResource(R.drawable.ic_is_collapsed)
                }
            }
        }

        fun bind(result: List<RegisterDetails>) {
            val punchAdapter = PunchAdapter(result)
            punchRecycleView.apply {
                layoutManager = LinearLayoutManager(
                    itemView.context, LinearLayoutManager.VERTICAL, false
                )

                adapter = punchAdapter
            }
        }
    }

    override fun getItemCount(): Int = punchCards.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.register_item, parent, false
        )

        return DataViewHolder(view)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val data = punchCards[position]
        holder.dateTextView.text = data.date
        holder.workedHourTextView.text = data.workedHour
        holder.bind(punchCards[position].punches)
    }

    fun update(newPunchCards: MutableList<RegisterDetails>) {
        registers.update(newPunchCards)
        punchCards = registers.toList()
        notifyDataSetChanged()
    }

}