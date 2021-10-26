package hanu.a2_1801040024.db;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.ArrayList;
import java.util.List;

import hanu.a2_1801040024.model.Products;

public class ProductCursorWrapper extends CursorWrapper {
    public ProductCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Products getProduct() {
        int id = getInt(getColumnIndex(DbSchema.ProductsTable.Cols.ID));
        String thumbnail = getString(getColumnIndex(DbSchema.ProductsTable.Cols.THUMBNAIL));
        String name = getString(getColumnIndex(DbSchema.ProductsTable.Cols.NAME));
        int unitPrice = getInt(getColumnIndex(DbSchema.ProductsTable.Cols.UNITPRICE));
        int amount = getInt(getColumnIndex(DbSchema.ProductsTable.Cols.AMOUNT));

        Products product = new Products(id, thumbnail, name, unitPrice, amount);

        return product;
    }

    public List<Products> getProducts() {
        List<Products> products = new ArrayList<>();
        moveToFirst();
        while (!isAfterLast()) {
            Products pr = getProduct();
            products.add(pr);
            moveToNext();
        }
        return products;
    }
}
