package sg.edu.np.mad.evap2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    private static final String FILENAME = "MyDBHandler.java";
    private static final String TAG = "evaDB";
    public static String DATABASE_NAME = "PrOrganize.db";
    public static int DATABASE_VERSION = 1;
    //KANPAN
    private static final String CAT = "cat";
    private static final String CAT_TITLE = "Title";
    private static final String COLUMN_USEREMAIL = "UserMail";
    //TASKS
    private static final String TASKS = "Tasks";
    private static final String COLUMN_TASKID = "TaskID";
    private static final String COLUMN_TASKDESC = "TaskDesc";
    private static final String COLUMN_TASKTITLE = "TaskTitle";
    private static final String COLUMN_TASKCONTENT = "TaskContent";
    private static final String COLUMN_PRIORITY = "Priority";
    private static final String COLUMN_CATID = "CatId";

    //NOTES
    private static final String NOTES = "Notes";
    private static final String NOTE_ID = "NoteID";
    private static final String NOTE_DESC = "NoteDesc";


    //Constructor
    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    //1. OnOpen
    @Override
    public void onOpen(SQLiteDatabase db){
        super.onOpen(db);
    }

    //1. OnCreate
    @Override
    public void onCreate(SQLiteDatabase db) {
        //On Delete Cascade refers to the fact that if a row of data is deleted, all data rows containing
        //the deleted data row's primary key as a foreign key will also be deleted.

        String CREATE_TASK_TABLE = "CREATE TABLE " + TASKS +
                "(" + COLUMN_TASKID + " INTEGER," +
                COLUMN_CATID+ " INTEGER," +
                COLUMN_TASKTITLE + " TEXT," +
                COLUMN_TASKDESC + " TEXT," +
                COLUMN_PRIORITY + " TEXT" + ")";

        db.execSQL(CREATE_TASK_TABLE);

        String CREATE_KANPANTABLE = "CREATE TABLE " + CAT +
                "(" + COLUMN_CATID + " INTEGER," +
                CAT_TITLE + " TEXT," +
                COLUMN_USEREMAIL + " TEXT" + ")";

        db.execSQL(CREATE_KANPANTABLE);

        String CREATE_Note_TABLE = "CREATE TABLE " + NOTES +
                "(" + NOTE_ID + " INTEGER," +
                NOTE_DESC + " TEXT," +
                COLUMN_TASKID + " INTEGER"+ ")";
        db.execSQL(CREATE_Note_TABLE);
    }

    //2. OnUpgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + CAT);
        db.execSQL("DROP TABLE IF EXISTS " + NOTES);
        onCreate(db);
    }

    //------------Project
    //8. Get user's projects
    public ArrayList<CategoryModel> getUserCategories(String email) {
        String query = "SELECT * FROM " + CAT + " WHERE " +
                COLUMN_USEREMAIL + " = \"" + email + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        CategoryModel queryData = new CategoryModel();

        ArrayList<CategoryModel> KanPanList = new ArrayList<CategoryModel>();

        if(cursor.moveToFirst()){
            do{
                queryData.setCategoryId(cursor.getInt(0));
                queryData.setTitle(cursor.getString(1));
                KanPanList.add(queryData);
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return KanPanList;
    }

    public void addCatToUser(UserModel userModel, CategoryModel categoryModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USEREMAIL, userModel.getEmail());
        values.put(CAT_TITLE, categoryModel.getTitle());
        values.put(COLUMN_CATID, categoryModel.getCategoryId());
        db.insert(CAT, null, values);
        Log.d(TAG, "addCatToUser: "+ userModel.getEmail() + categoryModel.getTitle()+ categoryModel.getCategoryId());
        db.close();
    }


    public void deleteCat(Integer catID) {
        String query = "SELECT * FROM " + CAT + " WHERE " +
                COLUMN_CATID + " = \"" + catID + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (!cursor.moveToFirst()) {
            cursor.close();
            db.close();
        } else {
            do {
                db.delete(CAT, COLUMN_CATID + " =" + catID,
                        null);
            } while (cursor.moveToNext());

            cursor.close();
            db.close();
        }
    }

    public void updateProject(CategoryModel newData) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CAT_TITLE, newData.getTitle());
        db.update(CAT, values, COLUMN_CATID + "=" + newData.getCategoryId(), null);
    }


    //------------Tasks
    //18. Get category's tasks
    public ArrayList<TaskModel> getAllCategoryTask(CategoryModel category) {
        String query = "SELECT * FROM " + TASKS + " WHERE " +
                COLUMN_CATID + " = \"" + category.getCategoryId() + "\"";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (!cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return null;
        }

        ArrayList<TaskModel> queryData = new ArrayList<>();
        do {
            Integer id = cursor.getInt(0);
            String taskTitle = cursor.getString(2);
            String taskDescription = cursor.getString(3);
            String taskPriority = cursor.getString(4);
            queryData.add(new TaskModel(id, category.getCategoryId(), taskTitle, taskDescription, taskPriority));
        } while (cursor.moveToNext());

        cursor.close();
        db.close();
        return queryData;
    }

    //20. Add task
    public void addTaskToCategory(Integer categoryId, String title, String description, String priority) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CATID, categoryId);
        values.put(COLUMN_TASKTITLE, title);
        values.put(COLUMN_TASKDESC, description);
        values.put(COLUMN_PRIORITY, priority);
        db.insert(TASKS, null, values);
        db.close();
    }

    //21. Get task
    public TaskModel getTask(Integer taskId) {
        String query = "SELECT * FROM " + TASKS + " WHERE " +
                TASKS + " = " + taskId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (!cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return null;
        }

        TaskModel queryData;
        Integer id = cursor.getInt(1);
        String taskTitle = cursor.getString(2);
        String taskDescription = cursor.getString(3);
        String taskPriority = cursor.getString(4);
        queryData = new TaskModel(taskId, id, taskTitle, taskDescription, taskPriority);

        cursor.close();
        db.close();
        return queryData;
    }

    //22. Delete task
    public void deleteTask(Integer taskId) {
        String query = "SELECT * FROM " + TASKS + " WHERE " +
                COLUMN_TASKID + " = \"" + taskId + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (!cursor.moveToFirst()) {
            cursor.close();
            db.close();
        } else {
            do {
                db.delete(TASKS, COLUMN_TASKID + " =" + taskId,
                        null);
            } while (cursor.moveToNext());

            cursor.close();
            db.close();
        }
    }

    //23. Update task details
    public void updateTask(TaskModel taskData, Integer categoryId, String title, String description, String priority) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CATID, categoryId);
        values.put(COLUMN_TASKTITLE, title);
        values.put(COLUMN_TASKDESC, description);
        values.put(COLUMN_PRIORITY, priority);
        db.update(TASKS, values, COLUMN_TASKID + "=" + taskData.getTaskId(), null);
    }

    //------------Notes
    //24. Get task's notes
    public ArrayList<NoteModel> getAllTaskNotes(TaskModel task) {
        String query = "SELECT * FROM " + NOTES + " WHERE " +
                COLUMN_TASKID + " = \"" + task.getTaskId() + "\"";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (!cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return null;
        }

        ArrayList<NoteModel> queryData = new ArrayList<>();
        do {
            Integer id = cursor.getInt(0);
            String noteDescription = cursor.getString(2);
            queryData.add(new NoteModel(id, task.getTaskId(), noteDescription));
        } while (cursor.moveToNext());

        cursor.close();
        db.close();
        return queryData;
    }

    //25. Add note
    public void addNoteToTask(Integer taskId, String description) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TASKID, taskId);
        values.put(NOTE_DESC, description);
        db.insert(NOTES, null, values);
        db.close();
    }

    //26. Delete note
    public void deleteNote(Integer noteId) {
        String query = "SELECT * FROM " + NOTES + " WHERE " +
                NOTE_ID + " = \"" + noteId + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (!cursor.moveToFirst()) {
            cursor.close();
            db.close();
        } else {
            do {
                db.delete(NOTES, NOTE_ID + " =" + noteId,
                        null);
            } while (cursor.moveToNext());

            cursor.close();
            db.close();
        }
    }

    //27. Update note details
    public void updateNote(NoteModel noteData, String description) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NOTE_DESC, description);
        db.update(NOTES, values, NOTE_ID + "=" + noteData.getNoteId(), null);
    }
}
