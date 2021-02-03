package sg.edu.np.mad.evap2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class grpchat extends AppCompatActivity {
    DrawerLayout drawerLayout;
    TextView groupname, displaymsg;
    ImageButton btnsend;
    EditText sendmsg;

    ScrollView scrollview;

    FirebaseAuth mauth;
    DatabaseReference uref, grpnameref, grpmsgkeyref;

    String currentUserID, currentUsername, currentDate, currentTime, grpname, selectedmodule, enmod;

    ListView lvnaviEn;

    private static Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groupchat);

        grpchat.context = getApplicationContext();

        Toolbar toolbar = findViewById(R.id.grpnamebar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Study group");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        groupname = findViewById(R.id.tvStudygrpname);
        displaymsg = findViewById(R.id.tvgrpchatdisplay);
        sendmsg = findViewById(R.id.etMessage);

        btnsend = findViewById(R.id.btnSend);

        scrollview = findViewById(R.id.scrollview);

        grpname = getIntent().getStringExtra("groupname");

        //Firebase stuff
        mauth = FirebaseAuth.getInstance();
        currentUserID = mauth.getCurrentUser().getUid();
        uref = FirebaseDatabase.getInstance().getReference().child("users");
        grpnameref = FirebaseDatabase.getInstance().getReference().child("Groups").child(grpname);
        final DatabaseReference enrolment = FirebaseDatabase.getInstance().getReference().child("enrollment").child(currentUserID);

        groupname.setText(grpname);

        GetUsername(currentUserID);

        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveMsgToDB();

                sendmsg.setText("");
                scrollview.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

        //navi bar / navi bar for my module code
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.stdygrpmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.grpInfo:
                Intent i = new Intent(grpchat.this, groupinfo.class);
                i.putExtra("groupname", grpname);
                startActivity(i);
                return true;
            case R.id.setting:
                Toast.makeText(this, "Item 2 selected", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
        String message = sendmsg.getText().toString();
        String msgKey = grpnameref.push().getKey();

        if (TextUtils.isEmpty(message)) {
            Toast.makeText(this, "Please write a message", Toast.LENGTH_LONG).show();
        } else {
            //get date of message send
            Calendar date = Calendar.getInstance();
            SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMM dd, yyyy");

            currentDate = currentDateFormat.format(date.getTime());

            //get time of message send
            Calendar time = Calendar.getInstance();
            SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm a");

            currentTime = currentTimeFormat.format(time.getTime());

            HashMap<String, Object> grpMsgkey = new HashMap<>();
            grpnameref.updateChildren(grpMsgkey);

            grpmsgkeyref = grpnameref.child(msgKey);

            //compile all the message information into one variable
            HashMap<String, Object> msgInfoMap = new HashMap<>();
            msgInfoMap.put("username", currentUsername);
            msgInfoMap.put("message", message);
            msgInfoMap.put("date", currentDate);
            msgInfoMap.put("time", currentTime);

            grpmsgkeyref.updateChildren(msgInfoMap);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        grpnameref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    DisplayMessages(snapshot);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    DisplayMessages(snapshot);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void DisplayMessages(DataSnapshot snapshot) {
        Iterator iterator = snapshot.getChildren().iterator();

        while (iterator.hasNext()) {
            String chatDate = (String) ((DataSnapshot) iterator.next()).getValue();
            String chatMessage = (String) ((DataSnapshot) iterator.next()).getValue();
            String chatName = (String) ((DataSnapshot) iterator.next()).getValue();
            String chatTime = (String) ((DataSnapshot) iterator.next()).getValue();

            displaymsg.append(chatName + ":\n" + chatMessage + "\n" + chatTime + "  " + chatDate + "\n\n\n");

            scrollview.post(new Runnable() {
                @Override
                public void run() {
                    scrollview.fullScroll(View.FOCUS_DOWN);
                }
            });
        }
    }

    //allows for the drawer to disappear if user presses back on their phone
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START); //close drawer
        }
    }

    //intents for navigation items
    public void tvTasklistClick(View view) {
        drawerLayout.closeDrawer(GravityCompat.START);

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
