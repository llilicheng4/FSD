package sg.edu.np.mad.evap2;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class dlmaterialhistory extends AppCompatActivity {
    RecyclerView history;

    RecyclerView.LayoutManager layoutManager;

    Toolbar toolbar;

    String uid, selectedmodule, enmod;

    DrawerLayout drawerLayout;
    ListView lvnaviEn;

    private static Context context;

    //firebase code
    FirebaseAuth mauth;
    DatabaseReference historyRef;
    StorageReference mStorageRef;
    StorageReference ref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlmaterialhistory);

        history = findViewById(R.id.rvHistory);


        //get class context
        dlmaterialhistory.context = getApplicationContext();

        //get user's uid
        mauth = FirebaseAuth.getInstance();
        uid = mauth.getUid();

        //get the ref for download history child based on current user id
        historyRef = FirebaseDatabase.getInstance().getReference().child("Download history").child(uid);

        //get the storage ref for web version firebase
        mStorageRef = FirebaseStorage.getInstance("gs://p2-web-7da74.appspot.com/").getReference();

        //toolbar code
        toolbar = findViewById(R.id.forumbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Downloaded material history");


        //init rv
        layoutManager = new LinearLayoutManager(context);
        history.setLayoutManager(layoutManager);

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
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle button click here
        if (item.getItemId() == android.R.id.home) {
            setResult(Activity.RESULT_CANCELED);
            finish(); // close this activity and return to preview activity
        }
        return super.onOptionsItemSelected(item);
    }

    //code for recyclerview
    @Override
    public void onStart(){
        super.onStart();

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<LMaterial>()
                        .setQuery(historyRef.orderByChild("modname"), LMaterial.class)
                        .build();

        FirebaseRecyclerAdapter<LMaterial, dlmaterialhistory.GroupsViewHolder> adapter
                = new FirebaseRecyclerAdapter<LMaterial, dlmaterialhistory.GroupsViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull final dlmaterialhistory.GroupsViewHolder groupsViewHolder, final int i, @NonNull LMaterial LMaterial) {
                final String refid = getRef(i).getKey();

                historyRef.child(refid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        final String modname = snapshot.child("modname").getValue().toString();
                        final String filename = snapshot.child("filename").getValue().toString();
                        final String filewExt = snapshot.child("filewExt").getValue().toString();
                        final String weekNum = snapshot.child("weekNum").getValue().toString();
                        groupsViewHolder.fname.setText(filename);
                        groupsViewHolder.week.setText(weekNum);
                        groupsViewHolder.file.setText(filewExt);
                        groupsViewHolder.dlMat.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startDownload(modname, weekNum, filewExt, context);
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
            public dlmaterialhistory.GroupsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_learning_material, parent,false);
                dlmaterialhistory.GroupsViewHolder viewholder = new dlmaterialhistory.GroupsViewHolder(view);
                return viewholder;
            }
        };

        history.setAdapter(adapter);
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


    public void startDownload(String module, String week, final String fnameEXT, final Context context) {
        String[] parts = fnameEXT.split("\\.");
        final String extension = parts[1];

        ref = mStorageRef.child("Modules").child(module).child(week).child(fnameEXT).child(fnameEXT + "." + extension);
        Log.d("OUTPUT", String.valueOf(ref));

        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url = uri.toString();
                //split file into 2 part, the file name and the file extension

                downloadItems(context, fnameEXT  + ".", extension, DIRECTORY_DOWNLOADS, url);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
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
