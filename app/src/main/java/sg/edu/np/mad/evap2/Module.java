package sg.edu.np.mad.evap2;

import java.util.ArrayList;

public class Module {
    private String ModName;
    private String ModDesc;
    private ArrayList<LMaterials> lMaterialsList;

    public Module() {
    }

    public Module(String modName, String modDesc){
        this.ModName = modName;
        this.ModDesc = modDesc;
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

    public ArrayList<LMaterials> getlMaterialsList() {
        return lMaterialsList;
    }

    public void setlMaterialsList(ArrayList<LMaterials> lMaterialsList) {
        this.lMaterialsList = lMaterialsList;
    }
}
