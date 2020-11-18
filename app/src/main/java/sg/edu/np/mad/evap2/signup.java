package sg.edu.np.mad.evap2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signup extends AppCompatActivity {
    private EditText etUsername, etEmail, etPwd, etCfmPwd;
    private TextView tvLogin;
    private Button btnSignUp;
    private FirebaseAuth mAuth;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPwd = findViewById(R.id.etPassword);
        etCfmPwd = findViewById(R.id.etCfmpassword);

        tvLogin = findViewById(R.id.tvSignup);

        btnSignUp = findViewById(R.id.btnSignup);

        mAuth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference();

        //submit account data into firebase when button is clicked
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = etUsername.getText().toString();
                final String email = etEmail.getText().toString();
                final String pwd = etPwd.getText().toString();
                String Cfmpwd = etCfmPwd.getText().toString();

                //checks if the field is empty
                if(username.isEmpty() || email.isEmpty() || pwd.isEmpty() || Cfmpwd.isEmpty()){
                    Toast.makeText(signup.this, "Please complete all the details.", Toast.LENGTH_SHORT).show();
                }
                //checks if both pwd field is the same
                else if(!etPwd.getText().toString().equals(etCfmPwd.getText().toString())){
                    Toast.makeText(signup.this, "Password must be the same.", Toast.LENGTH_SHORT).show();
                }
                else{
                    final UserModel userdata = new UserModel(username, email, pwd);
                    mAuth.createUserWithEmailAndPassword(email, pwd)
                            .addOnCompleteListener(signup.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        FirebaseUser user = mAuth.getCurrentUser();

                                        writeNewStudent(user.getUid(), username, email, pwd);
                                        Toast.makeText(signup.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                                        login.userdata = userdata;
                                        Intent mainpage = new Intent(signup.this, login.class);
                                        startActivity(mainpage);
                                    }

                                    else{
                                        try {
                                            throw task.getException();
                                        } catch (FirebaseAuthWeakPasswordException weakPassword) {
                                            Toast.makeText(signup.this, "You need at least 6 characters.",
                                                    Toast.LENGTH_SHORT).show();

                                        } catch (FirebaseAuthInvalidCredentialsException invalidEmail) {
                                            Toast.makeText(signup.this, "Invalid email.",
                                                    Toast.LENGTH_SHORT).show();
                                        } catch (FirebaseAuthUserCollisionException emailExists) {
                                            Toast.makeText(signup.this, "Email already exists.",
                                                    Toast.LENGTH_SHORT).show();
                                        } catch (Exception e) {
                                            //Everything else
                                        }
                                        // If sign in fails, display a message to the user.
                                        //Log.v(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(signup.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        //text view to redirect users back to login page if they have an account
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainpage = new Intent(signup.this, login.class);
                startActivity(mainpage);
            }
        });
    }

    private void writeNewStudent(String userid, String username, String email, String pwd){
        ref.child("users").child(userid).child("username").setValue(username);
        ref.child("users").child(userid).child("email").setValue(email);
        ref.child("users").child(userid).child("password").setValue(pwd);
        ref.child("users").child(userid).child("uid").setValue(userid);
    }
}
