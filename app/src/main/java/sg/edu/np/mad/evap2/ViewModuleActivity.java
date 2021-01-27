package sg.edu.np.mad.evap2;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ViewModuleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_module);
        Intent data = getIntent();
        Module viewedModule = (Module)data.getSerializableExtra("Module");
        viewedModule.getModName();
    }
}