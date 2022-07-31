package com.example.easylearn.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.easylearn.Cart;
import com.example.easylearn.MainActivity;
import com.example.easylearn.R;
import com.example.easylearn.RecommededItems;

import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class frag2 extends Fragment {
    Button upload_image;
    EditText search_amazon;
    ImageView imageView;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    ImageButton cart_send;
    String sImg="";

    public frag2() {
        // Required empty public constructor
    }

    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_frag2, container, false);
        upload_image=view.findViewById(R.id.upload_Image_frag2);
        cart_send=view.findViewById(R.id.cart_send_frag2);
        imageView=view.findViewById(R.id.furniture);
        search_amazon=view.findViewById(R.id.search_amazon_frag2);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post_upload("0");
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
        upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
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
                    Intent in=new Intent(getActivity().getApplicationContext(), Cart.class);
                    startActivity(in);
                }
                else{
                    if(s.contains("chair") || s.contains("Chair"))
                        post_upload("0");
                    else if(s.contains("couch") || s.contains("Couch") || s.contains("sofa") || s.contains("Sofa"))
                        post_upload("1");
                    else{
                        Toast.makeText(getActivity(), "Please Go with Chair, Sofa and Couch", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return view;
    }

    private void openGallery() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
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
                checkAndRequestPermissions(getActivity());
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
//
    public static boolean checkAndRequestPermissions(final Activity context) {
        int WExtstorePermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (WExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(context, listPermissionsNeeded
                            .toArray(new String[listPermissionsNeeded.size()]),
                    101);
            return false;
        }
        return true;
    }
    // Handled permission Result
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(),
                                    "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT)
                            .show();
                } else if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(),
                            "FlagUp Requires Access to Your Storage.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    openGallery();
                }
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            try{
                Bitmap bitmap=MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),imageUri);
                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                byte[] bytes=stream.toByteArray();
                sImg= Base64.encodeToString(bytes,Base64.DEFAULT);
//                search_amazon.setText("helo");
                Log.e("check",sImg);
                Toast.makeText(getActivity(), "Image Uploaded", Toast.LENGTH_SHORT).show();
                upload_image.setText("Image Uploaded");

            }catch (IOException e){
                e.printStackTrace();
            }
        }
        else if(resultCode == getActivity().RESULT_OK && requestCode == 0 && data!=null){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream stream=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
            byte[] bytes=stream.toByteArray();
            sImg= Base64.encodeToString(bytes,Base64.DEFAULT);
//                search_amazon.setText("helo");
            Log.e("check",sImg);
            Toast.makeText(getActivity(), "Image Uploaded", Toast.LENGTH_SHORT).show();
            upload_image.setText("Image Uploaded");
        }
    }

    public void post_upload(String val){

        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Wait while data is being fetched");
        AlertDialog alert11 = builder1.create();
        alert11.show();

        RequestQueue queue= Volley.newRequestQueue(getActivity());
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
                Intent in=new Intent(getActivity(),RecommededItems.class);
                in.putExtra("response",response);
                startActivity(in);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Error displaying recommendation!! Please try again", Toast.LENGTH_LONG).show();
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
