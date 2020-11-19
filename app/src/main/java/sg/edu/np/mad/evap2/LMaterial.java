package sg.edu.np.mad.evap2;

public class LMaterial {
    private String MaterialName;
    private String MaterialDesc;
    private boolean Done = false;

    public LMaterial() {
    }

    public LMaterial(String materialName, String materialDesc){
        this.MaterialName = materialName;
        this.MaterialDesc = materialDesc;
    }

    public String getMaterialName() {
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
    }
}
