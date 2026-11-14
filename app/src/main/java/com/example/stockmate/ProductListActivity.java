package com.example.stockmate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProductListActivity extends AppCompatActivity
        implements ProductAdapter.OnProductActionListener {

    RecyclerView recyclerViewProducts;
    TextView tvEmpty;
    Button btnAddFromList;

    DBHelper dbHelper;
    ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product_list);

        // Connect XML components
        recyclerViewProducts = findViewById(
                R.id.recyclerViewProducts
        );

        tvEmpty = findViewById(R.id.tvEmpty);
        btnAddFromList = findViewById(R.id.btnAddFromList);

        // Initialize SQLite database
        dbHelper = new DBHelper(this);

        // RecyclerView LayoutManager
        recyclerViewProducts.setLayoutManager(
                new LinearLayoutManager(this)
        );

        // Initialize Adapter
        adapter = new ProductAdapter(
                new ArrayList<>(),
                this
        );

        recyclerViewProducts.setAdapter(adapter);

        // Add Product Button
        btnAddFromList.setOnClickListener(v -> {
            Intent intent = new Intent(
                    ProductListActivity.this,
                    AddEditProductActivity.class
            );

            startActivity(intent);
        });
    }

    // Refresh records whenever Activity becomes visible
    @Override
    protected void onResume() {
        super.onResume();

        if (dbHelper != null && adapter != null) {
            loadProducts();
        }
    }

    // Retrieve products from SQLite
    private void loadProducts() {

        ArrayList<Product> products =
                dbHelper.getAllProducts();

        adapter.updateProducts(products);

        // Show message if database is empty
        if (products.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            tvEmpty.setVisibility(View.GONE);
        }
    }

    // Edit Product
    @Override
    public void onEdit(Product product) {

        Intent intent = new Intent(
                ProductListActivity.this,
                AddEditProductActivity.class
        );

        // Pass selected Product ID
        intent.putExtra("product_id", product.getId());

        startActivity(intent);
    }

    // Delete Product
    @Override
    public void onDelete(Product product) {

        new AlertDialog.Builder(this)
                .setTitle("Delete Product")
                .setMessage(
                        "Are you sure you want to delete " +
                                product.getName() + "?"
                )
                .setPositiveButton("Delete", (dialog, which) -> {

                    boolean success =
                            dbHelper.deleteProduct(product.getId());

                    if (success) {

                        Toast.makeText(
                                this,
                                "Product deleted successfully!",
                                Toast.LENGTH_SHORT
                        ).show();

                        loadProducts();

                    } else {

                        Toast.makeText(
                                this,
                                "Failed to delete product",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}