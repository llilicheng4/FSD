package sg.edu.np.mad.evap2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class BrowseAdapter extends RecyclerView.Adapter<BrowseViewHolder> {
    ArrayList<Module> materialsArrayList;
    Activity activityMain;
    final String TAG = "browseRecycler";
    public BrowseAdapter(ArrayList<Module> LMaterials, Activity parentActivity) {
        materialsArrayList = LMaterials;
        activityMain = parentActivity;
    }

    @NonNull
    @Override
    public BrowseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v;
        if(materialsArrayList.get(0).getModuleSchool().equals("Business")){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_ba_mod, parent, false);
        }
        else{
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_browse_module, parent, false);
        }
        BrowseViewHolder viewHolder = new BrowseViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BrowseViewHolder holder, int position) {
        final Module lm = materialsArrayList.get(position);

        holder.MaterialName.setText(lm.getModName().toUpperCase());
        holder.MaterialDesc.setText(lm.getModDesc());
        holder.MaterialSchool.setText(lm.getModuleSchool());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewMod = new Intent(activityMain, ViewModuleActivity.class);
                viewMod.putExtra("Module", lm);
                activityMain.startActivity(viewMod);
                Log.d(TAG, "IT clicked");
            }
        });
    }

    @Override
    public int getItemCount() {
        return materialsArrayList.size();
    }
}


