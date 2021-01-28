package sg.edu.np.mad.evap2;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewLearningFragment extends Fragment {
    private TextView moduleName, moduleDesc;
    private MaterialAdapter mAdapter;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private Context context;
    private DatabaseReference databaseReference, secondarydbRef;
    private static String TAG = "ViewModFrag";
    private ArrayList<LMaterial> materials;
    View v;

    FirebaseOptions options;
    FirebaseApp app;
    FirebaseDatabase secondaryDB;

    //1. OnCreateView
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        container.removeAllViews();

        v = inflater.inflate(R.layout.fragment_view_module, container, false);
        context = getContext();

        //code to get 2nd firebase db
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId("1:783825586537:android:cdd90f1300165eee3195f8")
                .setApiKey("AIzaSyDw-fCVxxs_iA02zsPGb_PWkB3U205kQ-g")
                .setDatabaseUrl("https://p2-web-7da74-default-rtdb.firebaseio.com")
                .build();

        FirebaseApp.initializeApp(context, options, "secondary");
        app = FirebaseApp.getInstance("secondary");
        secondaryDB = FirebaseDatabase.getInstance(app);

        //firebase db reference
        secondarydbRef = FirebaseDatabase.getInstance(app).getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        Module newModule = new Module("Full Stack Development", "full stack development is the development to both front and backend features", "You will learn important skills such as AGILE development","InfoTech");
        materials = new ArrayList<LMaterial>();
        LMaterial material = new LMaterial("Week 1 material", "work hard play hard");
        materials.add(material);
        newModule.setlMaterialsList(materials);
        moduleName = v.findViewById(R.id.modName);
        moduleDesc = v.findViewById(R.id.modDesc);
        recyclerView = v.findViewById(R.id.materials);

        moduleName.setText(newModule.getModName());
        moduleDesc.setText(newModule.getModDesc());

        //gonna comment your rv code so that i can replace it with the one i am using
        /*mAdapter = new MaterialAdapter(materials, getActivity(), getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);*/

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        return v;

    }


    @Override
    public void onStart(){
        super.onStart();

        final DatabaseReference secondarydbRef = FirebaseDatabase.getInstance(app).getReference().child("Materials");

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<LMaterial>()
                        .setQuery(secondarydbRef.orderByChild("FileName").equalTo("Week 1"), LMaterial.class)
                        .build();

        FirebaseRecyclerAdapter<LMaterial, ViewLearningFragment.GroupsViewHolder> adapter
                = new FirebaseRecyclerAdapter<LMaterial, ViewLearningFragment.GroupsViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull final ViewLearningFragment.GroupsViewHolder groupsViewHolder, final int i, @NonNull LMaterial LMaterial) {
                String materialid = getRef(i).getKey();

                //get file name
                secondarydbRef.child(materialid).child("File").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        final String fileName = snapshot.child("FileName").getValue().toString();
                        groupsViewHolder.LMatname.setText(fileName);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                secondarydbRef.child(materialid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String uploaderName = snapshot.child("FileName").getValue().toString();
                        groupsViewHolder.uploadername.setText(uploaderName);

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

        public TextView LMatname, uploadername;
        public Button dlMat;

        public GroupsViewHolder(@NonNull View itemView) {
            super(itemView);
            LMatname = itemView.findViewById(R.id.learningMaterialName);
            uploadername = itemView.findViewById(R.id.learningMaterialDesc);
            dlMat = itemView.findViewById(R.id.learningDone);
        }
    }

}