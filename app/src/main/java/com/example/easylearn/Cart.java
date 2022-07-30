package com.example.easylearn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.easylearn.Adapters.CartAdapter;
import com.example.easylearn.Adapters.FeaturedAdapter;
import com.example.easylearn.CommonClasses.DisplayData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Cart extends AppCompatActivity {
    RecyclerView recyclerView;
    Button visualize;

    @Override
    protected void onStart() {
        super.onStart();
        Set<String> adj=new HashSet<>();
        SharedPreferences sharedpreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putStringSet("visualize", adj);
        editor.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        recyclerView=findViewById(R.id.recycler_cart);
        visualize=findViewById(R.id.final_visualize);
        SharedPreferences sharedPreferences=getSharedPreferences("mypref", Context.MODE_PRIVATE);
        Set<String> s=sharedPreferences.getStringSet("hello",null);
        ArrayList<Integer> ids=new ArrayList<>();
        for (String str : s){
            ids.add(Integer.parseInt(str));
        }
        ArrayList<DisplayData> arrayList=new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        CartAdapter cartAdapter=new CartAdapter(getApplicationContext(),arrayList);
        recyclerView.setAdapter(cartAdapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Items");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                arrayList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    try {
                        if (ids.contains(dataSnapshot1.getValue(DisplayData.class).getId())) {
                            DisplayData displayData = new DisplayData(dataSnapshot1.getValue(DisplayData.class).getId(), dataSnapshot1.getValue(DisplayData.class).getImage_links(), dataSnapshot1.getValue(DisplayData.class).getModel_links(), dataSnapshot1.getValue(DisplayData.class).getName(), dataSnapshot1.getValue(DisplayData.class).getCost());
                            arrayList.add(displayData);
                        }
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(), "error adding this file", Toast.LENGTH_SHORT).show();
                    }
                }
                cartAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(getApplicationContext(), ""+error, Toast.LENGTH_SHORT).show();
            }
        });
        visualize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences=getSharedPreferences("mypref", Context.MODE_PRIVATE);
                Set<String> ss=sharedPreferences.getStringSet("visualize",null);
                Log.e("cards", String.valueOf(ss));
                if(ss.size()==0){
                    Toast.makeText(Cart.this, "Please add at least 1 product to visualize", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent in=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(in);
                }
            }
        });
    }
}