package com.example.angruixian.wheredidiput;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by angruixian on 15/2/18.
 */

public class ItemAdapter extends ArrayAdapter<Item> {
    private ArrayList<Item> itemArrayList;
    private Context context;
    private TextView tvItem, tvPlace;
    private ImageView ivPlace;
    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;

    public ItemAdapter(Context context, int resource, ArrayList<Item> objects){
        super(context, resource, objects);
        // Store the food that is passed to this adapter
        itemArrayList = objects;
        // Store Context object as we would need to use it later
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // The usual way to get the LayoutInflater object to
        //  "inflate" the XML file into a View object
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // "Inflate" the row.xml as the layout for the View object
        View rowView = inflater.inflate(R.layout.custom_layout, parent, false);

        tvItem = (TextView) rowView.findViewById(R.id.tvItem);
        tvPlace = (TextView) rowView.findViewById(R.id.tvPlace);
        ivPlace = (ImageView)rowView.findViewById(R.id.imageViewBook);

//        StorageReference storageRef =
//                FirebaseStorage.getInstance().getReference();
//        storageRef.child("users/me/profile.png").getDownloadUrl()
        // The parameter "position" is the index of the
        //  row ListView is requesting.
        //  We get back the food at the same index.
        Item currentItem = itemArrayList.get(position);
        // Set the TextView to show the food

        tvItem.setText("Item: "+currentItem.getItemTitle());
        tvPlace.setText("Place: "+currentItem.getItemPlace());
        //Firebase Init
//        storage = FirebaseStorage.getInstance();
//        storageReference = storage.getReference().child("images/"+ currentBook.getItemImage());
//        Uri newUrl = storageReference.getDownloadUrl();
//        Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(storageReference).getContent());
        Glide.with(context).load(currentItem.getItemImage()).into(ivPlace);
        // Return the nicely done up View to the ListView
        return rowView;
    }

}
