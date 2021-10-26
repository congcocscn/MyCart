package hanu.a2_1801040024;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import hanu.a2_1801040024.adapter.CartAdapter;
import hanu.a2_1801040024.db.ProductManager;
import hanu.a2_1801040024.model.Products;

public class CartActivity extends AppCompatActivity {

    private RecyclerView rv_item_cart;
    private TextView totalPrice;
    private ImageButton checkoutBt;
    private List<Products> products;
    private CartAdapter cartAdapter;
    private ProductManager productManager;
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        builder = new AlertDialog.Builder(this);
        //bottom checkout navigation
        checkoutBt = findViewById(R.id.checkoutBt);
        refreshData(this);

        checkoutBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setCancelable(true);
                builder.setTitle("Do you want to buy them?");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "purchase done", Toast.LENGTH_SHORT).show();
                                productManager.deleteAll();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }
                        });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    public void refreshData(Context context){
        int total = 0;
        totalPrice = findViewById(R.id.totalPrice);
        //recycler view
        rv_item_cart =findViewById(R.id.rv_item_cart);
        //dataset
        productManager= ProductManager.getInstance(this);
        products =productManager.all();
        // get total price of cart
        for(Products pr: products){
            total += pr.getAmount() * pr.getUnitPrice();
        }

        //adapter
        cartAdapter = new CartAdapter(products, this);
        if (cartAdapter.getItemCount() == 0){
            totalPrice.setText("0 ₫");
        } else{
            totalPrice.setText(String.valueOf(total) + " ₫");
        }
        rv_item_cart.setAdapter(cartAdapter);
        rv_item_cart.setLayoutManager(new LinearLayoutManager(this));
    }

}