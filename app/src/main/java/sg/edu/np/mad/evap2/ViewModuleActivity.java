package sg.edu.np.mad.evap2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ViewModuleActivity extends AppCompatActivity {
    final String TAG = "viewModuleActivity";
    ImageView arrowBack;
    TextView title, name, nameOfMod, description1, description2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_module);
        arrowBack = findViewById(R.id.imageView3);
        title = findViewById(R.id.textView8);
        name = findViewById(R.id.textView13);
        nameOfMod = findViewById(R.id.textView16);
        description1 = findViewById(R.id.textView15);
        description2 = findViewById(R.id.textView19);
        Intent data = getIntent();
        Module viewedModule = (Module)data.getSerializableExtra("Module");
        String moduleName = viewedModule.getModName();
        String modDesc = viewedModule.getModDesc();
        String modDesc2 = viewedModule.getModDesc2();

        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        title.setText(moduleName.toUpperCase());
        name.setText(moduleName.toUpperCase());
        nameOfMod.setText(moduleName);
        description1.setText(modDesc);
        description2.setText(modDesc2);
    }
}