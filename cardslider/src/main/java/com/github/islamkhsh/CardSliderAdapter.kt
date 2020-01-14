package com.github.islamkhsh

import android.util.SparseArray
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.core.util.forEach
import androidx.core.view.MarginLayoutParamsCompat
import androidx.core.view.marginStart
import androidx.recyclerview.widget.RecyclerView

abstract class CardSliderAdapter<VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    internal val viewHolders = SparseArray<VH>()

    @CallSuper
    override fun onBindViewHolder(holder: VH, position: Int) {
        bindVH(holder, position)
        viewHolders.put(position, holder)
    }

    /**
     * This method should update the contents of the {@link VH#itemView} to reflect the item at the
     * given position.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    abstract fun bindVH(holder: VH, position: Int)

}