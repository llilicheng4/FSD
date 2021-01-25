package sg.edu.np.mad.evap2;

public class accountClass {
    private String scheduleday, scheduleduration, schedulelocation, module;

    public accountClass(){}

    public accountClass(String sday, String sduration, String slocation, String m){
        this.scheduleday = sday;
        this.scheduleduration = sduration;
        this.schedulelocation = slocation;
        this.module = m;
    }

    public String getScheduleday() {
        return scheduleday;
    }

    public void setScheduleday(String scheduleday) {
        this.scheduleday = scheduleday;
    }

    public String getScheduleduration() {
        return scheduleduration;
    }

    public void setScheduleduration(String scheduleduration) {
        this.scheduleduration = scheduleduration;
    }

    public String getSchedulelocation() {
        return schedulelocation;
    }

    public void setSchedulelocation(String schedulelocation) {
        this.schedulelocation = schedulelocation;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }
}
