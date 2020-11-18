package sg.edu.np.mad.evap2;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class TaskViewHolder extends RecyclerView.ViewHolder {
    public TextView taskName, taskPriority;
    public ConstraintLayout task;
    public ImageView taskDelete;

    public TaskViewHolder(@NonNull View itemView) {
        super(itemView);
        taskName = itemView.findViewById(R.id.TaskName);
        taskPriority = itemView.findViewById(R.id.taskPriority);
        taskDelete = itemView.findViewById(R.id.taskDelete);
        task = itemView.findViewById(R.id.task);
    }
}
