package sg.edu.np.mad.evap2;

import java.util.ArrayList;

public class ProjectModel {
    public Integer projectId;
    public String name,title;
    private byte[] image;
    private ArrayList<CategoryModel> categories = new ArrayList<>();

    //Constructors
    public ProjectModel(){}

    //For default icon with no selected image to avoid storing a repeated icon when it can be implemented with
    //if else conditions
    public ProjectModel(Integer id, String name, String title) {
        this.projectId = id;
        this.name = name; //The user the project is under.
        this.title = title;
        //Unlikely to pass in categories in constructor, would also require the set condition null check to be run.
    }

    //For with selected image
    public ProjectModel(Integer id, String name, String title, byte[] image) {
        this.projectId = id;
        this.name = name; //The user the project is under.
        this.title = title;
        this.image = image;
        //Unlikely to pass in categories in constructor, would also require the set condition null check to be run.
    }

    //Accessors and Mutators
    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer id) {
        this.projectId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public ArrayList<CategoryModel> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<CategoryModel> categories) {
        if (categories != null) {
            this.categories = categories;
        }
    }
}
