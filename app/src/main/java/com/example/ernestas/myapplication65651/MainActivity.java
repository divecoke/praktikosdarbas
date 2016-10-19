package com.example.ernestas.myapplication65651;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private TextView tvViewEmail;
    private Button bLogout, bProfileSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null) {
            startActivity(new Intent(getApplicationContext(), Login.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();

        tvViewEmail = (TextView) findViewById(R.id.tvViewEmail);

        tvViewEmail.setText("Welcome: " + user.getEmail());

        bLogout = (Button) findViewById(R.id.bLogout);

        bProfileSettings = (Button) findViewById(R.id.bProfileSettings);

        bLogout.setOnClickListener(this);
        bProfileSettings.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == bLogout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(getApplicationContext(), Login.class));
        }
        if(v == bProfileSettings) {
            startActivity(new Intent(getApplicationContext(), ProfileSettings.class));
        }
    }
}
