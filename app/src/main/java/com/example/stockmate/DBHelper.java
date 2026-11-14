
package com.example.stockmate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "StockMate.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE = "products";

    // Constructor
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create Database Table
    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE " + TABLE + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "category TEXT NOT NULL, " +
                "price REAL NOT NULL CHECK(price >= 0), " +
                "quantity INTEGER NOT NULL CHECK(quantity >= 0))";

        db.execSQL(sql);
    }

    // Database Version Upgrade
    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion, int newVersion) {

    }

    // Prepare product values for database operations
    private ContentValues valuesFor(Product product) {

        ContentValues values = new ContentValues();

        values.put("name", product.getName());
        values.put("category", product.getCategory());
        values.put("price", product.getPrice());
        values.put("quantity", product.getQuantity());

        return values;
    }

    // CREATE - Insert a new product
    public boolean insertProduct(Product product) {

        SQLiteDatabase db = getWritableDatabase();

        long result = db.insert(
                TABLE, null, valuesFor(product)
        );

        return result != -1;
    }

    // READ - Retrieve all products
    public ArrayList<Product> getAllProducts() {

        ArrayList<Product> products = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id, name, category, price, quantity " +
                        "FROM " + TABLE + " ORDER BY id DESC",
                null
        );

        try {
            while (cursor.moveToNext()) {
                products.add(readProduct(cursor));
            }
        } finally {
            cursor.close();
        }

        return products;
    }

    // READ - Retrieve a single product using ID
    public Product getProductById(int id) {

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id, name, category, price, quantity " +
                        "FROM " + TABLE + " WHERE id = ?",
                new String[]{String.valueOf(id)}
        );

        try {
            if (cursor.moveToFirst()) {
                return readProduct(cursor);
            }
            return null;
        } finally {
            cursor.close();
        }
    }

    // Convert database row into Product object
    private Product readProduct(Cursor cursor) {

        return new Product(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getDouble(3),
                cursor.getInt(4)
        );
    }

    // UPDATE - Modify an existing product
    public boolean updateProduct(Product product) {

        SQLiteDatabase db = getWritableDatabase();

        int result = db.update(
                TABLE,
                valuesFor(product),
                "id = ?",
                new String[]{String.valueOf(product.getId())}
        );

        return result > 0;
    }

    // DELETE - Remove a product
    public boolean deleteProduct(int id) {

        SQLiteDatabase db = getWritableDatabase();

        int result = db.delete(
                TABLE,
                "id = ?",
                new String[]{String.valueOf(id)}
        );

        return result > 0;
    }
}