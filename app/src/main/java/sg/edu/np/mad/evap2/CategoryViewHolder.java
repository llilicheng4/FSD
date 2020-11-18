package sg.edu.np.mad.evap2;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryViewHolder extends RecyclerView.ViewHolder{

    public RecyclerView taskRecyclerView;
    public TextView categoryName;
    public ImageView categoryDelete, categoryAdd;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);

        taskRecyclerView = itemView.findViewById(R.id.taskRecyclerView);
        categoryName = itemView.findViewById(R.id.personal_task);
        categoryAdd = itemView.findViewById(R.id.addTask);
        categoryDelete = itemView.findViewById(R.id.deleteKanpan);
    }
}
