package sg.edu.np.mad.evap2;

public class Task {
    private Integer TaskID;
    private Integer KpiD;
    private String TaskContent;
    private String TaskName;
    private String Priority;

    public Task(Integer taskID, Integer kpID, String taskName, String taskContent, String priority) {
        this.TaskID = taskID;
        this.KpiD = kpID;
        this.TaskName = taskName;
        this.TaskContent = taskContent;
        this.Priority = priority;
    }

    public Task() {

    }

    public Integer getTaskID() {
        return TaskID;
    }

    public void setTaskID(Integer taskID) {
        TaskID = taskID;
    }

    public String getTaskContent() {
        return TaskContent;
    }

    public void setTaskContent(String taskContent) {
        TaskContent = taskContent;
    }

    public String getTaskName() {
        return TaskName;
    }

    public void setTaskName(String taskName) {
        TaskName = taskName;
    }

    public Integer getKpiD() {
        return KpiD;
    }

    public void setKpiD(Integer kpiD) {
        KpiD = kpiD;
    }

    public String getPriority() {
        return Priority;
    }

    public void setPriority(String priority) {
        Priority = priority;
    }
}
