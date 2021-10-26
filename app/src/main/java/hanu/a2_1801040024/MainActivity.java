package hanu.a2_1801040024;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import hanu.a2_1801040024.adapter.CartAdapter;
import hanu.a2_1801040024.adapter.ProductAdapter;
import hanu.a2_1801040024.api.RestAPI;
import hanu.a2_1801040024.db.ProductManager;
import hanu.a2_1801040024.model.Products;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView smsCountTxt;
    private List<Products> products;
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.products = products;
        this.productAdapter = productAdapter;
        setContentView(R.layout.activity_main);

        /**
         * Call API, get data, save into 'products'
         */
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://mpr-cart-api.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RestAPI jsonData = retrofit.create(RestAPI.class);

        Call<List<Products>> call = jsonData.getProducts();

        call.enqueue(new Callback<List<Products>>() {
            @Override
            public void onResponse(Call<List<Products>> call, Response<List<Products>> response) {
                if (!response.isSuccessful()) {
                    Log.e("Error", "get data fail");
                    return;
                }

                //Get all data from API
                products = response.body();
                setRecyclerView(products);
            }
            @Override
            public void onFailure(Call<List<Products>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Fail to load data from API. Please check the network connection!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupBadge();
    }

    public void setRecyclerView(List<Products> products){
        //dataset
        recyclerView = findViewById(R.id.rvProduct);

        //adapter
        productAdapter = new ProductAdapter(products, getApplicationContext()); // get all data from api

        //recycler view
        recyclerView.setAdapter(productAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_notifications);

        View actionView = MenuItemCompat.getActionView(menuItem);
        smsCountTxt = (TextView) actionView.findViewById(R.id.notification_badge);

        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_notifications: {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void setupBadge() {
        ProductManager productManager = ProductManager.getInstance(this);
        long count = productManager.getProfileCount();
        if (smsCountTxt != null) {
            if (count == 0) {
                if (smsCountTxt.getVisibility() != View.GONE) {
                    smsCountTxt.setVisibility(View.GONE);
                }
            } else {
                smsCountTxt.setText(String.valueOf(Math.min(count, 99)));
                if (smsCountTxt.getVisibility() != View.VISIBLE) {
                    smsCountTxt.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}