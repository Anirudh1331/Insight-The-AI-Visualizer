package com.example.easylearn;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArFragment arFragment;
    private ModelRenderable modelRenderable;
    //3d model credit : google.poly.com
    private String Model_URL1 = "https://drive.google.com/uc?export=download&id=1I0g0meI1F3gNIfnvzvJWK5SrjweoH_je";
    private String Model_URL2 = "https://drive.google.com/uc?export=download&id=1YIFeTW6x1qP_D7vfduXSgWyE4-3NPDLU";
    private String Model_URL3 = "https://drive.google.com/uc?export=download&id=1USfTvZ9ZZpa2erPWvsoLF-D-jbVpkBHE";
    Button b1,b2,b3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1=findViewById(R.id.btn1);
        b2=findViewById(R.id.btn2);
        b3=findViewById(R.id.btn3);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);

        setUpModel(Model_URL3);
        setUpPlane();
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpModel(Model_URL1);
                setUpPlane();
                Toast.makeText(MainActivity.this, "You selected figure 1", Toast.LENGTH_SHORT).show();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpModel(Model_URL2);
                setUpPlane();
                Toast.makeText(MainActivity.this, "You selected figure 2", Toast.LENGTH_SHORT).show();
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpModel(Model_URL3);
                setUpPlane();
                Toast.makeText(MainActivity.this, "You selected figure 3", Toast.LENGTH_SHORT).show();
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

//        ArrayList<SlideModel> images=new ArrayList<>();
//        images.add(new SlideModel(R.drawable.chair1,null));
//        images.add(new SlideModel(R.drawable.chair2,null));
//        images.add(new SlideModel(R.drawable.chair3,null));
//        imageSlider.setImageList(images, ScaleTypes.FIT);
//
//        setUpModel(Model_URL1);
//        setUpPlane();
//        imageSlider.setItemClickListener(new ItemClickListener() {
//            @Override
//            public void onItemSelected(int i) {
//                String model="Model_URL"+(i+1);
//                setUpModel(model);
//                setUpPlane();
//                Toast.makeText(MainActivity.this, ""+model, Toast.LENGTH_SHORT).show();
//            }
//        });
