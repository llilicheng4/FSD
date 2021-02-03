package sg.edu.np.mad.evap2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

public class forum extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView rvForum;
    RecyclerView.LayoutManager layoutManager;

    DatabaseReference ref, eref;
    FirebaseAuth mauth;

    String currentuserid, currentemail, selectedmodule, enmod, uid;

    DrawerLayout drawerLayout;
    ListView lvnaviEn;

    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum);

        forum.context = getApplicationContext();

        rvForum = findViewById(R.id.rvforum);

        toolbar = findViewById(R.id.forumbar);

        mauth = FirebaseAuth.getInstance();

        uid = mauth.getUid();

        //toolbar code
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Forum");

        //rv code here
        layoutManager = new LinearLayoutManager(this);
        rvForum.setLayoutManager(layoutManager);

        ref = FirebaseDatabase.getInstance().getReference().child("Forums");
        eref = FirebaseDatabase.getInstance().getReference().child("Forums");

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
        inflater.inflate(R.menu.forummenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addforum:
                Intent i = new Intent(forum.this, addforum.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart(){
        super.onStart();

        //String fbemail = currentemail.replace(".","_");

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<forumclass>()
                        .setQuery(eref, forumclass.class)
                        .build();

        FirebaseRecyclerAdapter<forumclass, forum.GroupsViewHolder> adapter
                = new FirebaseRecyclerAdapter<forumclass, forum.GroupsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final forum.GroupsViewHolder groupsViewHolder, int i, @NonNull forumclass forum) {
                String grpid = getRef(i).getKey();
                ref.child(grpid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String title = snapshot.child("title").getValue().toString();
                        String content = snapshot.child("content").getValue().toString();
                        String category = snapshot.child("category").getValue().toString();
                        String status = snapshot.child("status").getValue().toString();
                        String answer = snapshot.child("noofanswers").getValue().toString();

                        groupsViewHolder.title.setText(title);
                        groupsViewHolder.content.setText(content);
                        groupsViewHolder.status.setText(status);
                        groupsViewHolder.category.setText(category);
                        groupsViewHolder.noofanswers.setText(answer);

                        final String titleText = groupsViewHolder.title.getText().toString();
                        final String contentText = groupsViewHolder.content.getText().toString();
                        final String categoryText = groupsViewHolder.category.getText().toString();

                        groupsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(forum.this, forumchat.class);

                                intent.putExtra("title", titleText);
                                intent.putExtra("content", contentText);
                                intent.putExtra("category", categoryText);

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
            public forum.GroupsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rvforum, parent,false);
                forum.GroupsViewHolder viewholder = new forum.GroupsViewHolder(view);
                return viewholder;
            }
        };

        rvForum.setAdapter(adapter);
        adapter.startListening();
        adapter.notifyDataSetChanged();
    }

    public static class GroupsViewHolder extends RecyclerView.ViewHolder{

        TextView title, content, status, noofanswers, category;

        public GroupsViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.tvTitle);
            content = itemView.findViewById(R.id.tvQuestion);
            status = itemView.findViewById(R.id.tvAnswered);
            noofanswers = itemView.findViewById(R.id.tvVoteCount);
            category = itemView.findViewById(R.id.tvModule);
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