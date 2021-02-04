package sg.edu.np.mad.evap2;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskViewHolder> {
    //Declare variables
    private UserModel projectData;
    private CategoryModel categoryData;
    private List<TaskModel> taskModelList;
    private DBHandler dbHandler;
    private Context context;

    //1. Constructor
    public TaskAdapter(Context context, UserModel projectData, CategoryModel categoryData, DBHandler dbHandler) {
        this.projectData = projectData;
        this.categoryData = categoryData;
        this.taskModelList = categoryData.getTasks();
        this.dbHandler = dbHandler;
        this.context = context;
    }

    //2. Create view holder
    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TaskViewHolder viewHolder;

        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_task,
                parent,
                false
        );

        viewHolder = new TaskViewHolder(view);
        return viewHolder;
    }

    //3. Bind data to view holder
    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        //Declare function variables;
        TaskModel task = taskModelList.get(position);

        //Set data
        String taskTitle = task.getTitle();
        holder.taskName.setText(taskTitle);

        /*String currentPriority = task.getPriority();
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
        }*/

        //Set required data as tags for their functions
        holder.task.setTag(task);
        holder.taskDelete.setTag(task);
        //holder.taskShift.setTag(task);

        //3.1 Open task information
        holder.task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Declare function variables;
                TaskModel task = (TaskModel) v.getTag();

                //Display task fragment
                Bundle bundle = new Bundle();
                bundle.putString("projectId", projectData.getEmail()); //Required data to identify project
                bundle.putInt("categoryId", categoryData.getCategoryId());
                bundle.putInt("taskId", task.getTaskId());


                /*
                TaskFragment taskObj = new TaskFragment();
                taskObj.setArguments(bundle);
                ((MainActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        taskObj).commit();

                //Change actionbar title
                ((MainActivity) context).getSupportActionBar().setTitle("Task Information");
                */
            }
        });

        //3.2 Delete task
        holder.taskDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Declare function variables;
                final TaskModel task = (TaskModel) v.getTag();

                //Confirmation alert
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Task?");
                builder.setMessage("Are you sure you want to permanently delete this task?");
                builder.setCancelable(false); //Closable without an option
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        //Delete task from database and current category data
                        dbHandler.deleteTask(task.getTaskId());
                        categoryData.getTasks().remove(task);
                        TaskAdapter.this.notifyDataSetChanged();
                    }

                    ;
                });
                builder.setNegativeButton("No", null);
                builder.show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //3.3 Shift task
        /*holder.taskShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Declare function variables;
                TaskModel task = (TaskModel) view.getTag();

                //Check if there are other categories to shift to
                if (dbHandler.getAllProjectCategories(projectData).size() > 1) {
                    //Display shift task fragment
                    Bundle bundle = new Bundle();
                    bundle.putInt("projectId", projectData.getProjectId()); //Required data to identify project
                    bundle.putInt("categoryId", categoryData.getCategoryId());
                    bundle.putInt("taskId", task.getTaskId());

                    ShiftTaskFragment taskObj = new ShiftTaskFragment();
                    taskObj.setArguments(bundle);
                    ((MainActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            taskObj).commit();

                    //Change actionbar title
                    ((MainActivity) context).getSupportActionBar().setTitle("Shift task to category");
                } else {
                    //Display error message
                    Toast.makeText(context, "No other category to shift to.", Toast.LENGTH_SHORT).show();
                }
            }
        });

         */
    }



    @Override
    public int getItemCount() {
        if (taskModelList != null) {
            return taskModelList.size();
        }
        return 0;
    }
}
