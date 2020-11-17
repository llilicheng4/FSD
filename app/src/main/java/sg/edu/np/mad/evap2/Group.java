package sg.edu.np.mad.evap2;

public class Group {
    private String grpname, grpdesc;

    public Group(){}

    public Group(String grpname, String grpdesc){
        this.grpname = grpname;
        this.grpdesc = grpdesc;
    }

    public String getGrpname() {
        return grpname;
    }

    public void setGrpname(String grpname) {
        this.grpname = grpname;
    }

    public String getGrpdesc() {
        return grpdesc;
    }

    public void setGrpdesc(String grpdesc) {
        this.grpdesc = grpdesc;
    }
}
