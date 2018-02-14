package com.example.angruixian.wheredidiput;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ItemListActivity extends AppCompatActivity {

    ListView lvItem;
    ItemAdapter caItems;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView nv;
    private ArrayList<Item> itemsArrayList;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference itemsListRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        setTitle("Item List");
        lvItem = (ListView) findViewById(R.id.listViewItem);
        nv = (NavigationView)findViewById(R.id.nvItemList);
//        mToolbar = (Toolbar)findViewById(R.id.nav_action);
//        setSupportActionBar(mToolbar);

        itemsArrayList = new ArrayList<Item>();
        caItems = new ItemAdapter(this, R.layout.custom_layout, itemsArrayList);
        lvItem.setAdapter(caItems);

        firebaseDatabase = FirebaseDatabase.getInstance();
        itemsListRef = firebaseDatabase.getReference("/itemList");

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawerLayoutItemList);
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

        itemsListRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Item book = dataSnapshot.getValue(Item.class);
                if (book != null) {
                    book.setItemId(dataSnapshot.getKey());
                    itemsArrayList.add(book);
//                    aaInventory.notifyDataSetChanged();
                    caItems.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String selectedId = dataSnapshot.getKey();
                Item item = dataSnapshot.getValue(Item.class);
                if (item != null) {
                    for (int i = 0; i < itemsArrayList.size(); i++) {
                        if (itemsArrayList.get(i).getItemId().equals(selectedId)) {
                            item.setItemId(selectedId);
                            itemsArrayList.set(i, item);
                        }
                    }
//                    aaInventory.notifyDataSetChanged();
                    caItems.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String selectedId = dataSnapshot.getKey();
                Item item = dataSnapshot.getValue(Item.class);
                if (item != null) {
                    for (int i = 0; i < itemsArrayList.size(); i++) {
                        if (itemsArrayList.get(i).getItemId().equals(selectedId)) {
                            item.setItemId(selectedId);
                            itemsArrayList.remove(i);
                        }
                    }
//                    aaInventory.notifyDataSetChanged();
                    caItems.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        lvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Item item = (Item) adapterView.getItemAtPosition(position);
                Intent i = new Intent(ItemListActivity.this,EditActivity.class);
                i.putExtra("Item",item);
                startActivity(i);
            }
        });

        lvItem.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                final String id = itemsArrayList.get(position).getItemId();
                AlertDialog.Builder b = new AlertDialog.Builder(ItemListActivity.this);
                b.setIcon(android.R.drawable.ic_dialog_alert);
                b.setTitle("Are you sure to delete " + itemsArrayList.get(position).getItemTitle()+" ?");
                b.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(getApplicationContext(), "Item record removed successfully", Toast.LENGTH_SHORT).show();
                        itemsListRef.child(id).removeValue();
                    }
                });
                b.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                b.show();

                return true;
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
}
