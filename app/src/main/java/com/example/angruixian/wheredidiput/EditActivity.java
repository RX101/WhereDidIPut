package com.example.angruixian.wheredidiput;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class EditActivity extends AppCompatActivity {

    EditText etEditItem, etEditPlace;
    Button btnEdit;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView nv;
    // TODO: Task 1 - Declare Firebase variables
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference itemListRef;
    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        setTitle("Edit Item");
        etEditItem = (EditText) findViewById(R.id.editTextEditItem);
        etEditPlace = (EditText) findViewById(R.id.editTextEditPlace);
        btnEdit = (Button) findViewById(R.id.buttonEdit);

        nv = (NavigationView)findViewById(R.id.nvEditItem);
//        mToolbar = (Toolbar)findViewById(R.id.nav_action);
//        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawerLayoutEditItem);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.close, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case(R.id.nav_add_item):
                        Intent i = new Intent(getBaseContext(),MainActivity.class);
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

        firebaseDatabase = FirebaseDatabase.getInstance();
        itemListRef = firebaseDatabase.getReference("/itemList");

        final Intent intent = getIntent();
        final Item item = (Item) intent.getSerializableExtra("Item");

       etEditItem.setText(item.getItemTitle());
       etEditPlace.setText(item.getItemPlace());

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemTitle = etEditItem.getText().toString();
                String itemPlace = etEditPlace.getText().toString();
                String id = item.getItemId();
                Item updatedItem = new Item(itemTitle,itemPlace,item.getItemImage());
                Toast.makeText(getApplicationContext(), "Item record updated successfully", Toast.LENGTH_SHORT).show();
                itemListRef.child(id).setValue(updatedItem);
                setResult(RESULT_OK);
                finish();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
