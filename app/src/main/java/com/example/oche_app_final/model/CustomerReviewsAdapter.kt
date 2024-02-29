package com.example.oche_app_final.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.oche_app_final.R

class CustomerReviewsAdapter(private val customerReviews: List<CustomerReviews>) :
    RecyclerView.Adapter<CustomerReviewsAdapter.CustomerReviewsViewHolder>() {

    inner class CustomerReviewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImageView: ImageView = itemView.findViewById(R.id.profileImageView)
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val messageTextView: TextView = itemView.findViewById(R.id.messageTextView)
        val star1: ImageView = itemView.findViewById(R.id.driverStar1)
        val star2: ImageView = itemView.findViewById(R.id.driverStar2)
        val star3: ImageView = itemView.findViewById(R.id.driverStar3)
        val star4: ImageView = itemView.findViewById(R.id.driverStar4)
        val star5: ImageView = itemView.findViewById(R.id.driverStar5)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerReviewsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_chat_driver, parent, false)
        return CustomerReviewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomerReviewsViewHolder, position: Int) {
        val customerReview = customerReviews[position]

        holder.profileImageView.setImageResource(customerReview.profileImage)
        holder.nameTextView.text = customerReview.name
        holder.messageTextView.text = customerReview.description

        val stars = customerReview.stars
        val starViews =
            arrayOf(holder.star1, holder.star2, holder.star3, holder.star4, holder.star5)
        for (i in starViews.indices) {
            if (i < stars) {
                starViews[i].setImageResource(R.drawable.ic_star_filled)
            } else {
                starViews[i].setImageResource(R.drawable.ic_star)
            }
        }
    }

    override fun getItemCount(): Int {
        return minOf(customerReviews.size, 2)
    }
}