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

class DishAdapter(
    private var dishes: List<DishShort>,
    private var callback: (DishShort) -> Unit
) : RecyclerView.Adapter<DishAdapter.DishViewHolder>() {

    inner class DishViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val card: MaterialCardView = itemView.findViewById(R.id.cardDish)

        private val title: TextView = itemView.findViewById(R.id.dishesItemName)
        private val image: ImageView = itemView.findViewById(R.id.dishesItemImage)
        private val tags: TextView = itemView.findViewById(R.id.dishesItemTags)
        private val description: TextView = itemView.findViewById(R.id.dishesItemDescription)
        private val indicator: ImageView = itemView.findViewById(R.id.dishesItemIndicator)

        fun bind(dish: DishShort) {
            Glide.with(itemView)
                .load(itemView.context.getString(R.string.baseUrlDishImage, dish.id.toString()))
                .into(image)

            title.text = dish.title
            tags.text = dish.tags.joinToString(", ")
            description.text = "${dish.calories}kk ${dish.cookMinutes}min"
            if (dish.order > 0) {
                indicator.setImageResource(R.drawable.ic_circle_check)
            } else {
                indicator.setImageResource(R.drawable.ic_circle_cross)
            }

            card.setOnClickListener { callback(dish) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dish, parent, false)
        return DishViewHolder(view)
    }

    override fun onBindViewHolder(holder: DishViewHolder, position: Int) {
        val task = dishes[position]
        holder.bind(task)
    }

    override fun getItemCount(): Int = dishes.size
}