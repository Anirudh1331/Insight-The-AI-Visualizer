package com.example.easylearn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.easylearn.Adapters.FeaturedAdapter;
import com.example.easylearn.CommonClasses.DisplayData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class RecommededItems extends AppCompatActivity {
    RecyclerView recyclerView;
    Button hide,upload;
    EditText search_amazon;
    ImageButton cart_send;
    LinearLayout ll_recommend;
    String ss;

    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    String sImg="";
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

        search_amazon.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int sz=charSequence.length();
                if(sz!=0){
                    cart_send.setImageResource(R.drawable.send);
                }
                else{
                    cart_send.setImageResource(R.drawable.cart);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(RecommededItems.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
                }
                else{
                    openGallery();
                }

            }
        });

        cart_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s=search_amazon.getText().toString();
                if(s.length()==0){
                    Intent in=new Intent(getApplicationContext().getApplicationContext(), Cart.class);
                    startActivity(in);
                }
                else{
                    if(s.contains("chair") || s.contains("Chair"))
                        post_upload("0");
                    else if(s.contains("couch") || s.contains("Couch") || s.contains("sofa") || s.contains("Sofa"))
                        post_upload("1");
                    else{
                        Toast.makeText(getApplicationContext(), "Please Go with Chair, Sofa and Couch", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void openGallery() {
        AlertDialog.Builder builder=new AlertDialog.Builder(RecommededItems.this);
        builder.setTitle("Add Photo");
        builder.setPositiveButton("Choose From Gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                dialog.cancel();
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);

            }
        });
        builder.setNegativeButton("Take Photo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                dialog.cancel();
                Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                dialog.cancel();
            }
        });
        builder.create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RecommededItems.RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            try{
                Bitmap bitmap=MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),imageUri);
                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                byte[] bytes=stream.toByteArray();
                sImg= Base64.encodeToString(bytes,Base64.DEFAULT);
//                search_amazon.setText("helo");
                Log.e("check",sImg);
                Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
                upload.setText("Uploaded");

            }catch (IOException e){
                e.printStackTrace();
            }
        }
        else if(resultCode == RecommededItems.RESULT_OK && requestCode == 0 && data!=null){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream stream=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
            byte[] bytes=stream.toByteArray();
            sImg= Base64.encodeToString(bytes,Base64.DEFAULT);
//                search_amazon.setText("helo");
            Log.e("check",sImg);
            Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
            upload.setText("Image Uploaded");
        }
    }

    public void post_upload(String val){

        AlertDialog.Builder builder1 = new AlertDialog.Builder(RecommededItems.this);
        builder1.setMessage("Wait while data is being fetched");
        AlertDialog alert11 = builder1.create();
        alert11.show();

        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        String URL="http://amazonaadn.centralindia.cloudapp.azure.com:421/result";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response",response);
                alert11.cancel();
                if(response.equalsIgnoreCase("all") || response.equalsIgnoreCase("")){
                    response="";
                    if(val.equalsIgnoreCase("0")){
                        for(int i=1;i<34;i++){
                            response+=String.valueOf(i)+",";
                        }
                        response+=String.valueOf(34);
                    }
                    else{
                        for(int i=35;i<51;i++){
                            response+=String.valueOf(i)+",";
                        }
                        response+=String.valueOf(51);
                    }
                }
                Intent in=new Intent(getApplicationContext(),RecommededItems.class);
                in.putExtra("response",response);
                startActivity(in);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error displaying recommendation!! Please try again", Toast.LENGTH_LONG).show();
                Log.e("error",error.toString());
                alert11.cancel();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> param = new HashMap<>();
                param.put("image",sImg);
                param.put("item",val);
                return param;
            }
        };
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 600000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        queue.add(stringRequest);
    }
}