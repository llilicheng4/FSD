package sg.edu.np.mad.evap2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    public UserModel userModel;
    private static final String TAG = "MainActivity";
    RecyclerView viewIT;
    ArrayList<Module> modules = new ArrayList<>();
    private BrowseAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);
        viewIT = findViewById(R.id.ITrecyclerView);
        Module newModule = new Module("Full Stack Development", "full stack development is the development to both front and backend features", "You will learn important skills such as AGILE development","InfoTech");
        modules.add(newModule);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("modules").child("InfoTech");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, snapshot.toString());
                for(DataSnapshot moduleName : snapshot.getChildren()){
                    Log.d(TAG,  moduleName.toString());
                    Module newDbModule = new Module(moduleName.child("modName").getValue().toString(), moduleName.child("modDesc").getValue().toString(), moduleName.child("modDesc2").toString(), moduleName.child("moduleSchool").getValue().toString());
                    modules.add(newDbModule);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.v(TAG, "Error getting database data");
            }
        });

        mAdapter = new BrowseAdapter(modules, MainActivity.this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        viewIT.setLayoutManager(layoutManager);
        viewIT.setItemAnimator(new DefaultItemAnimator());
        viewIT.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String email = getIntent().getStringExtra("email");
        Log.d(TAG, "onStart: "+email);
    }

    public UserModel getUser() {
        return userModel;
    }

    public void ClickMenu(View view) {
        //open drawer
        MainActivity.openDrawer(drawerLayout);
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
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CategoryFragment()).commit();
    }

    public void tvDiscussionClick(View view) {
        drawerLayout.closeDrawer(GravityCompat.START);
        redirectActivity(this, discussionboard.class);
    }

    public void tvLogoutClick(View view) {
        FirebaseAuth.getInstance().signOut();
        redirectActivity(this, login.class);
    }
    public void tvModulesClick(View view){
        Log.d(TAG, "modules clicked ");
        drawerLayout.closeDrawer(GravityCompat.START);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ViewLearningFragment()).commit();
    };
    public void tvBrowseModulesClick(View view){
        drawerLayout.closeDrawer(GravityCompat.START);
        redirectActivity(this, MainActivity.class);
    }

    public void tvAccountClick(View view){
        drawerLayout.closeDrawer(GravityCompat.START);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new account()).commit();
    }

    public static void redirectActivity(Activity activity, Class aClass) {
        Intent intent = new Intent(activity, aClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);

    }


   /* public void InitUser() {
        //obtain database and user ID
        final String currentId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d(TAG, "InitUser: "+currentId);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(currentId);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //initiate userModel base
                String username = snapshot.child("username").getValue().toString();
                String email = snapshot.child("email").getValue().toString();
                String password = snapshot.child("password").getValue().toString();
                userModel = new UserModel(username, password, email);
                Log.d(TAG, "onDataChange: "+username+email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    */
    
}