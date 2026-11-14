package com.example.stockmate;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class ProductAdapter
        extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private ArrayList<Product> products;
    private final OnProductActionListener listener;

    // Interface for Edit and Delete actions
    public interface OnProductActionListener {
        void onEdit(Product product);
        void onDelete(Product product);
    }

    // Constructor
    public ProductAdapter(
            ArrayList<Product> products,
            OnProductActionListener listener) {

        this.products = products;
        this.listener = listener;
    }

    // Create the item view
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(
                parent.getContext()
        ).inflate(
                R.layout.item_product,
                parent,
                false
        );

        return new ViewHolder(view);
    }

    // Display product information
    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        Product product = products.get(position);

        holder.tvName.setText(product.getName());
        holder.tvCategory.setText(product.getCategory());

        holder.tvPrice.setText(
                String.format(
                        Locale.getDefault(),
                        "Unit Price: Rs. %,.2f",
                        product.getPrice()
                )
        );

        holder.tvQuantity.setText(
                "Quantity: " + product.getQuantity() + " units"
        );

        holder.tvStockValue.setText(
                String.format(
                        Locale.getDefault(),
                        "Stock Value: Rs. %,.2f",
                        product.getStockValue()
                )
        );

        // Determine Stock Status
        int quantity = product.getQuantity();

        if (quantity == 0) {
            holder.tvStockStatus.setText("OUT OF STOCK");
            holder.tvStockStatus.setTextColor(
                    Color.parseColor("#DC2626")
            );

        } else if (quantity <= 5) {
            holder.tvStockStatus.setText("LOW STOCK");
            holder.tvStockStatus.setTextColor(
                    Color.parseColor("#D97706")
            );

        } else {
            holder.tvStockStatus.setText("IN STOCK");
            holder.tvStockStatus.setTextColor(
                    Color.parseColor("#16A34A")
            );
        }

        // Edit Button Click
        holder.btnEdit.setOnClickListener(v -> {
            listener.onEdit(product);
        });

        // Delete Button Click
        holder.btnDelete.setOnClickListener(v -> {
            listener.onDelete(product);
        });

        // Open Edit screen when card is clicked
        holder.itemView.setOnClickListener(v -> {
            listener.onEdit(product);
        });
    }

    // Total number of products
    @Override
    public int getItemCount() {
        return products.size();
    }

    // Refresh the list when database data changes
    public void updateProducts(ArrayList<Product> newProducts) {
        products = newProducts;
        notifyDataSetChanged();
    }

    // ViewHolder Class
    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvName, tvCategory;
        TextView tvPrice, tvQuantity;
        TextView tvStockValue, tvStockStatus;

        Button btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(
                    R.id.tvItemProductName
            );

            tvCategory = itemView.findViewById(
                    R.id.tvItemCategory
            );

            tvPrice = itemView.findViewById(
                    R.id.tvItemPrice
            );

            tvQuantity = itemView.findViewById(
                    R.id.tvItemQuantity
            );

            tvStockValue = itemView.findViewById(
                    R.id.tvItemStockValue
            );

            tvStockStatus = itemView.findViewById(
                    R.id.tvItemStockStatus
            );

            btnEdit = itemView.findViewById(
                    R.id.btnItemEdit
            );

            btnDelete = itemView.findViewById(
                    R.id.btnItemDelete
            );
        }
    }
}