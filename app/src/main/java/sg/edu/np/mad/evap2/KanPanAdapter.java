package sg.edu.np.mad.evap2;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class KanPanAdapter extends RecyclerView.Adapter<KanPanViewHolder> {
    //Declare variables
    private ArrayList<KanPanBoard> KanPanBoardList;
    private MyDBHandler DBHandler;
    private Context context;
    private Activity activity;

    public KanPanAdapter(Context Context, MyDBHandler DbHandler) {
        this.context = Context;
        this.DBHandler = DbHandler;
        this.KanPanBoardList = DBHandler.getAllKanPan();
    }

    @NonNull
    @Override
    public KanPanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        KanPanViewHolder viewHolder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_kanban, parent, false);
        viewHolder = new KanPanViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull KanPanViewHolder holder, final int position) {
        KanPanBoard kanPanBoard = KanPanBoardList.get(position);
        String kanPanTitle = kanPanBoard.getKpTitle();
        holder.kanPanName.setText(kanPanTitle);
        kanPanBoard.setTaskList(DBHandler.getTasksFromKanPan(kanPanBoard));

        TaskAdapter taskAdapter = new TaskAdapter(context, kanPanBoard, DBHandler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);

        RecyclerView taskRecyclerView = holder.taskRecyclerView;
        taskRecyclerView.setLayoutManager(layoutManager);
        taskRecyclerView.setItemAnimator(new DefaultItemAnimator());
        taskRecyclerView.setAdapter(taskAdapter);

        holder.kanPanDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final KanPanBoard kanPanBoard1 = KanPanBoardList.get(position);
                //Confirmation alert
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Category?");
                builder.setMessage("Are you sure you want to permanently delete this category?");
                builder.setCancelable(false); //Closable without an option
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        //Delete category from database and current project data
                        DBHandler.deleteKanPan(kanPanBoard1);
                        KanPanBoardList.remove(kanPanBoard1);
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("No", null);
                builder.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return KanPanBoardList.size();
    }
}
