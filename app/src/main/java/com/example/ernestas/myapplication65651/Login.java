package com.example.ernestas.myapplication65651;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private Button bLogin;
    private EditText etEmail, etPassword;
    private TextView tvRegisterHere;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    private ProgressDialog progressdialog;

    private static final int RC_SIGN_IN = 0;
    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = "LOGIN_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(Login.this, "Logged in with Google", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(Login.this, "User is logged out", Toast.LENGTH_LONG).show();
                }

            }
        };


        if(firebaseAuth.getCurrentUser() != null) {
            //Profile activity here
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        progressdialog = new ProgressDialog(this);

        bLogin = (Button) findViewById(R.id.bLogin);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvRegisterHere = (TextView) findViewById(R.id.tvRegisterHere);

        bLogin.setOnClickListener(this);
        tvRegisterHere.setOnClickListener(this);

        findViewById(R.id.bGoogleLogin).setOnClickListener(this);


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    @Override
    protected void onStart() {
        super.onStart();

        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(firebaseAuthListener != null)
            firebaseAuth.removeAuthStateListener(firebaseAuthListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
                Log.d(TAG, "Google Login Failed");
            }
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct){
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("AUTH", "signInWithCredential:oncomplete: " + task.isSuccessful());
                        finish();
                        Intent goInMainPage = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(goInMainPage);
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut(){
        FirebaseAuth.getInstance().signOut();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed.");
    }




    private void tvRegisterOn() {
        Intent goToRegister = new Intent(getApplicationContext(),Register.class);
        startActivity(goToRegister);
        finish();
    }

    public void loginRequest() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if(TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter the username", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter the password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressdialog.setMessage("Logging in please wait..");
        progressdialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressdialog.dismiss();
                if(task.isSuccessful()) {
                    //start main activity
                    finish();
                    Toast.makeText(Login.this, "You are logged in successfull", Toast.LENGTH_SHORT).show();
                    Intent goInLogin = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(goInLogin);
                } else {
                    Toast.makeText(Login.this, "Something is wrong please check information and try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.bLogin:
                loginRequest();
                break;
            case R.id.tvRegisterHere:
                tvRegisterOn();
                break;
            case  R.id.bGoogleLogin:
                signIn();
                break;
        }
    }


}
