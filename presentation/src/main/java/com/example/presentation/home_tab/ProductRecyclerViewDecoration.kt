package com.example.presentation.home_tab

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ProductRecyclerViewDecoration(private val context: Context) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        // just to make sure the spacing does not get duplicated on refresh or on resume
        if (parent.itemDecorationCount == 1) {
            val lastRow = parent.adapter?.itemCount?.minus(1)
            val viewPosition = parent.getChildAdapterPosition(view)
            if (lastRow != null && viewPosition == lastRow) {
                // not sure why outRect requires pixels instead of DP,
                // but this line converts 20 dp to pixels
                outRect.bottom = TypedValue
                    .applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        20.0f,
                        context.resources.displayMetrics
                    )
                    .toInt()
            }
        }
    }
}
