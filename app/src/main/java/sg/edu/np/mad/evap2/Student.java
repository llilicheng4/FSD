package sg.edu.np.mad.evap2;

public class Student extends UserModel {
private TimeTable timeTable;

public Student(){};

    public Student(String myUsername, String myEmail, String myPwd) {
        super(myUsername, myEmail, myPwd);
    }

    @Override
    public String getEmail() {
        return super.getEmail();
    }

    @Override
    public void setEmail(String email) {
        super.setEmail(email);
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public void setPassword(String password) {
        super.setPassword(password);
    }

    public TimeTable getTimeTable() {
        return timeTable;
    }

    public void setTimeTable(TimeTable timeTable) {
        this.timeTable = timeTable;
    }
}
