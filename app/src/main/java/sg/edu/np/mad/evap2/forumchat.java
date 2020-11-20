package sg.edu.np.mad.evap2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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
    ScrollView scrollview;

    TextView forumchattitle, forumchatcontent, notetitle, tvusername, desc, chatcontent;

    EditText forumchat;

    ImageButton btnsend;

    CardView cv;

    DatabaseReference uref, forumnameref, forummsgkeyref;
    FirebaseAuth mauth;

    String currentUserID, currentUsername, title, forumname, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forumchat);

        forumchattitle = findViewById(R.id.tvforumchattitle);
        forumchatcontent = findViewById(R.id.tvfourmchatcontent);

        notetitle = findViewById(R.id.tvnotetitle);
        tvusername = findViewById(R.id.tvUsername);
        desc = findViewById(R.id.tvDesc);
        chatcontent = findViewById(R.id.forumchatcontent);

        forumchat = findViewById(R.id.etforumchat);

        btnsend = findViewById(R.id.btnforumchatsend);

        rvForumchat = findViewById(R.id.rvForumchat);



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

        layoutManager = new LinearLayoutManager(this);
        rvForumchat.setLayoutManager(layoutManager);

        GetUsername(currentUserID);

        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveMsgToDB();
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

    @Override
    public void onStart(){
        super.onStart();

        DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("Forums").child(title);

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<forumchatclass>()
                        .setQuery(dref.orderByChild("atitle").equalTo(title), forumchatclass.class)
                        .build();

        FirebaseRecyclerAdapter<forumchatclass, forumchat.GroupsViewHolder> adapter
                = new FirebaseRecyclerAdapter<forumchatclass, forumchat.GroupsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final forumchat.GroupsViewHolder groupsViewHolder, int i, @NonNull forumchatclass forumchatclass) {
                String msgid = getRef(i).getKey();
                forumnameref.child(msgid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String name = snapshot.child("username").getValue().toString();
                        String desc = snapshot.child("atitle").getValue().toString();
                        String msg = snapshot.child("message").getValue().toString();
                        String content = snapshot.child("acontent").getValue().toString();

                        groupsViewHolder.desc.setText(content);
                        groupsViewHolder.username.setText(name);
                        groupsViewHolder.forumchatcontent.setText(msg);
                        groupsViewHolder.notetitle.setText(desc);
                        /*final String itemText = groupsViewHolder.grpname.getText().toString();

                        groupsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(discussionboard.this, grpchat.class);

                                intent.putExtra("groupname", itemText);
                                startActivity(intent);
                            }
                        });*/
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @NonNull
            @Override
            public forumchat.GroupsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rvforumchat, parent,false);
                forumchat.GroupsViewHolder viewholder = new forumchat.GroupsViewHolder(view);
                return viewholder;
            }
        };

        rvForumchat.setAdapter(adapter);
        adapter.startListening();
        adapter.notifyDataSetChanged();
    }

    public static class GroupsViewHolder extends RecyclerView.ViewHolder{

        public TextView notetitle, username, desc, forumchatcontent;

        public GroupsViewHolder(@NonNull View itemView) {
            super(itemView);

            notetitle = itemView.findViewById(R.id.tvnotetitle);
            username = itemView.findViewById(R.id.tvUsername);
            desc = itemView.findViewById(R.id.tvDesc);
            forumchatcontent = itemView.findViewById(R.id.forumchatcontent);
        }
    }
}