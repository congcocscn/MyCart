package hanu.a2_1801040024.db;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.List;

import hanu.a2_1801040024.model.Products;

public class ProductManager {
    private static ProductManager instance;

    private DbHelper dbHelper;
    private SQLiteDatabase db;

    public static ProductManager getInstance(Context context) {
        if (instance == null) {
            instance = new ProductManager(context);
        }
        return instance;
    }

    private ProductManager(Context context) {
        dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public long getProfileCount(){
        String sql = "SELECT COUNT(*) FROM " + DbSchema.ProductsTable.NAME;
        SQLiteStatement statement = db.compileStatement(sql);
        long count = statement.simpleQueryForLong();
        return count;
    }

    // get all data
    public List<Products> all() {
        String sql = "SELECT * FROM products";
        Cursor cursor = db.rawQuery(sql, null);
        ProductCursorWrapper cursorWrapper = new ProductCursorWrapper(cursor);
        return cursorWrapper.getProducts();
    }

    /**
     * @overview: check product existed in db or not
     * @param products
     * @return the amount of product or return -1 if this is not exist
     */
    public int checkProductExist(Products products){
        try{
            Cursor cursor = db.rawQuery("SELECT amount from "+ DbSchema.ProductsTable.NAME +" where id = ?",
                    new String[]{products.getId() + ""});
            cursor.moveToFirst();
            return cursor.getCount();
        }catch (SQLException e){
            Log.e("Error", "Fail to load data from db: " + e.getMessage());
            return -1;
        }
    }

    /**
     * @overview: delete product by id
     * @param id
     * @return
     */
    public boolean delete(int id) {
        int result = db.delete(DbSchema.ProductsTable.NAME, "id = ?", new String[]{ id+"" });
        return result > 0;
    }

    public boolean deleteAll(){
        int result = db.delete(DbSchema.ProductsTable.NAME, null, null);
        return result >0;
    }

    /**
     * @overview: add new product or change amount if it exist
     * @param products
     * @param amount
     */
    public void add(Products products, int amount){

        int checkCode = checkProductExist(products);

        if(checkCode == 0){
            final String insertData = "INSERT INTO "+ DbSchema.ProductsTable.NAME +" (id, thumbnail, name, unitPrice, amount) VALUES (?, ?, ?, ?, ?)";
            try{
                SQLiteStatement statement = db.compileStatement(insertData);
                statement.bindLong(1, products.getId());
                statement.bindString(2, products.getThumbnail());
                statement.bindString(3, products.getName());
                statement.bindLong(4, products.getUnitPrice());
                statement.bindLong(5, 1);

                statement.executeInsert();
            }catch (SQLException e){
                Log.e("Error", "Fail to add product ");
            }
        } else if(checkCode == -1){
            Log.e("Error", "Fail to add this product into db");
        } else{
            setAmountDb(products, amount);
        }
    }

    /**
     * @overview: set amount of this.product(update)
     * @param products
     * @param amount
     */
    public void setAmountDb(Products products, int amount){
        final String updateAmount = "UPDATE "+ DbSchema.ProductsTable.NAME +" SET amount = ? WHERE id = ?";
        try{
            SQLiteStatement statement = db.compileStatement(updateAmount);

            statement.bindLong(1, amount);
            statement.bindLong(2, products.getId());
            statement.executeUpdateDelete();
        }catch (SQLException e){
            Log.e("Error", e.getMessage());
        }
    }

    /**
     * @overview: get amount of this.product by id
     * @param ID
     * @return
     */
    public int getAmountByID(int ID) {
        int amount = -1;
        Cursor cursor = db.rawQuery("SELECT amount from "+ DbSchema.ProductsTable.NAME +" where id = ?",
                new String[]{ID + ""});

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            amount = cursor.getInt(cursor.getColumnIndex("amount"));
        }
        cursor.close();
        return amount;
    }

}
