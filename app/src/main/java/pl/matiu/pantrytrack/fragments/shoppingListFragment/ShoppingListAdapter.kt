package pl.matiu.pantrytrack.fragments.shoppingListFragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.matiu.pantrytrack.R
import pl.matiu.pantrytrack.fragments.firstfragment.FirstFragmentProductModel

class ShoppingListAdapter(private var products: List<FirstFragmentProductModel>?) : RecyclerView.Adapter<ShoppingListAdapter.ViewHolder>() {

    fun updateProducts(newProducts: List<FirstFragmentProductModel>) {
        this.products = newProducts
        notifyItemInserted(newProducts.size - 1)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productName: TextView = view.findViewById(R.id.product_name)
        val quantity: TextView = view.findViewById(R.id.product_quantity)
        val targetQuantity: TextView = view.findViewById(R.id.product_target_quantity)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.shopping_list_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun getItemCount() = products?.size ?: 0

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.productName.text = products?.get(position)?.productName
        viewHolder.quantity.text = products?.get(position)?.quantity.toString()
        viewHolder.targetQuantity.text = products?.get(position)?.targetQuantity.toString()
    }
}