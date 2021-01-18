package sg.edu.np.mad.evap2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class addforum extends AppCompatActivity {

    EditText title, content, category;
    FloatingActionButton btncreate;

    Toolbar toolbar;

    DatabaseReference ref, uref;
    FirebaseAuth mauth;

    String currentUserID, currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_forum);

        title = findViewById(R.id.etForumtitle);
        content = findViewById(R.id.etForumcontent);
        category = findViewById(R.id.etCategory);

        btncreate = findViewById(R.id.fabCreateforum);

        toolbar = findViewById(R.id.forumbar);

        //toolbar code
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Forum");

        ref = FirebaseDatabase.getInstance().getReference();
        uref = FirebaseDatabase.getInstance().getReference().child("users");

        mauth = FirebaseAuth.getInstance();

        currentUserID = mauth.getCurrentUser().getUid();
        GetUsername(currentUserID);
        btncreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ftitle = title.getText().toString();
                String fcontent = content.getText().toString();
                String fcategory = category.getText().toString();

                //blocks entry if user doesnt enter a group name and group description
                if(TextUtils.isEmpty(ftitle) && TextUtils.isEmpty(fcontent)&& TextUtils.isEmpty(fcategory)){
                    Toast.makeText(addforum.this, "Fields cannot be empty.", Toast.LENGTH_LONG).show();
                }
                else{
                    CreateNewForum(ftitle, fcontent, fcategory);
                }
            }


        });
    }

    private void CreateNewForum(final String n, final String d, final String c) {
        ref.child("Forums").child(n).setValue("")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            ref.child("Forums").child(n).child("op").setValue(currentUsername);
                            ref.child("Forums").child(n).child("title").setValue(n);
                            ref.child("Forums").child(n).child("content").setValue(d);
                            ref.child("Forums").child(n).child("category").setValue(c);
                            ref.child("Forums").child(n).child("status").setValue("");
                            ref.child("Forums").child(n).child("noofanswers").setValue("0");
                            Toast.makeText(addforum.this, "Forum created!", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    private void GetUsername(String uid) {
        uref.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    currentUsername = snapshot.child("username").getValue().toString();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}