package com.example.ernestas.myapplication65651;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PostListActivity extends AppCompatActivity {


    Firebase firebase;
    private FirebaseAuth firebaseAuth;

    private DatabaseReference databaseReference;

    private ImageView ivPostPhoto;
    ListView lv;

    protected ArrayList<String> posts = new ArrayList<>();
    protected ArrayList<Uri> img = new ArrayList<>();

    private List<PostInformation> postList = new ArrayList<PostInformation>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);

        Firebase.setAndroidContext(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        firebase = new Firebase("https://howdoilooktoday-401f4.firebaseio.com");


        ivPostPhoto = (ImageView) findViewById(R.id.ivPostPhoto);
        lv = (ListView) findViewById(R.id.lvAllPosts);

        postList.clear();


        firebase.child("posts").addChildEventListener(new com.firebase.client.ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, String> map = dataSnapshot.getValue(Map.class);


                String news = null;

                /*displayEncodedImage(map.get("bit64"));*/

                PostInformation postInfo = new PostInformation(map.get("userID"),map.get("postText"), news, map.get("postDate"), map.get("bit64"));


                postList.add(postInfo);


                posts.add(postInfo.getPostText());






                if(posts.size() > 0) {

                    /*ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.single_row, R.id.tvPostText, posts);
                    lv.setAdapter(adapter);*/

                }



            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        populateListView();


    }

    private void populateListView() {
        ArrayAdapter<PostInformation> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.lvAllPosts);
        list.setAdapter(adapter);
    }

    private class MyListAdapter extends ArrayAdapter<PostInformation> {

        public MyListAdapter() {
            super(PostListActivity.this, R.layout.single_row, postList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // make sure we have a view to work with
            View itemView = convertView;

            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.single_row, parent, false);
            }

            //find a car
            PostInformation currentPost = postList.get(position);

            //fill the view
            ImageView imageView = (ImageView)itemView.findViewById(R.id.ivPostPhoto);

            byte[] decodedString = Base64.decode(currentPost.getBit64(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            imageView.setImageBitmap(decodedByte);

            TextView textView = (TextView)itemView.findViewById(R.id.tvPostText);
            textView.setText(currentPost.getPostText());

            return itemView;

        }

    }

    private void displayEncodedImage(String bit64) {
        byte[] decodedString = Base64.decode(bit64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        if (decodedByte != null) {
            ivPostPhoto.setImageBitmap(decodedByte);
        } else {
            System.out.print("bla bla");
        }

    }

}
