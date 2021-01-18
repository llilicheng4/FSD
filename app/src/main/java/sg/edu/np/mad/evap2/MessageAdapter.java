package sg.edu.np.mad.evap2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MsgViewHolder> {
    private List<forumchatclass> userMessageList;
    FirebaseAuth mauth;
    DatabaseReference uRef;

    public MessageAdapter (List<forumchatclass> userMessageList){
        this.userMessageList = userMessageList;
    }

    public class MsgViewHolder extends RecyclerView.ViewHolder{
        public TextView notetitle, username, desc, forumchatcontent;

        public MsgViewHolder(@NonNull View itemView) {
            super(itemView);

            notetitle = itemView.findViewById(R.id.tvnotetitle);
            username = itemView.findViewById(R.id.tvUsername);
            desc = itemView.findViewById(R.id.tvDesc);

        }
    }

    @NonNull
    @Override
    public MsgViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rvforumchat, parent, false);

        return new MsgViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MsgViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return userMessageList.size();
    }


}
