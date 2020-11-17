package sg.edu.np.mad.evap2;

import java.util.ArrayList;
import java.util.List;

public class KanPanBoard {
    private Integer KpID;
    private String KpTitle;
    private ArrayList<Task> TaskList;

    public KanPanBoard(Integer iD, String title) {
        this.KpID = iD;
        this.KpTitle = title;
    }

    public KanPanBoard() {

    }

    public ArrayList<Task> getTaskList() {
        return TaskList;
    }

    public void setTaskList(ArrayList<Task> taskList) {
        TaskList = taskList;
    }

    public Integer getKpID() {
        return KpID;
    }

    public void setKpID(Integer kpID) {
        KpID = kpID;
    }

    public String getKpTitle() {
        return KpTitle;
    }

    public void setKpTitle(String kpTitle) {
        KpTitle = kpTitle;
    }
}
