package sg.edu.np.mad.evap2;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BrowseViewHolder extends RecyclerView.ViewHolder{
    public TextView MaterialName, MaterialDesc, MaterialSchool;

    public BrowseViewHolder(@NonNull View itemView) {
        super(itemView);
        MaterialName = itemView.findViewById(R.id.textView10);
        MaterialSchool = itemView.findViewById(R.id.textView11);
        MaterialDesc = itemView.findViewById(R.id.textView12);
    }
}