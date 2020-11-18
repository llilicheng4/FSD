package sg.edu.np.mad.evap2;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MaterialViewHolder extends RecyclerView.ViewHolder{
    public TextView MaterialName, MaterialDesc;
    public Button MaterialDone;

    public MaterialViewHolder(@NonNull View itemView) {
        super(itemView);
        MaterialName = itemView.findViewById(R.id.learningMaterialName);
        MaterialDesc = itemView.findViewById(R.id.learningMaterialDesc);
        MaterialDone = itemView.findViewById(R.id.learningDone);
    }
}
