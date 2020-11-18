package sg.edu.np.mad.evap2;

public class NoteModel {

    private Integer noteId, taskId;
    private String description;

    //Constructors
    public NoteModel(){}

    public NoteModel(Integer noteId, Integer taskId, String description) {
        this.noteId = noteId;
        this.taskId = taskId; //The task the note is under
        this.description = description;
    }

    //Accessors and mutators
    public Integer getNoteId() {
        return noteId;
    }

    public void setNoteId(Integer id) {
        this.noteId = id;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer id) {
        this.taskId = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
