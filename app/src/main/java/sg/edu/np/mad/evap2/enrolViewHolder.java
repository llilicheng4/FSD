package sg.edu.np.mad.evap2;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class enrolViewHolder extends RecyclerView.ViewHolder {
    public TextView mod;

    public enrolViewHolder(@NonNull View itemView) {
        super(itemView);
        mod = itemView.findViewById(R.id.tvEnmod);
    }
}
