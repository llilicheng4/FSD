package sg.edu.np.mad.evap2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    public UserModel userModel;
    private static final String TAG = "MainActivity";
    RecyclerView viewIT, viewBA;
    ArrayList<Module> ITModules, BAModules;
    private BrowseAdapter mAdapter;
    private BrowseAdapter browseAdapter;
    ImageView searchButton;
    EditText searchItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);
        viewIT = findViewById(R.id.ITrecyclerView);
        viewBA = findViewById(R.id.BArecyelerView);
        ITModules = new ArrayList<>();
        BAModules = new ArrayList<>();
        searchButton = findViewById(R.id.searchView);
        searchItem = findViewById(R.id.search);



        final DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference Modules = FirebaseDatabase.getInstance().getReference().child("modules");



        //Log.d(TAG, Modules.toString());
        /*Module newModule = new Module("full stack development", "full stack development is the development to both front and backend features", "You will learn important skills such as AGILE development","InfoTech");
        Module module1 = new Module("accounting", "Accounting is the process of recording financial transactions pertaining to a business. The accounting process includes summarizing, analyzing and reporting these transactions to oversight agencies, regulators and tax collection entities.", "Students will learn important skills in accounting", "Business");
        Module module2 = new Module("programming 1", "Programming is the process of creating a set of instructions that tell a computer how to perform a task. Programming can be done using a variety of computer programming languages, such as JavaScript, Python, and C++.", "Students will learn skills and concepts about programming with Python 3.9", "InfoTech");
        DBRef.child("modules").child(newModule.getModName()).setValue(newModule);
        DBRef.child("modules").child(module1.getModName()).setValue(module1);
        DBRef.child("modules").child(module2.getModName()).setValue(module2);*/

        Modules.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, String.valueOf(snapshot.getValue()));
                for(DataSnapshot module : snapshot.getChildren()){
                    Module newDbModule = new Module(module.child("modName").getValue().toString(), module.child("modDesc").getValue().toString(), module.child("modDesc2").getValue().toString(), module.child("moduleSchool").getValue().toString());
                    if(newDbModule.getModuleSchool().equals("Business")){
                        BAModules.add(newDbModule);
                    }
                    else{
                        ITModules.add(newDbModule);
                    }
                }
                mAdapter = new BrowseAdapter(ITModules, MainActivity.this);
                LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
                viewIT.setLayoutManager(layoutManager);
                viewIT.setItemAnimator(new DefaultItemAnimator());
                viewIT.setAdapter(mAdapter);

                browseAdapter = new BrowseAdapter(BAModules, MainActivity.this);
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    viewBA.setLayoutManager(mLayoutManager);
                    viewBA.setItemAnimator(new DefaultItemAnimator());
                    viewBA.setAdapter(browseAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.v(TAG, "Error getting database data");
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = searchItem.getText().toString();
                if (!item.isEmpty() || !item.equals("")) {
                    String lower = item.toLowerCase();
                    Query searchQuery = Modules.orderByChild("modName").startAt(lower).endAt(lower + "\uf8ff");

                    searchQuery.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot snap : snapshot.getChildren()){
                                Intent found = new Intent(MainActivity.this, ViewModuleActivity.class);
                                Module foundMod = new Module(snap.child("modName").getValue().toString(), snap.child("modDesc").getValue().toString(), snap.child("modDesc2").getValue().toString(), snap.child("moduleSchool").getValue().toString());
                                found.putExtra("Module", foundMod);
                                startActivity(found);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });

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