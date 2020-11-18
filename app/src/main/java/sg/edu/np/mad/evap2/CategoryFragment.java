package sg.edu.np.mad.evap2;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class CategoryFragment extends Fragment {
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
    private NavigationView navigationView;
    private CategoryAdapter adapter;
    private Context context;
    private DBHandler dbHandler;
    private static String TAG = "Cat";
    View v;

    //1. OnCreateView
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Set reference to database
        dbHandler = new DBHandler(getActivity(), "PrOrganize.db", null, 1);
         v = inflater.inflate(R.layout.activity_view_personal_tasklist, container, false);

        //Get required data
        final String email = getActivity().getIntent().getStringExtra("email");

        final UserModel user = new UserModel();
        user.setEmail(email);
        Log.d(TAG, ": "+ user.getEmail());
        final ArrayList<CategoryModel> categoryModels = dbHandler.getUserCategories(email);
        user.setCategories(categoryModels);

        //Set reference to XML
        projectName = v.findViewById(R.id.newKanpanName);
        ImageView addCategory = v.findViewById(R.id.addKanpan);
        //navigationView = getActivity().findViewById(R.id.);

        //Set up recycler view to display categories with their tasks
        RecyclerView categoryRecyclerView = v.findViewById(R.id.kanPanRecyclerView);
        adapter = new CategoryAdapter(context, user, dbHandler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        categoryRecyclerView.setLayoutManager(layoutManager);
        categoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        categoryRecyclerView.setAdapter(adapter);


        //1.4 Add new category
        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CategoryModel categoryModel = new CategoryModel(categoryModels.size()+1, "new title");
                //Update current project data
                dbHandler.addCatToUser(user, categoryModel);

                user.getCategories().clear();
                user.getCategories().addAll(dbHandler.getUserCategories(user.getEmail()));
                Log.d(TAG, ": "+ dbHandler.getUserCategories(user.getEmail()).toString());
                Log.d(TAG, "onClick: "+user.getCategories().toString());
                adapter.notifyDataSetChanged(); //Refresh recycler view
            }
        });

        return v;

    }


}
