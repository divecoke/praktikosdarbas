package com.example.ernestas.myapplication65651;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

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
import java.util.Map;

public class PostListActivity extends AppCompatActivity {


    Firebase firebase;
    private FirebaseAuth firebaseAuth;

    private DatabaseReference databaseReference;

    private ImageView ivPostPhoto;
    ListView lv;

    protected ArrayList<String> posts = new ArrayList<>();
    protected ArrayList<Uri> img = new ArrayList<>();

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


        firebase.child("posts").addChildEventListener(new com.firebase.client.ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Map<String, String> map = dataSnapshot.getValue(Map.class);
                    Map<Uri, Uri> maps = dataSnapshot.getValue(Map.class);


                    String news = null;

                    PostInformation postInfo = new PostInformation(map.get("user_id"), map.get("postText"), news, map.get("postDate"));

                    posts.add(postInfo.getPostText());

                if(posts.size() > 0) {

                    ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.single_row, R.id.tvPostText, posts);
                    lv.setAdapter(adapter);

                   /* firebase.child("groups/pizza.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // TODO: handle uri
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });*/

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




    }

}
