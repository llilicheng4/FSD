package sg.edu.np.mad.evap2;

public class LMaterial {
    /*private String MaterialName;
    private String MaterialDesc;
    private boolean Done = false;*/
    private String modname, filename, filewExt, weekNum;

    public LMaterial() {
    }

    public LMaterial(String mname, String fname, String fnamewExt, String wNum){
        this.modname = mname;
        this.filename = fname;
        this.filewExt = fnamewExt;
        this.weekNum = wNum;
    }

    public String getModname() {
        return modname;
    }

    public void setModname(String modname) {
        this.modname = modname;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilewExt() {
        return filewExt;
    }

    public void setFilewExt(String filewExt) {
        this.filewExt = filewExt;
    }

    public String getWeekNum() {
        return weekNum;
    }

    public void setWeekNum(String weekNum) {
        this.weekNum = weekNum;
    }

    /*public String getMaterialName() {
        return MaterialName;
    }

    public void setMaterialName(String materialName) {
        MaterialName = materialName;
    }

    public boolean isDone() {
        return Done;
    }

    public void setDone(boolean done) {
        Done = done;
    }

    public String getMaterialDesc() {
        return MaterialDesc;
    }

    public void setMaterialDesc(String materialDesc) {
        MaterialDesc = materialDesc;
    }*/
}
