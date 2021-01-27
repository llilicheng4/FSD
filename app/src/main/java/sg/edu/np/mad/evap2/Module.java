package sg.edu.np.mad.evap2;

import java.io.Serializable;
import java.util.ArrayList;
public class Module implements Serializable {
    private String ModID;
    private String ModName;
    private String ModDesc;
    private String ModuleSchool;
    private ArrayList<LMaterial> lMaterialsList;

    public Module() {
    }

    public Module(String modName, String modDesc, String moduleSchool){
        this.ModName = modName;
        this.ModDesc = modDesc;
        this.ModuleSchool = moduleSchool;
    }

    public String getModuleSchool() {
        return ModuleSchool;
    }

    public void setModuleSchool(String moduleSchool) {
        ModuleSchool = moduleSchool;
    }

    public String getModName() {
        return ModName;
    }

    public void setModName(String modName) {
        ModName = modName;
    }

    public String getModDesc() {
        return ModDesc;
    }

    public void setModDesc(String modDesc) {
        ModDesc = modDesc;
    }

    public ArrayList<LMaterial> getlMaterialsList() {
        return lMaterialsList;
    }

    public void setlMaterialsList(ArrayList<LMaterial> lMaterialsList) {
        this.lMaterialsList = lMaterialsList;
    }

    public String getModID() {
        return ModID;
    }

    public void setModID(String modID) {
        ModID = modID;
    }
}
