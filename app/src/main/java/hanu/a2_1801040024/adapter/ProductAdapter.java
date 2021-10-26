package hanu.a2_1801040024.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;

import java.util.List;

import hanu.a2_1801040024.CartActivity;
import hanu.a2_1801040024.MainActivity;
import hanu.a2_1801040024.R;
import hanu.a2_1801040024.db.ProductCursorWrapper;
import hanu.a2_1801040024.db.ProductManager;
import hanu.a2_1801040024.model.Products;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {
    private List<Products> products;
    private Context context;
    private ProductManager productManager;
    private int RESULTCODE = 2;

    public ProductAdapter(List<Products> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        productManager = ProductManager.getInstance(context);
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.product_item, parent, false);
        return new ProductHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, final int position) {
        Products product = this.products.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return this.products.size();
    }

    public class ProductHolder extends RecyclerView.ViewHolder {

        private ImageView thumbnail;
        private TextView name, unitPrice;
        private ImageButton addToCartBt;

        public ProductHolder(@NonNull View itemView, final Context context) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            name = itemView.findViewById(R.id.name);
            unitPrice = itemView.findViewById(R.id.unitPrice);
            addToCartBt = itemView.findViewById(R.id.addToCartBt);
        }

        public void bind(Products product) {
            Picasso.get().load(product.getThumbnail()).into(thumbnail);
            name.setText(product.getName());
            unitPrice.setText(String.valueOf(product.getUnitPrice()) + " ₫");


            addToCartBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int amount = productManager.getAmountByID(product.getId());
                    amount++;
                    //add to db
                    productManager.add(product, amount);
                    //add to instance
                    if (!products.contains(product)){
                        products.add(product);
                    } else{
                        product.setAmount(amount);
                        products.set(products.indexOf(product), product);
                    }
                    ((MainActivity)context).setupBadge();
                    notifyDataSetChanged();
                    Toast.makeText(context, "Product added successfully!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
