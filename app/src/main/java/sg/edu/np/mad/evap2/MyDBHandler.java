package sg.edu.np.mad.evap2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MyDBHandler extends SQLiteOpenHelper{

    private static final String FILENAME = "MyDBHandler.java";
    private static final String TAG = "evaDB";
    public static String DATABASE_NAME = "EvaDB.db";
    public static int DATABASE_VERSION = 1;
    //KANPAN
    private static final String KANPAN = "KanPan";
    private static final String COLUMN_KPTITLE = "KanPanTitle";

    //TASKS
    private static final String TASKS = "Tasks";
    private static final String COLUMN_TASKID = "TaskID";
    private static final String COLUMN_TASKDESC = "TaskDesc";
    private static final String COLUMN_TASKTITLE = "TaskTitle";
    public static final String COLUMN_TASKCONTENT = "TaskContent";
    public static final String COLUMN_PRIORITY = "Priority";
    private static final String COLUMN_KPID = "KanPanID";


    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        /* HINT:
            This is used to init the database.
         */
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_TASK_TABLE = "CREATE TABLE " + TASKS +
                "(" + COLUMN_TASKID + " INTEGER NOT NULL," +
                COLUMN_KPID+ " INTEGER NOT NULL," +
                COLUMN_TASKTITLE + " TEXT NOT NULL," +
                COLUMN_TASKDESC + " TEXT NOT NULL," +
                COLUMN_PRIORITY + " TEXT NOT NULL" + ")";

        db.execSQL(CREATE_TASK_TABLE);

        String CREATE_KANPANTABLE = "CREATE TABLE " + KANPAN +
                "(" + COLUMN_KPID + " INTEGER NOT NULL," +
                COLUMN_KPTITLE + " TEXT NOT NULL" + ")";

        db.execSQL(CREATE_KANPANTABLE);


    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        /* HINT:
            This is triggered if there is a new version found. ALL DATA are replaced and irreversible.
         */
        db.execSQL("DROP TABLE IF EXISTS " + TASKS);
        onCreate(db);

    }
    //function to add kanpan to local db
    public void addKanPan(KanPanBoard kanPanBoard)
    {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();


        values.put(COLUMN_KPID, kanPanBoard.getKpID());
        values.put(COLUMN_KPTITLE, kanPanBoard.getKpTitle());

        db.insert(KANPAN, null, values);


        Log.v(TAG, FILENAME + ": Adding data for Database: " + values.toString());


        db.close();
    }
    //function to add task to local db
    public void addTask(Task task)
    {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();


            values.put(COLUMN_TASKID, task.getTaskID());
            values.put(COLUMN_KPID, task.getKpiD());
            values.put(COLUMN_TASKTITLE, task.getTaskName());
            values.put(COLUMN_TASKCONTENT, task.getTaskContent());
            values.put(COLUMN_PRIORITY, task.getPriority());
            db.insert(TASKS, null, values);


            Log.v(TAG, FILENAME + ": Adding data for Database: " + values.toString());


        db.close();
    }

    public ArrayList<KanPanBoard> getAllKanPan(){
        String query = "SELECT * FROM "+KANPAN;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        KanPanBoard queryData = new KanPanBoard();

        ArrayList<KanPanBoard> KanPanList = new ArrayList<KanPanBoard>();

        if(cursor.moveToFirst()){
            do{
                queryData.setKpID(cursor.getInt(0));
                queryData.setKpTitle(cursor.getString(1));
                KanPanList.add(queryData);
            }while(cursor.moveToFirst());
        }
        return KanPanList;
    }


    public ArrayList<Task> getAllTasks(){
        String query = "SELECT * FROM "+TASKS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Task queryData = new Task();

        ArrayList<Task> TaskList = new ArrayList<Task>();

        if(cursor.moveToFirst()){
            do{
                queryData.setTaskID(cursor.getInt(0));
                queryData.setKpiD(cursor.getInt(1));
                queryData.setTaskName(cursor.getString(2));
                queryData.setTaskContent(cursor.getString(3));
                queryData.setPriority(cursor.getString(4));
                TaskList.add(queryData);
            }while(cursor.moveToFirst());
        }
        return TaskList;
    }

    public ArrayList<Task> getTasksFromKanPan(KanPanBoard kanPanBoard){
        Integer kanPanID = kanPanBoard.getKpID();
        String query = "SELECT * FROM "+TASKS+" WHERE "+ COLUMN_KPID + "=\""+ kanPanID +"\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Task queryData = new Task();

        ArrayList<Task> TaskList = new ArrayList<Task>();

        if(cursor.moveToFirst()){
            do{
                queryData.setTaskID(cursor.getInt(0));
                queryData.setKpiD(cursor.getInt(1));
                queryData.setTaskName(cursor.getString(2));
                queryData.setTaskContent(cursor.getString(3));
                queryData.setPriority(cursor.getString(4));
                TaskList.add(queryData);
            }while(cursor.moveToFirst());
        }
        return TaskList;
    }

    public boolean deleteKanPan(KanPanBoard kanPanBoard) {
        boolean result = false;
        Integer kanPanBoardID = kanPanBoard.getKpID();
        String query = "SELECT * FROM "+KANPAN+" WHERE "+ COLUMN_KPID + "=\""+ kanPanBoardID +"\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            db.delete(KANPAN, COLUMN_KPID + "= ?", new String[]{String.valueOf(kanPanBoardID)});
            cursor.close();
            result = true;
            Log.v(TAG, query);
        }


        String query2 = "SELECT * FROM "+TASKS+" WHERE "+ COLUMN_KPID + "=\""+ kanPanBoardID +"\"";
        Cursor cursor2 = db.rawQuery(query2, null);
        if(cursor2.moveToFirst()){
            db.delete(TASKS, COLUMN_KPID + "= ?", new String[]{String.valueOf(kanPanBoardID)});
            cursor.close();
            result = true;
            Log.v(TAG, query2);
        }

        db.close();
        return result;
    }

    public boolean deleteTask(Task task) {
        boolean result = false;
        Integer taskID = task.getTaskID();
        String query = "SELECT * FROM "+TASKS+" WHERE "+ COLUMN_TASKID + "=\""+ taskID +"\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            db.delete(TASKS, COLUMN_TASKID + "= ?", new String[]{String.valueOf(taskID)});
            cursor.close();
            result = true;
            Log.v(TAG, FILENAME + ": Database delete user: " + query);
        }
        db.close();
        return result;
    }

    public void updateTaskContent(Task task){
        String currentContent;
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASKCONTENT, task.getTaskContent());

        String query = "SELECT "+COLUMN_TASKCONTENT+" FROM "+TASKS+" WHERE "+ COLUMN_TASKID + " = \""+ task.getTaskID();

        String whereClause = COLUMN_TASKID + " = \""+ task.getTaskID();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            currentContent = cursor.getString(0);
            if(task.getTaskContent() != currentContent){
                db.update(COLUMN_TASKCONTENT, values, whereClause,null);
                db.close();
            }
        }
    }
}
