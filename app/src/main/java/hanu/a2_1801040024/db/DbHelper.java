package hanu.a2_1801040024.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "products.db";
    private static final int DATABASE_VERSION = 2;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DbSchema.ProductsTable.NAME + "(" +
                DbSchema.ProductsTable.Cols.KEYID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DbSchema.ProductsTable.Cols.ID + " INTEGER, " +
                DbSchema.ProductsTable.Cols.THUMBNAIL + " TEXT, " +
                DbSchema.ProductsTable.Cols.NAME + " TEXT, " +
                DbSchema.ProductsTable.Cols.UNITPRICE + " TEXT, " +
                DbSchema.ProductsTable.Cols.AMOUNT + " TEXT" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // delete this db if it exist before create new
        db.execSQL("DROP TABLE IF EXISTS " + DbSchema.ProductsTable.NAME);
        onCreate(db);
    }
}