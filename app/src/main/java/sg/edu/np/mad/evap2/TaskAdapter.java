package sg.edu.np.mad.evap2;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskViewHolder> {
    private KanPanBoard kanPanBoard;
    private ArrayList<Task> taskList;
    private MyDBHandler dbHandler;
    private Context context;

    public TaskAdapter(Context context1, KanPanBoard kanPanBoard1, MyDBHandler dbHandler1){
        this.context = context1;
        this.kanPanBoard = kanPanBoard1;
        this.dbHandler = dbHandler1;
        this.taskList = kanPanBoard.getTaskList();
    }
    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TaskViewHolder viewHolder;

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_task, parent, false);

        viewHolder = new TaskViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, final int position) {
        Task task = taskList.get(position);

        String taskTitle = task.getTaskName();

        holder.taskName.setText(taskTitle);

        String currentPriority = task.getPriority();
        if(currentPriority.equals("impt")){
            holder.taskPriority.setBackgroundResource(R.drawable.bg_impt);
        }
        else if(currentPriority.equals("veryimpt")){
            holder.taskPriority.setBackgroundResource(R.drawable.bg_veryimpt);
        }
        else if(currentPriority.equals("notimpt")){
            holder.taskPriority.setBackgroundResource(R.drawable.bg_notimpt);
        }
        else{
            holder.taskPriority.setBackgroundResource(R.drawable.bg_layer3);
        }

        holder.taskDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Task task1 = taskList.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Task?");
                builder.setMessage("Are you sure you want to permanently delete this task?");
                builder.setCancelable(false); //Closable without an option
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        //Delete task from database and current category data
                        dbHandler.deleteTask(task1);
                        kanPanBoard.getTaskList().remove(task1);
                        TaskAdapter.this.notifyDataSetChanged();
                    }

                    ;
                });
                builder.setNegativeButton("No", null);
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
}
