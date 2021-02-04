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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class creategrp extends AppCompatActivity {

    EditText grpname, grpdesc;

    FloatingActionButton addgrp;

    DatabaseReference ref, uref;
    FirebaseAuth mauth;

    Toolbar toolbar;

    String currentUserID, currentUsername, selectedmodule, enmod, uid;

    DrawerLayout drawerLayout;
    ListView lvnaviEn;

    private static Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_grp);

        creategrp.context = getApplicationContext();

        grpname = findViewById(R.id.etForumtitle);
        grpdesc = findViewById(R.id.etGrpdesc);

        addgrp = findViewById(R.id.fabCreategrp);

        ref = FirebaseDatabase.getInstance().getReference();
        uref = FirebaseDatabase.getInstance().getReference().child("users");

        mauth = FirebaseAuth.getInstance();

        currentUserID = mauth.getCurrentUser().getUid();

        uid = mauth.getUid();

        GetUsername(currentUserID);

        //toolbar code
        toolbar = findViewById(R.id.forumbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Created study group");

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
                    CreateNewGrp(name, desc, currentUserID);
                }
            }


        });

        //navi bar / navi bar for my module code
        final DatabaseReference enrolment = FirebaseDatabase.getInstance().getReference().child("enrollment").child(uid);
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
                Toast.makeText(this, "Item 1 selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.setting:
                Toast.makeText(this, "Item 2 selected", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void CreateNewGrp(final String n, final String d, final String uid) {
        ref.child("Groups").child(n).setValue("")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            ref.child("Groups").child(n).child("name").setValue(n);
                            ref.child("Groups").child(n).child("description").setValue(d);
                            ref.child("Groups").child(n).child(currentUsername.replace(".","_")).setValue(currentUsername);
                            Toast.makeText(creategrp.this, "Study group created!", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    private void GetUsername(String uid) {
        uref.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    currentUsername = snapshot.child("email").getValue().toString();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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