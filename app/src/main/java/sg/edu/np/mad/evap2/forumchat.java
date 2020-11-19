package sg.edu.np.mad.evap2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class forumchat extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView rvForumchat;
    RecyclerView.LayoutManager layoutManager;

    TextView forumchattitle, forumchatcontent;

    EditText forumchat;

    ImageButton btnsend;

    DatabaseReference uref, forumnameref, forummsgkeyref;
    FirebaseAuth mauth;

    String currentUserID, currentUsername, title, forumname, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forumchat);

        forumchattitle = findViewById(R.id.tvforumchattitle);
        forumchatcontent = findViewById(R.id.tvfourmchatcontent);

        forumchat = findViewById(R.id.etforumchat);

        btnsend = findViewById(R.id.btnforumchatsend);

        rvForumchat = findViewById(R.id.rvforum);

        toolbar = findViewById(R.id.forumbar);

        mauth = FirebaseAuth.getInstance();

        forumname = getIntent().getStringExtra("category");
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");

        //toolbar code
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Forum: " + forumname);

        forumchattitle.setText(title);
        forumchatcontent.setText(content);

        //firebase stuff
        mauth = FirebaseAuth.getInstance();
        currentUserID = mauth.getCurrentUser().getUid();
        uref = FirebaseDatabase.getInstance().getReference().child("users");
        forumnameref = FirebaseDatabase.getInstance().getReference().child("Forums").child(title);

        GetUsername(currentUserID);

        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveMsgToDB();
                forumchat.setText("");
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

    private void SaveMsgToDB() {
        String message = forumchat.getText().toString();
        String msgKey = forumnameref.push().getKey();

        if (TextUtils.isEmpty(message)) {
            Toast.makeText(this, "Please write a message", Toast.LENGTH_LONG).show();
        } else{
            HashMap<String, Object> forumMsgkey = new HashMap<>();
            forumnameref.updateChildren(forumMsgkey);

            forummsgkeyref = forumnameref.child(msgKey);

            //compile all the message information into one variable
            HashMap<String, Object> msgInfoMap = new HashMap<>();
            msgInfoMap.put("username", currentUsername);
            msgInfoMap.put("atitle", title);
            msgInfoMap.put("acontent", content);
            msgInfoMap.put("message", message);

            forummsgkeyref.updateChildren(msgInfoMap);
        }
    }
}