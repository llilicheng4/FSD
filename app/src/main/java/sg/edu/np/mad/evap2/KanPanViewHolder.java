package sg.edu.np.mad.evap2;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class KanPanViewHolder extends RecyclerView.ViewHolder{

    public RecyclerView taskRecyclerView;
    public EditText kanPanName;
    public ImageView kanPanDelete, kanPanAdd;

    public KanPanViewHolder(@NonNull View itemView) {
        super(itemView);

        taskRecyclerView = itemView.findViewById(R.id.taskRecyclerView);
        kanPanName = itemView.findViewById(R.id.personal_task);
        kanPanDelete = itemView.findViewById(R.id.deleteKanpan);
        kanPanAdd = itemView.findViewById(R.id.addKanpan);
    }
}
