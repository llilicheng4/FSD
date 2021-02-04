package sg.edu.np.mad.evap2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class forumchat extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView rvForumchat;
    RecyclerView.LayoutManager layoutManager;

    TextView forumchattitle, forumchatcontent, notetitle, tvusername, desc, chatcontent;

    EditText fchat;

    ImageButton btnsend, btnattachment;

    DatabaseReference uref, forumnameref, forummsgkeyref;
    FirebaseAuth mauth;

    String currentUserID, currentUsername, title, forumname, content, selectedmodule, enmod;

    //for uploading images
    private static final int PICK_IMAGE_REQUEST = 1;
    private String checker ="", myUri="";
    private StorageTask uploadTask;
    private Uri fileUri;
    private ProgressDialog loadingBar;
    private Context mContext;
    //end

    DrawerLayout drawerLayout;
    ListView lvnaviEn;

    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forumchat);

        forumchat.context = getApplicationContext();

        forumchattitle = findViewById(R.id.tvforumchattitle);
        forumchatcontent = findViewById(R.id.tvfourmchatcontent);

        notetitle = findViewById(R.id.tvnotetitle);
        tvusername = findViewById(R.id.tvUsername);
        desc = findViewById(R.id.tvDesc);

        fchat = findViewById(R.id.etforumchat);

        btnsend = findViewById(R.id.btnforumchatsend);
        btnattachment = findViewById(R.id.btnforumattachment);

        rvForumchat = findViewById(R.id.rvForumchat);

        toolbar = findViewById(R.id.forumbar);

        mauth = FirebaseAuth.getInstance();

        forumname = getIntent().getStringExtra("category");
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");

        //sending image stuff
        loadingBar = new ProgressDialog(this);

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
                fchat.setText("");
            }
        });

        //code to open the device storage to send images
        btnattachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence options[] = new CharSequence[]{
                        "Images"
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(forumchat.this);
                builder.setTitle("Select file");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        if (i==0){
                            checker = "image";

                            //intent to open internal storage
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(intent.ACTION_GET_CONTENT);
                            startActivityForResult(intent, PICK_IMAGE_REQUEST);
                        }
                    }
                });
                builder.show();
            }
        });

        //navi bar / navi bar for my module code
        final DatabaseReference enrolment = FirebaseDatabase.getInstance().getReference().child("enrollment").child(currentUserID);
        drawerLayout = findViewById(R.id.drawer_layout);
        lvnaviEn = findViewById(R.id.lvnaviEnroll);
        enrolment.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> enrolmentList = new ArrayList<>();
                for(DataSnapshot enrolmentmodule : snapshot.getChildren()){

                    enmod = enrolmentmodule.getValue().toString();

                    enrolmentList.add(enmod);

                }

                final ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, enrolmentList);
                lvnaviEn.setAdapter(adapter);
                lvnaviEn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        selectedmodule = parent.getItemAtPosition(position).toString();
                        Log.d("Out", selectedmodule);
                        Intent intent = new Intent(context, ViewLearningFragment.class);
                        intent.putExtra("selectmodule", selectedmodule);

                        drawerLayout.closeDrawer(GravityCompat.START);

                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //code when activity resumes from intent to get image data
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            fileUri = data.getData();
            uploadFile();
        }
    }

    //gets the extension of the file that the user selects
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    //upload the file into firebase
    private void uploadFile() {
        if (fileUri != null) {
            loadingBar.setTitle("Sending file");
            loadingBar.setMessage("Please wait, we are sending file....");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("Image files");

            String msgKey = forumnameref.push().getKey();

            HashMap<String, Object> forumMsgkey = new HashMap<>();
            forumnameref.updateChildren(forumMsgkey);

            forummsgkeyref = forumnameref.child(msgKey);

            String msgPushID = forummsgkeyref.getKey();

            final StorageReference fileReference = storageRef.child(msgPushID
                    + "." + getFileExtension(fileUri));
            uploadTask = fileReference.putFile(fileUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        myUri = downloadUri.toString();
                        Log.d("output", "uri: " + myUri);

                        //compile all the message information into one variable
                        HashMap<String, Object> msgInfoMap = new HashMap<>();
                        msgInfoMap.put("username", currentUsername);
                        msgInfoMap.put("atitle", title);
                        msgInfoMap.put("acontent", content);
                        msgInfoMap.put("message", myUri);
                        msgInfoMap.put("filename", fileUri.getLastPathSegment());
                        msgInfoMap.put("messagetype", checker);

                        forummsgkeyref.updateChildren(msgInfoMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                loadingBar.dismiss();
                            }
                        });
                    }
                }
            });

        } else {
            loadingBar.dismiss();
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
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
        String message = fchat.getText().toString();
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
            msgInfoMap.put("messagetype", "text");

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
            protected void onBindViewHolder(@NonNull final forumchat.GroupsViewHolder groupsViewHolder, final int i, @NonNull forumchatclass forumchatclass) {
                String msgid = getRef(i).getKey();
                forumnameref.child(msgid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String name = snapshot.child("username").getValue().toString();
                        String desc = snapshot.child("atitle").getValue().toString();
                        String msg = snapshot.child("message").getValue().toString();
                        String content = snapshot.child("acontent").getValue().toString();

                        groupsViewHolder.desc.setText(msg);
                        groupsViewHolder.username.setText(name);
                        groupsViewHolder.notetitle.setText(desc);
                        Picasso.get().load(msg).into(groupsViewHolder.imgChat);

                        groupsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //if(currentUsername = )
                            }
                        });
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

        public TextView notetitle, username, desc;
        public ImageView imgChat;

        public GroupsViewHolder(@NonNull View itemView) {
            super(itemView);
            notetitle = itemView.findViewById(R.id.tvnotetitle);
            username = itemView.findViewById(R.id.tvUsername);
            desc = itemView.findViewById(R.id.tvDesc);
            imgChat = itemView.findViewById(R.id.ivImg);
        }
    }

    //intents for navigation items
    public void tvTasklistClick(View view) {
        drawerLayout.closeDrawer(GravityCompat.START);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Task lisk");
        CategoryFragment fragment = new CategoryFragment();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.frameLayout,fragment).commit();
    }

    public void tvDiscussionClick(View view) {
        drawerLayout.closeDrawer(GravityCompat.START);
        redirectActivity(this, discussionboard.class);
    }

    public void tvLogoutClick(View view) {
        FirebaseAuth.getInstance().signOut();
        redirectActivity(this, login.class);
    }

    public void tvBrowseModulesClick(View view){
        drawerLayout.closeDrawer(GravityCompat.START);
        redirectActivity(this, MainActivity.class);
    }

    public void tvAccountClick(View view){
        drawerLayout.closeDrawer(GravityCompat.START);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Account");
        account fragment = new account();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.frameLayout,fragment).commit();
    }

    public static void redirectActivity(Activity activity, Class aClass) {
        Intent intent = new Intent(activity, aClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);

    }

}