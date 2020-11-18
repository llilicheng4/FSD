package sg.edu.np.mad.evap2;

import java.util.ArrayList;

public class CategoryModel {
    private Integer categoryId,projectId;
    private String categoryTitle;
    private ArrayList<TaskModel> tasks = new ArrayList<>();

    //Constructors
    public CategoryModel(){}

    public CategoryModel(Integer categoryId,Integer projectId, String categoryTitle) {
        this.categoryId = categoryId;
        this.projectId = projectId; //The project the category is under.
        this.categoryTitle = categoryTitle;
        //Unlikely to pass in tasks in constructor, would also require the set condition null check to be run.
    }

    //Accessors and Mutators
    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer id) {
        this.categoryId = id;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer id) {
        this.projectId = id;
    }

    public String getTitle() {
        return categoryTitle;
    }

    public void setTitle(String name) {
        this.categoryTitle = name;
    }

    public ArrayList<TaskModel> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<TaskModel> tasks) {

        if (tasks != null) {
            this.tasks = tasks;
        }
    }
}