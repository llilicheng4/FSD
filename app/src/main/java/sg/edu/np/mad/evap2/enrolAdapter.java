package sg.edu.np.mad.evap2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class enrolAdapter extends RecyclerView.Adapter<enrolViewHolder> {

    List<String> enrolmodList;
    String selectedmod;

    public enrolAdapter(List<String> list, String smod){
        enrolmodList = list;
        selectedmod = smod;
    }

    @NonNull
    @Override
    public enrolViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.rvenrol, parent,false);

        return new enrolViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull enrolViewHolder holder, int position) {
        final String information = enrolmodList.get(position);

        holder.mod.setText(information);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedmod = information;
            }
        });

    }

    @Override
    public int getItemCount() {
        return enrolmodList.size();
    }
}
