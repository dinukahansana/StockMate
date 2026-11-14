package com.example.stockmate;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddEditProductActivity extends AppCompatActivity {

    // UI components
    EditText etProductName, etCategory, etPrice, etQuantity;
    Button btnSaveProduct, btnCancel;
    TextView tvFormTitle;

    // SQLite database helper
    DBHelper dbHelper;
    int productId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_edit_product);

        // Connect Java with XML
        etProductName = findViewById(R.id.etProductName);
        etCategory = findViewById(R.id.etCategory);
        etPrice = findViewById(R.id.etPrice);
        etQuantity = findViewById(R.id.etQuantity);

        btnSaveProduct = findViewById(R.id.btnSaveProduct);
        btnCancel = findViewById(R.id.btnCancel);

        tvFormTitle = findViewById(R.id.tvFormTitle);

        // Initialize SQLite database
        dbHelper = new DBHelper(this);

        // Get the ID passed from another Activity
        productId = getIntent().getIntExtra("product_id", -1);

        // If a product ID exists, load it for editing
        if (productId != -1) {

            Product product = dbHelper.getProductById(productId);

            if (product == null) {
                Toast.makeText(this,
                        "Product not found",
                        Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            tvFormTitle.setText("Edit Product");
            btnSaveProduct.setText("Update Product");

            etProductName.setText(product.getName());
            etCategory.setText(product.getCategory());
            etPrice.setText(String.valueOf(product.getPrice()));
            etQuantity.setText(String.valueOf(product.getQuantity()));
        }

        // Save Product Button
        btnSaveProduct.setOnClickListener(v -> saveProduct());

        // Cancel Button
        btnCancel.setOnClickListener(v -> finish());
    }

    private void saveProduct() {

        // Read input values
        String name = etProductName.getText().toString().trim();
        String category = etCategory.getText().toString().trim();
        String priceText = etPrice.getText().toString().trim();
        String quantityText = etQuantity.getText().toString().trim();

        // Validate Product Name
        if (name.isEmpty()) {
            etProductName.setError("Enter product name");
            etProductName.requestFocus();
            return;
        }

        // Validate Category
        if (category.isEmpty()) {
            etCategory.setError("Enter category");
            etCategory.requestFocus();
            return;
        }

        // Validate Price
        if (priceText.isEmpty()) {
            etPrice.setError("Enter price");
            etPrice.requestFocus();
            return;
        }

        // Validate Quantity
        if (quantityText.isEmpty()) {
            etQuantity.setError("Enter quantity");
            etQuantity.requestFocus();
            return;
        }

        double price;
        int quantity;

        // Convert Price to double
        try {
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            etPrice.setError("Enter a valid price");
            etPrice.requestFocus();
            return;
        }

        // Price must be greater than zero
        if (price <= 0 || Double.isNaN(price)
                || Double.isInfinite(price)) {
            etPrice.setError("Enter a valid positive price");
            etPrice.requestFocus();
            return;
        }

        // Convert Quantity to integer
        try {
            quantity = Integer.parseInt(quantityText);
        } catch (NumberFormatException e) {
            etQuantity.setError("Enter a valid quantity");
            etQuantity.requestFocus();
            return;
        }

        // Quantity cannot be negative
        if (quantity < 0) {
            etQuantity.setError("Quantity cannot be negative");
            etQuantity.requestFocus();
            return;
        }

        // Create Product object
        Product product = new Product(
                productId,
                name,
                category,
                price,
                quantity
        );

        boolean success;

        // Create or Update
        if (productId == -1) {
            success = dbHelper.insertProduct(product);
        } else {
            success = dbHelper.updateProduct(product);
        }

        // Show operation result
        if (success) {

            String message = (productId == -1)
                    ? "Product added successfully!"
                    : "Product updated successfully!";

            Toast.makeText(
                    this,
                    message,
                    Toast.LENGTH_SHORT
            ).show();

            // Return to previous Activity
            finish();

        } else {

            Toast.makeText(
                    this,
                    "Operation failed!",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}