package sg.edu.np.mad.evap2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {
    public static UserModel userdata;
    final String TAG = "LoginActivity";
    public EditText emailId, password;
    Button btnLogin;
    TextView signUp;
    //create firebase object
    FirebaseAuth mFirebaseAuth;
    public UserModel userModel;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //create firebase instance
        mFirebaseAuth = FirebaseAuth.getInstance();
        //find email from text input
        emailId = findViewById(R.id.etEmail);
        //find password from text input
        password = findViewById((R.id.etPassword));
        //button
        btnLogin = findViewById(R.id.btnLogin);
        //link to sign in
        signUp = findViewById(R.id.tvSignup);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //To get current user and sign user in without input:
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if (mFirebaseUser != null) {
                    Toast.makeText(login.this,"You are logged in", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(login.this, MainActivity.class);
                    startActivity(i);

                }
                else   {
                    Toast.makeText(login.this,"Please Login", Toast.LENGTH_SHORT).show();
                }
            }
        };
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailId.getText().toString();
                final String pass = password.getText().toString();
                //check whether email is empty
                if (email.isEmpty()) {
                    //set empty password error
                    emailId.setError("Please enter your email");
                    emailId.requestFocus();
                }
                //check whether password is empty
                //it is then
                else  if (pass.isEmpty())
                {
                    //set empty password error
                    password.setError("Please enter your password");
                    password.requestFocus();
                }
                //if both not empty then go ahead with sign in
                else if (!(email.isEmpty() && pass.isEmpty())) {
                    Log.d(TAG, "signing started with email and password: "+email+" "+pass);
                    mFirebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(login.this, "Login Failed... Please Try Again", Toast.LENGTH_SHORT).show();

                            } else {
                                Intent intToHome = new Intent(login.this, MainActivity.class);
                                intToHome.putExtra("email", email);
                                startActivity(intToHome);
                            }
                        }
                    });
                }
                //if some other error occurred then display this msg
                else{
                    Toast.makeText(login.this,"Error Occurred!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ChangeActivity = new Intent(login.this, signup.class);

                startActivity(ChangeActivity);

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

}
