package hanu.a2_1801040024.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;

import java.util.List;

import hanu.a2_1801040024.CartActivity;
import hanu.a2_1801040024.R;
import hanu.a2_1801040024.db.ProductManager;
import hanu.a2_1801040024.model.Products;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolder> {
    private List<Products> products;
    private Context context;

    public CartAdapter(List<Products> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.checkout_product_item, parent, false);

        return new CartHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull CartHolder holder, final int position) {
        Products product = this.products.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return this.products.size();
    }

    public class CartHolder extends RecyclerView.ViewHolder {

        private ImageView checkout_thumbnail;
        private TextView checkout_name, checkout_unitPrice, checkout_totalPrice;
        private EditText product_number;
        private ImageButton plusBt, minusBt;

        public CartHolder(@NonNull View itemView, final Context context) {
            super(itemView);
            checkout_thumbnail = itemView.findViewById(R.id.checkout_thumbnail);
            checkout_name = itemView.findViewById(R.id.checkout_name);
            checkout_unitPrice = itemView.findViewById(R.id.checkout_unitPrice);
            checkout_totalPrice = itemView.findViewById(R.id.checkout_totalPrice);
            plusBt = itemView.findViewById(R.id.plusBt);
            product_number = itemView.findViewById(R.id.product_number);
            minusBt = itemView.findViewById(R.id.minusBt);
        }

        public void bind(Products product) {
            ProductManager productManager = ProductManager.getInstance(context);

            Picasso.get().load(product.getThumbnail()).into(checkout_thumbnail);
            checkout_name.setText(product.getName());
            checkout_unitPrice.setText(String.valueOf(product.getUnitPrice()) + " ₫");
            product_number.setText(String.valueOf(product.getAmount()));
            checkout_totalPrice.setText(String.valueOf(product.getUnitPrice() * product.getAmount()) + " ₫");

            product_number.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    int num = Integer.parseInt(String.valueOf(product_number.getText()));
                    product_number.setImeActionLabel("OK", KeyEvent.KEYCODE_ENTER);

                    if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_GO)) {
                        if (num > 0) {
                            productManager.setAmountDb(product, num);
                            product.setAmount(num);
                        } else {
                            productManager.delete(product.getId());
                            products.remove(product);
                        }

                        product_number.clearFocus();
                        product_number.setCursorVisible(false);

                        notifyDataSetChanged();
                        ((CartActivity)context).refreshData(context);

                    }
                    return false;
                }
            });

            plusBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int amount = product.getAmount();
                    amount++;
                    productManager.setAmountDb(product, amount);
                    product.setAmount(amount);

                    notifyDataSetChanged();
                    ((CartActivity)context).refreshData(context);
                }
            });

            minusBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int product_count = product.getAmount();
                    if (product_count == 1) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setCancelable(true);
                        builder.setTitle("Do you want to remove this item?");
                        builder.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // delete cart
                                        productManager.delete(product.getId());
                                        products.remove(product);

                                        notifyDataSetChanged();
                                        ((CartActivity)context).refreshData(context);
                                    }
                                });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    } else {
                        int amount = product.getAmount();
                        amount--;
                        productManager.setAmountDb(product, amount);
                        product.setAmount(amount);

                        notifyDataSetChanged();
                        ((CartActivity)context).refreshData(context);

                    }
                }
            });
        }
    }
}