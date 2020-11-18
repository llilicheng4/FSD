package sg.edu.np.mad.evap2;

import java.util.ArrayList;

public class TaskModel {
    private Integer taskId, categoryId;
    private String title, description, priority;
    private ArrayList<NoteModel> notes = new ArrayList<>();

    //Constructors
    public TaskModel() {
    }

    public TaskModel(Integer taskId, Integer categoryId, String title, String description, String priority) {
        this.taskId = taskId;
        this.categoryId = categoryId; //The category the task is under
        this.title = title;
        this.description = description;
        this.priority = priority;
        //Unlikely to pass in notes in constructor, would also require the set condition null check to be run.
    }

    //Accessors and Mutators
    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer id) {
        this.taskId = id;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer id) {
        this.categoryId = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public ArrayList<NoteModel> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<NoteModel> notes) {
        if (notes != null) {
            this.notes = notes;
        }
    }
}
