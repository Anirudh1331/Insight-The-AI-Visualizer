package com.example.easylearn.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class FeaturedAdapter extends RecyclerView.Adapter<FeaturedAdapter.FeaturedViewHolder> {

    Context context;
    ArrayList<DisplayData> arr;
    Set<String> adj=new HashSet<>();
    int i=1;
    public FeaturedAdapter(Context context, ArrayList<DisplayData> arr) {
        this.context = context;
        this.arr = arr;
    }


    @NonNull
    @Override
    public FeaturedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recommed_cards,parent,false);
        return new FeaturedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedViewHolder holder, int position) {
        DisplayData dp=arr.get(position);
        Picasso.get().load(dp.getImage_links()).into(holder.imageView);
        Picasso.get().setLoggingEnabled(true);
//        Log.e("verify",dp.getImage_links());
        holder.name.setText(dp.getName());

        holder.price.setText("₹ "+String.valueOf(dp.getCost()));
        holder.addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.addCart.setText("Added to Cart");
                adj.add(String.valueOf(dp.getId()));
                SharedPreferences sharedpreferences = context.getSharedPreferences("mypref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putStringSet("hello",adj);
                editor.commit();
//                Toast.makeText(context, "Clicked"+i, Toast.LENGTH_SHORT).show();
                i++;
            }
        });
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    public class FeaturedViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name,addCart;
        TextView price;
        public FeaturedViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image_viewAll);
            name=itemView.findViewById(R.id.textview_viewAll);
            addCart=itemView.findViewById(R.id.add_cart_viewAll);
            price=itemView.findViewById(R.id.price_viewAll);
        }

    }
}
