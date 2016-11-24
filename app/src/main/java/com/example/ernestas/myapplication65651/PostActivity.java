package com.example.ernestas.myapplication65651;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import java.util.Map;

public class PostActivity extends AppCompatActivity implements View.OnClickListener {


    private  String IMAGE_URL;

    private ImageView ivTakePhoto;
    private EditText etPostText;
    private Button bPost;

    Firebase firebase;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private ProgressBar progressBar;

    private StorageReference mStorage;

    public static final int GALLERY_INTENT = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);


        Firebase.setAndroidContext(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        firebaseAuth = FirebaseAuth.getInstance();



        firebase = new Firebase("https://howdoilooktoday-401f4.firebaseio.com");



        mStorage = FirebaseStorage.getInstance().getReference();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        ivTakePhoto = (ImageView) findViewById(R.id.ivTakePicture);
        etPostText = (EditText) findViewById(R.id.etPostText);
        bPost = (Button) findViewById(R.id.bPost);

        bPost.setOnClickListener(this);
        ivTakePhoto.setOnClickListener(this);





    }

    @Override
    public void onClick(View v)  {

        if(v == ivTakePhoto) {

            Intent intent = new Intent(Intent.ACTION_PICK);

            intent.setType("image/*");

            startActivityForResult(intent, GALLERY_INTENT);

        }

        if(v == bPost) {

            try {
                savePost();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }


    public void savePost() throws ParseException {

        String postText = etPostText.getText().toString().trim();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date()); // Find todays date

        FirebaseUser user = firebaseAuth.getCurrentUser();

        PostInformation postInformation = new PostInformation(user.getUid(), postText, IMAGE_URL, currentDateTime);


            String uuid = UUID.randomUUID().toString();
            databaseReference.child("posts").child(uuid).setValue(postInformation); //Saugojimas
            Toast.makeText(this, "Information Saved", Toast.LENGTH_LONG).show();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {

            Uri uri = data.getData();

            StorageReference filePath = mStorage.child("post").child("photos").child(uri.getLastPathSegment());

            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    
                    Uri downloadUri = taskSnapshot.getDownloadUrl();

                    IMAGE_URL = taskSnapshot.getStorage().child("pathSegments").child("4").getName();

                    System.out.println(IMAGE_URL);

                    Picasso.with(getApplicationContext()).load(downloadUri).fit().centerCrop().into(ivTakePhoto);

                    Toast.makeText(getApplicationContext(), "Upload Done...", Toast.LENGTH_LONG).show();

                }
            });

        }
    }


}
