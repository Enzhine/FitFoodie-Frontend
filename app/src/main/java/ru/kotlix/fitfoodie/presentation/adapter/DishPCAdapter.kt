package ru.kotlix.fitfoodie.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import ru.kotlix.fitfoodie.R
import ru.kotlix.fitfoodie.domain.dto.DishShort
import ru.kotlix.fitfoodie.domain.dto.Product
import ru.kotlix.fitfoodie.presentation.viewmodel.DishesFragmentViewModel
import ru.kotlix.fitfoodie.presentation.viewmodel.ProductsFragmentViewModel

class DishPCAdapter(
    private var dishes: List<DishShort>,
    private var callback: (DishShort) -> Unit
) : RecyclerView.Adapter<DishPCAdapter.DishViewHolder>() {

    inner class DishViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.dishesItemName)
        private val image: ImageView = itemView.findViewById(R.id.dishesItemImage)
        private val tags: TextView = itemView.findViewById(R.id.dishesItemTags)
        private val description: TextView = itemView.findViewById(R.id.dishesItemDescription)

        private val infoBtn: ImageView = itemView.findViewById(R.id.dishesItemBtnInfo)
        private val selectBtn: ImageView = itemView.findViewById(R.id.dishesItemBtnSelect)

        fun bind(dish: DishShort) {
            Glide.with(itemView)
                .load(itemView.context.getString(R.string.baseUrlDishImage, dish.id.toString()))
                .into(image)

            title.text = dish.title
            tags.text = dish.tags.joinToString(", ") { it.name }
            description.text = "${dish.calories}kk ${dish.cookMinutes}min"

            infoBtn.setOnClickListener { callback(dish) }
            selectBtn.setOnClickListener {
                dish.selected = !dish.selected
                if (dish.selected) {
                    selectBtn.setImageResource(R.drawable.ic_circle_check)
                } else {
                    selectBtn.setImageDrawable(null)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dish_selectable, parent, false)
        return DishViewHolder(view)
    }

    override fun onBindViewHolder(holder: DishViewHolder, position: Int) {
        val task = dishes[position]
        holder.bind(task)
    }

    override fun getItemCount(): Int = dishes.size
}