package sg.edu.np.mad.evap2;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class ViewLearningFragment extends AppCompatActivity {
    DrawerLayout drawerLayout;
    private static Context context;
    private TextView moduleName, moduleDesc;
    private MaterialAdapter mAdapter;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    Toolbar toolbar;

    private DatabaseReference databaseReference, secondarydbRef;
    private static String TAG = "ViewModFrag";
    private ArrayList<LMaterial> materials;
    View v;

    StorageReference mStorageRef;
    StorageReference ref;

    FirebaseOptions options;
    FirebaseApp app;
    FirebaseDatabase secondaryDB;
    FirebaseAuth auth;

    String userID, selectedmodule, enmod;

    ListView lvnaviEn;

    Module viewedMod;
    ArrayList<Module> allMods;

    //1. OnCreateView

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_view_module);
        ViewLearningFragment.context = getApplicationContext();
        allMods = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        userID = auth.getUid();
        Bundle selectedmod = getIntent().getExtras();

        if(selectedmod != null){
            selectedmodule = selectedmod.getString("selectmodule");
        }
        //code to get 2nd firebase db
        final FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId("1:783825586537:android:cdd90f1300165eee3195f8")
                .setApiKey("AIzaSyDw-fCVxxs_iA02zsPGb_PWkB3U205kQ-g")
                .setDatabaseUrl("https://p2-web-7da74-default-rtdb.firebaseio.com")
                .build();

        FirebaseApp.initializeApp(context, options, "secondary");
        app = FirebaseApp.getInstance("secondary");
        secondaryDB = FirebaseDatabase.getInstance(app);

        //firebase db reference
        secondarydbRef = FirebaseDatabase.getInstance(app).getReference();
        mStorageRef = FirebaseStorage.getInstance("gs://p2-web-7da74.appspot.com/").getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference enrolment = FirebaseDatabase.getInstance().getReference().child("enrollment").child(userID);

        Module newModule = new Module("FSD","Full Stack Development", "full stack development is the development to both front and backend features", "You will learn important skills such as AGILE development", "InfoTech");
        Module module1 = new Module("ACC1", "accounting 1", "Accounting is the process of recording financial transactions pertaining to a business. The accounting process includes summarizing, analyzing and reporting these transactions to oversight agencies, regulators and tax collection entities.", "Students will learn important skills in accounting", "Business");
        Module module2 = new Module("PRG1","programming 1", "Programming is the process of creating a set of instructions that tell a computer how to perform a task. Programming can be done using a variety of computer programming languages, such as JavaScript, Python, and C++.", "Students will learn skills and concepts about programming with Python 3.9", "InfoTech");
        Module module3 = new Module("FMATH", "further mathematics", "Further Mathematics is the title given to a number of advanced secondary mathematics courses. The term Higher and Further Mathematics, and the term Advanced Level Mathematics, may also refer to any of several advanced mathematics courses at many institutions.", "This would include (depending on the institution) courses in analysis (real analysis, complex analysis, functional analysis, etc), courses in modern algebra (group theory, field theory, galois theory, etc), geometry (projective geometry, differential geometry, etc)", "Engineering");
        allMods.add(newModule);
        allMods.add(module1);
        allMods.add(module2);
        allMods.add(module3);


        materials = new ArrayList<LMaterial>();
        //LMaterial material = new LMaterial("Week 1 material", "work hard play hard");
        //materials.add(material);
        //newModule.setlMaterialsList(materials);
        /*databaseReference.child("modules").child(newModule.getModuleSchool()).child(newModule.getModName()).setValue(newModule).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "success");
            }
        });*/

        /*LMaterial material = new LMaterial("Week 1 material", "work hard play hard");
        materials.add(material);
        newModule.setlMaterialsList(materials);*/
        moduleName = findViewById(R.id.modName);
        moduleDesc = findViewById(R.id.modDesc);
        recyclerView = findViewById(R.id.materials);
        drawerLayout = findViewById(R.id.drawer_layout);

        toolbar = findViewById(R.id.forumbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(selectedmodule);

        for (Module mod:allMods
        ) {
            if(mod.getModID().equals(selectedmodule)){
                viewedMod = mod;
                moduleName.setText(viewedMod.getModName());
                moduleDesc.setText(viewedMod.getModDesc());
            }
        }
        //gonna comment your rv code so that i can replace it with the one i am using
        /*mAdapter = new MaterialAdapter(materials, getActivity(), getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);*/

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

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
                        //kills 2nd firebase instance
                        try{
                            FirebaseApp.getInstance("secondary").delete();
                        }
                        catch(IllegalStateException e){
                            selectedmodule = parent.getItemAtPosition(position).toString();
                            Log.d("Out", selectedmodule);
                            Intent intent = new Intent(context, ViewLearningFragment.class);
                            intent.putExtra("selectmodule", selectedmodule);

                            drawerLayout.closeDrawer(GravityCompat.START);

                            startActivity(intent);
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //kills 2nd firebase instance
    @Override
    protected void onStop() {
        super.onStop();
        try{
            FirebaseApp.getInstance("secondary").delete();
        }
        catch(IllegalStateException e){

        }

    }

    @Override
    public void onStart(){
        super.onStart();
        final DatabaseReference secondarydbRef = FirebaseDatabase.getInstance(app).getReference().child("Materials");

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<LMaterial>()
                        .setQuery(secondarydbRef.orderByChild("ModuleName").equalTo(selectedmodule), LMaterial.class)
                        .build();

        FirebaseRecyclerAdapter<LMaterial, ViewLearningFragment.GroupsViewHolder> adapter
                = new FirebaseRecyclerAdapter<LMaterial, ViewLearningFragment.GroupsViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull final ViewLearningFragment.GroupsViewHolder groupsViewHolder, final int i, @NonNull LMaterial LMaterial) {
                final String materialid = getRef(i).getKey();

                //get file name
                /*secondarydbRef.child(materialid).child("File").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        final String fileName = snapshot.child("FileName").getValue().toString();
                        groupsViewHolder.file.setText(fileName);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });*/

                secondarydbRef.child(materialid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        final String modname = snapshot.child("ModuleName").getValue().toString();
                        final String filename = snapshot.child("FileName").getValue().toString();
                        final String filewExt = snapshot.child("NamewExt").getValue().toString();
                        final String weekNum = snapshot.child("WeekNumber").getValue().toString();
                        groupsViewHolder.fname.setText(filename);
                        groupsViewHolder.week.setText(weekNum);
                        groupsViewHolder.file.setText(filewExt);
                        groupsViewHolder.dlMat.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startDownload(modname, weekNum, filewExt, filewExt, context);
                                LMaterial material = new LMaterial(modname, filename, filewExt, weekNum);

                                databaseReference.child("Download history").child(userID).push().setValue(material);
                                Toast.makeText(context,"Downloading file", Toast.LENGTH_SHORT).show();
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
            public ViewLearningFragment.GroupsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_learning_material, parent,false);
                ViewLearningFragment.GroupsViewHolder viewholder = new ViewLearningFragment.GroupsViewHolder(view);
                return viewholder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.notifyDataSetChanged();
    }

    public static class GroupsViewHolder extends RecyclerView.ViewHolder{

        public TextView week, fname, file;
        public Button dlMat;

        public GroupsViewHolder(@NonNull View itemView) {
            super(itemView);
            week = itemView.findViewById(R.id.tvWeek);
            fname = itemView.findViewById(R.id.tvFilename);
            file = itemView.findViewById(R.id.tvFile);
            dlMat = itemView.findViewById(R.id.learningDone);
        }
    }

    //code for downloading file
    public void downloadItems(Context context, String filename, String fileExtension, String destinationDirectory, String url) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, filename + fileExtension);

        downloadManager.enqueue(request);
    }


    public void startDownload(String module, String week, final String filenameEXT,final String fnameEXT, final Context context) {
        String[] parts = fnameEXT.split("\\.");
        final String extension = parts[1];
        ref = mStorageRef.child("Modules").child(module).child(week).child(filenameEXT).child(fnameEXT + "." + extension);
        Log.d("OUTPUT", String.valueOf(ref));

        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url = uri.toString();

                //split file into 2 part, the file name and the file extension
                /*String[] parts = fnameEXT.split("\\.");
                String extension = parts[1];*/
                downloadItems(context, filenameEXT , "."+extension, DIRECTORY_DOWNLOADS, url);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void ClickMenu(View view) {
        //open drawer
        ViewLearningFragment.openDrawer(drawerLayout);
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