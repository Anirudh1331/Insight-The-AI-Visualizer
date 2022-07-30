package com.example.easylearn.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easylearn.CommonClasses.DisplayData;
import com.example.easylearn.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    Context context;
    ArrayList<DisplayData> arr;
    Set<String> adj=new HashSet<>();
    int i=0;

    public CartAdapter(Context context, ArrayList<DisplayData> arr) {
        this.context = context;
        this.arr = arr;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cart_card,parent,false);
        return new CartAdapter.CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        DisplayData dp=arr.get(position);
        Picasso.get().load(dp.getImage_links()).into(holder.imageView);
        holder.name.setText(dp.getName());
        holder.price.setText("₹ "+String.valueOf(dp.getCost()));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences=context.getSharedPreferences("mypref", Context.MODE_PRIVATE);
                Set<String> s=sharedPreferences.getStringSet("hello",null);
                s.remove(String.valueOf(dp.getId()));
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putStringSet("hello",s);
                editor.commit();
                arr.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                notifyItemRangeChanged(holder.getAdapterPosition(), arr.size());
            }
        });
        holder.visualize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(adj.contains(String.valueOf(dp.getId()))) {
                    adj.remove(String.valueOf(dp.getId()));
                    holder.visualize.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                    i--;
                }
                else if(i<4){
                    holder.visualize.setImageResource(R.drawable.ic_baseline_visibility_24);
                    adj.add(String.valueOf(dp.getId()));
                    i++;
                }
                else{
                    Toast.makeText(context, "max 4 are allowed", Toast.LENGTH_SHORT).show();
                }
                SharedPreferences sharedpreferences = context.getSharedPreferences("mypref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putStringSet("visualize", adj);
                editor.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name;
        TextView price;
        ImageButton delete,visualize;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image_cart);
            name=itemView.findViewById(R.id.name_cart);
            price=itemView.findViewById(R.id.price_cart);
            delete=itemView.findViewById(R.id.remove_cart);
            visualize=itemView.findViewById(R.id.visualize_cart);
        }
    }
}
