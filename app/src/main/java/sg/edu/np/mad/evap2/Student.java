package sg.edu.np.mad.evap2;

public class Student extends UserData {
private KanPanBoard kanPanBoard;
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

    @Override
    public String getUsername() {
        return super.getUsername();
    }

    @Override
    public void setUsername(String username) {
        super.setUsername(username);
    }

    public TimeTable getTimeTable() {
        return timeTable;
    }

    public void setTimeTable(TimeTable timeTable) {
        this.timeTable = timeTable;
    }

    public KanPanBoard getKanPanBoard() {
        return kanPanBoard;
    }

    public void setKanPanBoard(KanPanBoard kanPanBoard) {
        this.kanPanBoard = kanPanBoard;
    }
}
