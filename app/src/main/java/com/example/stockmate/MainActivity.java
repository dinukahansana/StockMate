package com.example.stockmate;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    // Declare UI components
    TextView tvTotalProducts, tvTotalUnits;
    TextView tvInventoryValue, tvLowStock;

    Button btnAddProduct, btnViewProducts;

    // Database Helper
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Connect Java with XML components
        tvTotalProducts = findViewById(R.id.tvTotalProducts);
        tvTotalUnits = findViewById(R.id.tvTotalUnits);
        tvInventoryValue = findViewById(R.id.tvInventoryValue);
        tvLowStock = findViewById(R.id.tvLowStock);

        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnViewProducts = findViewById(R.id.btnViewProducts);

        // Initialize SQLite database
        dbHelper = new DBHelper(this);

        // Add Product Button
        btnAddProduct.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    AddEditProductActivity.class
            );

            startActivity(intent);
        });

        // View Products Button
        btnViewProducts.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    ProductListActivity.class
            );

            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (dbHelper != null) {
            loadDashboard();
        }
    }

    // Load inventory statistics
    private void loadDashboard() {

        ArrayList<Product> products =
                dbHelper.getAllProducts();

        int totalProducts = products.size();
        int totalUnits = 0;
        int lowStock = 0;

        double inventoryValue = 0;

        for (Product product : products) {

            totalUnits += product.getQuantity();

            inventoryValue += product.getStockValue();

            // Products with 1-5 units are low stock
            if (product.getQuantity() > 0 &&
                    product.getQuantity() <= 5) {
                lowStock++;
            }
        }

        // Display statistics
        tvTotalProducts.setText(
                String.valueOf(totalProducts)
        );

        tvTotalUnits.setText(
                String.valueOf(totalUnits)
        );

        tvLowStock.setText(
                String.valueOf(lowStock)
        );

        tvInventoryValue.setText(
                String.format(
                        Locale.getDefault(),
                        "Rs. %,.2f",
                        inventoryValue
                )
        );
    }
}