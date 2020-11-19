package sg.edu.np.mad.evap2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    public UserModel userModel;
    private static final String TAG = "HomeActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);
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
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CategoryFragment()).commit();
    }

    public void tvDiscussionClick(View view) {
        redirectActivity(this, discussionboard.class);
    }

    public void tvLogoutClick(View view) {
        FirebaseAuth.getInstance().signOut();
        redirectActivity(this, login.class);
    }
    public void tvModulesClick(View view){
        Log.d(TAG, "modules clicked ");
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ViewModuleFragment()).commit();
    };

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