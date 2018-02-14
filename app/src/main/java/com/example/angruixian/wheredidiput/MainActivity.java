package com.example.angruixian.wheredidiput;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    EditText etItem, etPlace;
    Button btnSelectPicture, btnAdd;
    ImageView imgViewPlace;
    private static final int PICK_IMAGE_REQUEST = 71;
    public static final int PERMISSION_REQUEST = 200;
    private Uri filePath;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView nv;
    private String UUID = java.util.UUID.randomUUID().toString();
    // TODO: Task 1 - Declare Firebase variables
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference itemListRef;
    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Add Item");

        etItem =(EditText)findViewById(R.id.editTextItem);
        etPlace =(EditText)findViewById(R.id.editTextPlace);
        btnSelectPicture = (Button)findViewById(R.id.btnSelectPicture);
        btnAdd = (Button)findViewById(R.id.buttonAdd);
        imgViewPlace = (ImageView)findViewById(R.id.imageViewPlace);

        nv = (NavigationView)findViewById(R.id.nvAddItem);
//        mToolbar = (Toolbar)findViewById(R.id.nav_action);
//        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawerLayoutAddItem);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.close, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case(R.id.nav_add_item):
                        Intent i = new Intent(MainActivity.this,MainActivity.class);
                        startActivity(i);
                        break;

                    case(R.id.nav_item_list):
                        Intent iItemList = new Intent(getBaseContext(),ItemListActivity.class);
                        startActivity(iItemList);
                        break;

                }
                return true;
            }
        });

        // TODO: Task 2: Get Firebase database instance and reference
        firebaseDatabase = FirebaseDatabase.getInstance();
        itemListRef = firebaseDatabase.getReference("/itemList");

        //Firebase Init
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        btnSelectPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.CAMERA}, PERMISSION_REQUEST);
                }
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadImage();

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mToggle.onOptionsItemSelected(item)){
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void uploadImage(){

        if (filePath != null){
            final ProgressDialog progressDiolog = new ProgressDialog(MainActivity.this);
            progressDiolog.setTitle("Uploading");
            progressDiolog.show();

            StorageReference ref = storageReference.child("images/" + UUID);
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDiolog.dismiss();
                    String strItem = etItem.getText().toString();
                    String strPlace = etPlace.getText().toString();
                    Item insertBook = new Item(strItem,strPlace,taskSnapshot.getDownloadUrl().toString());
                    itemListRef.push().setValue(insertBook);
                    Toast.makeText(getBaseContext(),"Uploaded",Toast.LENGTH_SHORT).show();
                    etItem.setText("");
                    etPlace.setText("");
                }
            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDiolog.dismiss();
                    Toast.makeText(getBaseContext(),"Failed"+e.getMessage(),Toast.LENGTH_SHORT).show();
                    Log.d("uploaded failed", e.getMessage());
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDiolog.setMessage("Uploaded " + (int)progress + "%");
                }
            });
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getBaseContext().getContentResolver(),filePath);
                imgViewPlace.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

}
