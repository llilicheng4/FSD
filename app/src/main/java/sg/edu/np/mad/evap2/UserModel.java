package sg.edu.np.mad.evap2;

import java.io.Serializable;
import java.util.ArrayList;

public class UserModel implements Serializable {
    private String name, email, password;
    private ArrayList<CategoryModel> categoryModels;
    private ArrayList<Module> modulesTaken;

    //Constructors
    public UserModel() {
    }

    public UserModel(String name, String password, String email) {
        this.name = name;
        this.email = email;
        this.password = password;
        //Unlikely to pass in projects in constructor, would also require the set condition null check to be run.
    }

    //Accessors and Mutators
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<CategoryModel> getCategories() {
        return categoryModels;
    }

    public void setCategories(ArrayList<CategoryModel> categoryModels) {
        this.categoryModels = categoryModels;
    }

    public ArrayList<Module> getModulesTaken() {
        return modulesTaken;
    }

    public void setModulesTaken(ArrayList<Module> modulesTaken) {
        this.modulesTaken = modulesTaken;
    }
}
