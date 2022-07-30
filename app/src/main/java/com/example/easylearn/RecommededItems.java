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
import com.example.easylearn.Adapters.FeaturedAdapter;
import com.example.easylearn.CommonClasses.DisplayData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


public class RecommededItems extends AppCompatActivity {
    RecyclerView recyclerView;
    Button hide,upload;
    EditText search_amazon;
    ImageButton cart_send;
    LinearLayout ll_recommend;
    String ss;

    @Override
    protected void onStart() {
        ll_recommend.setVisibility(View.VISIBLE);
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommeded_items);
        recyclerView=findViewById(R.id.recycler_recommend);
        hide=findViewById(R.id.hide_recommend);
        upload=findViewById(R.id.upload_recommend);
        search_amazon=findViewById(R.id.search_amazon_recommed);
        cart_send=findViewById(R.id.cart_send_recommend);
        ll_recommend=findViewById(R.id.ll_recommend);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ss = extras.getString("response");
        }
        hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_recommend.setVisibility(View.GONE);
            }
        });
        ArrayList<Integer> ids=new ArrayList<>();
        String[] array = ss.split(",");
        for(String value:array) {
            ids.add(Integer.parseInt(value));
        }
        Log.e("helo", ss);
        ArrayList<DisplayData> arrayList=new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Items");
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        FeaturedAdapter featuredAdapter=new FeaturedAdapter(getApplicationContext(),arrayList);
        recyclerView.setAdapter(featuredAdapter);
        // Read from the database
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
                        Toast.makeText(RecommededItems.this, "error", Toast.LENGTH_SHORT).show();
                    }
                }
                featuredAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(RecommededItems.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        });
        cart_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(getApplicationContext(),Cart.class);
                startActivity(in);
            }
        });

    }
}