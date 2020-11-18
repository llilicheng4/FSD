package sg.edu.np.mad.evap2;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {
    //Declare variables
    private UserModel projectData;
    private List<CategoryModel> categoryModelList;
    private DBHandler dbHandler;
    private Context context;

    //1. Constructor
    public CategoryAdapter(Context context, UserModel projectData, DBHandler dbHandler) {
        this.projectData = projectData;
        this.categoryModelList = projectData.getCategories();
        this.dbHandler = dbHandler;
        this.context = context;
    }

    //2. Create view holder
    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CategoryViewHolder viewHolder;

        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_category,
                parent,
                false
        );
        viewHolder = new CategoryViewHolder(view);
        return viewHolder;
    }

    //3. Bind data to view holder
    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        //Declare function variables;
        CategoryModel category = categoryModelList.get(position);

        //Set data
        String categoryTitle = category.getTitle();
        holder.categoryName.setText(categoryTitle);

        category.setTasks(dbHandler.getAllCategoryTask(category));

        //Set up recycler view to display tasks
        TaskAdapter taskAdapter = new TaskAdapter(context, projectData, category, dbHandler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);

        RecyclerView taskRecyclerView = holder.taskRecyclerView;
        taskRecyclerView.setLayoutManager(layoutManager);
        taskRecyclerView.setItemAnimator(new DefaultItemAnimator());
        taskRecyclerView.setAdapter(taskAdapter);

        //Set required data as tags for their functions
        holder.categoryName.setTag(category);
        holder.categoryDelete.setTag(category);
        holder.categoryAdd.setTag(R.id.category, category);
        holder.categoryAdd.setTag(R.id.taskAdapter, taskAdapter);

        //3.1 Edit category name
        holder.categoryName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //If user exits text editing state.
                if (!hasFocus) {
                    CategoryModel category = ((CategoryModel) v.getTag());
                    category.setTitle(((EditText) v).getText().toString().trim());
                    dbHandler.updateProject(category);
                }
            }
        });

        //3.2 Delete category
        holder.categoryDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Declare function variables;
                final CategoryModel category = ((CategoryModel) v.getTag());

                //Confirmation alert
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Category?");
                builder.setMessage("Are you sure you want to permanently delete this category?");
                builder.setCancelable(false); //Closable without an option
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        //Delete category from database and current project data
                        dbHandler.deleteCat(category.getCategoryId());
                        projectData.getCategories().remove(category);
                        CategoryAdapter.this.notifyDataSetChanged(); //Refresh recycler view
                    }

                    ;
                });
                builder.setNegativeButton("No", null);
                builder.show();
            }
        });

        //3.3 Add new task
        holder.categoryAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryModel category = ((CategoryModel) v.getTag(R.id.category));
                dbHandler.addTaskToCategory(category.getCategoryId(), "New Task", "Add description", "None");

                //Update current category data
                category.getTasks().clear();
                category.getTasks().addAll(dbHandler.getAllCategoryTask(category));

                notifyDataSetChanged(); //Refresh recycler view
            }
        });
    }

    //4. Get item count
    @Override
    public int getItemCount() {
        if (categoryModelList != null) {
            return categoryModelList.size();
        }
        return 0;
    }
}