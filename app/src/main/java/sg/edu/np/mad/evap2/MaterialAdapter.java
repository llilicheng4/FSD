package sg.edu.np.mad.evap2;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MaterialAdapter extends RecyclerView.Adapter<MaterialViewHolder> {
    ArrayList<LMaterials> materialsArrayList;
    Activity activityMain;
    String materialKey;

    public MaterialAdapter(ArrayList<LMaterials> LMaterials, Activity parentActivity){
        materialsArrayList = LMaterials;
        activityMain = parentActivity;
    }
    @NonNull
    @Override
    public MaterialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_learning_material, parent, false);
        MaterialViewHolder viewHolder = new MaterialViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MaterialViewHolder holder, int position) {
        final LMaterials lm = materialsArrayList.get(position);

        holder.MaterialName.setText(lm.getMaterialName());
        holder.MaterialDesc.setText(lm.getMaterialDesc());
        if(lm.isDone()){
            holder.MaterialDone.setText("Done");
        }
        else{
            holder.MaterialDone.setText("Not Done");
        }

    }

    @Override
    public int getItemCount() {
        return materialsArrayList.size();
    }
}
