package sg.edu.np.mad.evap2;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

public class ProjectFragment extends Fragment {
    /*
        Author: Zhao Yi
        Sections: All


        ProjectFragment
        Purpose: To display the kanban board.
        Accessed from: The navigation view of main activity as well as deleting a task from task
                       fragment.

        1. OnCreateView
            1.1 Edit project name [Line 118]
                 Once user exits editing text state, save into database and update project data and
                 refresh the navigation view.
            1.2 Change image [Line 136]
                 Proceed to gallery to pick an image to change the project icon in navigation view.
            1.3 Delete project [Line 149]
                 Run a confirmation alert, and delete project from database once confirmed before
                 refreshing the navigation view.
            1.4 Add new category [Line 179]
                 Add a category with default information to be edited, into the project data and database
                 before refreshing the recycler view.
        2. OnActivityResult [Line 193]
            Optimize the image and store into the database and refresh the navigation view.
        3. Update image in fragment [Line 228]
            Updates the image beside the project name in the fragment.
    */

    //Declare variables
    private EditText projectName;
    private ImageView projectIcon;
    private ProjectModel projectData;
    private NavigationView navigationView;
    private CategoryAdapter adapter;
    private Context context;
    private DBHandler dbHandler;
    View v;

    //1. OnCreateView
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Set reference to database
        dbHandler = new DBHandler(getActivity(), "PrOrganize.db", null, 1);
         v = inflater.inflate(R.layout.activity_view_personal_tasklist, container, false);
/*
        //Get required data
        Bundle bundle = getArguments();
        //projectData = dbHandler.getProject(bundle.getInt("projectId"));

        //Get view


        //Set reference to XML
        projectName = v.findViewById(R.id.newKanpanName);
        ImageView addCategory = v.findViewById(R.id.addKanpan);
        //navigationView = getActivity().findViewById(R.id.);

        //Set data
        context = getActivity();
        projectName.setText(projectData.getTitle());
        projectData.setCategories(dbHandler.getAllProjectCategories(projectData));

        //Set up recycler view to display categories with their tasks
        RecyclerView categoryRecyclerView = v.findViewById(R.id.kanPanRecyclerView);
        adapter = new CategoryAdapter(context, projectData, dbHandler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        categoryRecyclerView.setLayoutManager(layoutManager);
        categoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        categoryRecyclerView.setAdapter(adapter);


        //1.4 Add new category
        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Update current project data
                dbHandler.addCategoryToProject(projectData.getProjectId(), "New Title");
                projectData.getCategories().clear();
                projectData.getCategories().addAll(dbHandler.getAllProjectCategories(projectData));
                adapter.notifyDataSetChanged(); //Refresh recycler view
            }
        });

 */
        return v;

    }


}
