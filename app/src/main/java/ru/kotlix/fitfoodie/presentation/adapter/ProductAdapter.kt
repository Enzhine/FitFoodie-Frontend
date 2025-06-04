package ru.kotlix.fitfoodie.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.kotlix.fitfoodie.R
import ru.kotlix.fitfoodie.domain.dto.Product
import ru.kotlix.fitfoodie.mapper.toLocalizedName
import ru.kotlix.fitfoodie.presentation.viewmodel.ProductsFragmentViewModel

class ProductAdapter(
    private var products: List<Product>,
    private var productsFragmentViewModel: ProductsFragmentViewModel
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.productsItemName)
        private val image: ImageView = itemView.findViewById(R.id.productsItemImage)
        private val quantity: TextView = itemView.findViewById(R.id.productsItemQuantity)
        private val quant: TextView = itemView.findViewById(R.id.productsItemQuant)

        private val inc: ImageButton = itemView.findViewById(R.id.productsItemBtnInc)
        private val dec: ImageButton = itemView.findViewById(R.id.productsItemBtnDec)

        fun bind(product: Product) {
            Glide.with(itemView)
                .load(itemView.context.getString(R.string.baseUrlProductImage, product.id.toString()))
                .into(image)

            title.text = product.title
            quantity.text = "${product.quantity} ${product.unit.toLocalizedName(itemView.context)}"
            quant.text = "${product.quant} ${product.unit.toLocalizedName(itemView.context)}"

            inc.setOnClickListener {
                product.quantity += product.quant
                quantity.text = "${product.quantity} ${product.unit.toLocalizedName(itemView.context)}"
                productsFragmentViewModel.updateProduct(product)
            }
            dec.setOnClickListener {
                product.quantity -= product.quant
                if (product.quantity <= 0) {
                    product.quantity = 0
                }
                quantity.text = "${product.quantity} ${product.unit.toLocalizedName(itemView.context)}"
                productsFragmentViewModel.updateProduct(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val task = products[position]
        holder.bind(task)
    }

    override fun getItemCount(): Int = products.size
}