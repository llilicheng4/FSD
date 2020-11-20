package sg.edu.np.mad.evap2;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ViewModuleFragment extends Fragment {
    private TextView moduleName, moduleDesc;
    private MaterialAdapter mAdapter;
    private RecyclerView recyclerView;
    private Context context;
    private DatabaseReference databaseReference;
    private static String TAG = "ViewModFrag";
    private ArrayList<LMaterial> materials;
    View v;

    //1. OnCreateView
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_view_module, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        Module newModule = new Module("Full Stack Development", "full stack development is the development to both front and backend features");
        materials = new ArrayList<LMaterial>();
        LMaterial material = new LMaterial("Week 1 material", "work hard dont stop");
        materials.add(material);
        newModule.setlMaterialsList(materials);
        databaseReference.child("modules").child(newModule.getModName()).setValue(newModule);

        moduleName = v.findViewById(R.id.modName);
        moduleDesc = v.findViewById(R.id.modDesc);
        recyclerView = v.findViewById(R.id.materials);

        moduleName.setText(newModule.getModName());
        moduleDesc.setText(newModule.getModDesc());

        //context = getContext();
        mAdapter = new MaterialAdapter(materials, getActivity(), getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        return v;

    }


}