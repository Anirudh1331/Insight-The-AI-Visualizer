package com.example.easylearn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easylearn.CommonClasses.DisplayData;
import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private ArFragment arFragment;
    private ModelRenderable modelRenderable;
    ImageButton b1,b2,b3,b4;
    TextView clear_all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1=findViewById(R.id.btn1);
        b2=findViewById(R.id.btn2);
        b3=findViewById(R.id.btn3);
        b4=findViewById(R.id.btn4);
        clear_all=findViewById(R.id.remove_all_visualize);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        SharedPreferences sharedPreferences=getSharedPreferences("mypref", Context.MODE_PRIVATE);
        Set<String> s=sharedPreferences.getStringSet("visualize",null);
        int sz=s.size();
        ArrayList<Integer> ids=new ArrayList<>();
        for (String str : s){
            ids.add(Integer.parseInt(str));
        }

        ArrayList<DisplayData> arrayList=new ArrayList<>();
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
                        Toast.makeText(getApplicationContext(), "error adding the files", Toast.LENGTH_SHORT).show();
                    }
                }
                hide(sz,arrayList);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(getApplicationContext(), "Error fetching the data!! Please try again...", Toast.LENGTH_SHORT).show();
            }
        });

        clear_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(getIntent());
            }
        });

    }

    private void hide(int sz, ArrayList<DisplayData> arrayList) {
        Picasso.get().load(arrayList.get(0).getImage_links()).into(b1);
        Log.e("naku", String.valueOf(arrayList.get(0)));
        setUpModel(arrayList.get(0).getModel_links());
        setUpPlane();
        if(sz==4){
            Picasso.get().load(arrayList.get(1).getImage_links()).into(b2);
            Picasso.get().load(arrayList.get(2).getImage_links()).into(b3);
            Picasso.get().load(arrayList.get(3).getImage_links()).into(b4);
        }
        else if(sz==3){
            b4.setVisibility(View.INVISIBLE);
            Picasso.get().load(arrayList.get(1).getImage_links()).into(b2);
            Picasso.get().load(arrayList.get(2).getImage_links()).into(b3);
        }
        else if(sz==2){
            b4.setVisibility(View.INVISIBLE);
            b3.setVisibility(View.INVISIBLE);
            Picasso.get().load(arrayList.get(1).getImage_links()).into(b2);
        }
        else if(sz==1){
            b4.setVisibility(View.INVISIBLE);
            b3.setVisibility(View.INVISIBLE);
            b2.setVisibility(View.INVISIBLE);
        }
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpModel(arrayList.get(0).getModel_links());
                Toast.makeText(MainActivity.this, "Pic 1 selected", Toast.LENGTH_SHORT).show();
                setUpPlane();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpModel(arrayList.get(1).getModel_links());
                setUpPlane();
                Toast.makeText(MainActivity.this, "Pic 2 selected", Toast.LENGTH_SHORT).show();
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpModel(arrayList.get(2).getModel_links());
                setUpPlane();
                Toast.makeText(MainActivity.this, "Pic 3 selected", Toast.LENGTH_SHORT).show();
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpModel(arrayList.get(3).getModel_links());
                setUpPlane();
                Toast.makeText(MainActivity.this, "Pic 4 selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpModel(String Model_URL) {
        ModelRenderable.builder()
                        .setSource(this,
                                 RenderableSource.builder().setSource(
                                this,
                                Uri.parse(Model_URL),
                                RenderableSource.SourceType.GLB)
                                         .setScale(0.75f)
                                .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                                 .build())

                .setRegistryId(Model_URL)



                .build()
                .thenAccept(renderable -> modelRenderable = renderable)
                .exceptionally(throwable -> {
                    Log.i("Model","cant load");
                    Toast.makeText(MainActivity.this,"Model can't be Loaded", Toast.LENGTH_SHORT).show();
                    return null;
                });
    }

    private void setUpPlane(){
        arFragment.setOnTapArPlaneListener(((hitResult, plane, motionEvent) -> {
            Anchor anchor = hitResult.createAnchor();
            AnchorNode anchorNode = new AnchorNode(anchor);
            anchorNode.setParent(arFragment.getArSceneView().getScene());
            createModel(anchorNode);
        }));
    }

    private void createModel(AnchorNode anchorNode){
        TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
        node.setParent(anchorNode);
        node.setRenderable(modelRenderable);
        node.select();
    }
}