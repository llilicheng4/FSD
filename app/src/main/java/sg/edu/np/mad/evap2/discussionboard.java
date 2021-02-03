package sg.edu.np.mad.evap2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class discussionboard extends AppCompatActivity {

    DrawerLayout drawerLayout;

    ImageView add;

    TextView viewall;

    RecyclerView grprv, forumrv;
    RecyclerView.LayoutManager layoutManager;

    DatabaseReference ref, eref;
    FirebaseAuth mauth;

    String currentuserid, currentemail, selectedmodule, enmod;

    ListView lvnaviEn;

    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discussion_board);

        discussionboard.context = getApplicationContext();

        viewall = findViewById(R.id.tvViewall);

        drawerLayout = findViewById(R.id.drawer_layout);
        add = findViewById(R.id.btnAdd);

        grprv = findViewById(R.id.rvStudyGrp);
        forumrv = findViewById(R.id.rvForums);

        layoutManager = new LinearLayoutManager(this);
        grprv.setLayoutManager(layoutManager);

        mauth = FirebaseAuth.getInstance();

        currentuserid = mauth.getCurrentUser().getUid();
        currentemail = mauth.getCurrentUser().getEmail();

        ref = FirebaseDatabase.getInstance().getReference().child("Groups");
        eref = FirebaseDatabase.getInstance().getReference().child("Groups");
        final DatabaseReference enrolment = FirebaseDatabase.getInstance().getReference().child("enrollment").child(currentuserid);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(discussionboard.this, creategrp.class);
            }
        });

        viewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(discussionboard.this, forum.class);
            }
        });

        //navi bar for my module code
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

    public void ClickMenu(View view) {
        //open drawer
        openDrawer(drawerLayout);
    }

    private static void openDrawer(DrawerLayout drawerLayout) {
        //open drawer layout
        drawerLayout.openDrawer(GravityCompat.START);
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


    public static void redirectActivity(Activity activity, Class aClass){
        Intent intent = new Intent(activity,aClass);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        activity.startActivity(intent);
    }

    @Override
    public void onStart(){
        super.onStart();

        String fbemail = currentemail.replace(".","_");

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Group>()
                .setQuery(eref.orderByChild(fbemail).equalTo(currentemail), Group.class)
                .build();

        FirebaseRecyclerAdapter<Group, GroupsViewHolder> adapter
                = new FirebaseRecyclerAdapter<Group, GroupsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final GroupsViewHolder groupsViewHolder, int i, @NonNull Group group) {
                String grpid = getRef(i).getKey();
                ref.child(grpid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String name = snapshot.child("name").getValue().toString();
                        String desc = snapshot.child("description").getValue().toString();

                        groupsViewHolder.grpname.setText(name);
                        groupsViewHolder.grpdesc.setText(desc);
                        final String itemText = groupsViewHolder.grpname.getText().toString();

                        groupsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(discussionboard.this, grpchat.class);

                                intent.putExtra("groupname", itemText);
                                startActivity(intent);
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
            public GroupsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rvgrouplist, parent,false);
                GroupsViewHolder viewholder = new GroupsViewHolder(view);
                return viewholder;
            }
        };

        grprv.setAdapter(adapter);
        adapter.startListening();
        adapter.notifyDataSetChanged();
    }

    public static class GroupsViewHolder extends RecyclerView.ViewHolder{

        TextView grpname, grpdesc;

        public GroupsViewHolder(@NonNull View itemView) {
            super(itemView);

            grpname = itemView.findViewById(R.id.tvgrpname);
            grpdesc = itemView.findViewById(R.id.tvgrpdesc);
        }
    }

    private void Getemail(String uid) {
        eref.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                   currentemail  = snapshot.child("email").getValue().toString();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
