package sg.edu.np.mad.evap2;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class creategrp extends AppCompatActivity {

    EditText grpname, grpdesc;

    FloatingActionButton addgrp;

    DatabaseReference ref;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_grp);

        grpname = findViewById(R.id.etGrpname);
        grpdesc = findViewById(R.id.etGrpdesc);

        addgrp = findViewById(R.id.fabCreategrp);

        ref = FirebaseDatabase.getInstance().getReference();

        addgrp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = grpname.getText().toString();
                String desc = grpdesc.getText().toString();

                //blocks entry if user doesnt enter a group name and group description
                if(TextUtils.isEmpty(name) && TextUtils.isEmpty(desc)){
                    Toast.makeText(creategrp.this, "Fields cannot be empty.", Toast.LENGTH_LONG).show();
                }
                else{
                    CreateNewGrp(name, desc);
                }
            }


        });
    }
    private void CreateNewGrp(final String n, final String d) {
        ref.child("Groups").child(n).setValue("")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            ref.child("Groups").child(n).child("name").setValue(n);
                            ref.child("Groups").child(n).child("description").setValue(d);
                            Toast.makeText(creategrp.this, "Study group created!", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}
