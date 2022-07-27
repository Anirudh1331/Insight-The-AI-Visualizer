package com.example.easylearn.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.easylearn.MainActivity;
import com.example.easylearn.R;

public class frag2 extends Fragment {
    Button upload_image;
    EditText search_amazon;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    ImageButton cart_send;
    public frag2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_frag2, container, false);
        upload_image=view.findViewById(R.id.upload_Image);
        cart_send=view.findViewById(R.id.cart_send);
        search_amazon=view.findViewById(R.id.search_amazon);
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
                openGallery();
            }
        });
        cart_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(getActivity().getApplicationContext(), MainActivity.class);
                startActivity(in);
            }
        });
        return view;
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
        }
    }
}