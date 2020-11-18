package sg.edu.np.mad.evap2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    private static final String databaseName = "PrOrganize.db";
    private static final int databaseVersion = 1;

    //UserModel
    private static final String accountsTable = "Accounts";
    private static final String accountsUsernameColumn = "Username";
    private static final String accountsPasswordColumn = "Password";
    private static final String accountsEmailColumn = "Email";

    //ProjectModel
    private static final String projectsTable = "Projects";
    private static final String projectsIdColumn = "ProjectID";
    private static final String projectsTitleColumn = "ProjectTitle";
    private static final String projectsIconColumn = "Image";

    //CategoryModel
    private static final String categoryTable = "Category";
    private static final String categoryIdColumn = "CategoryID";
    private static final String categoryTitleColumn = "CategoryTitle";

    //TaskModel
    private static final String taskTable = "Task";
    private static final String taskIdColumn = "TaskID";
    private static final String taskTitleColumn = "TaskTitle";
    private static final String taskDescriptionColumn = "TaskDescription";
    private static final String taskPriorityColumn = "TaskPriority";

    //NoteModel
    private static final String noteTable = "Note";
    private static final String noteIdColumn = "NoteID";
    private static final String noteDescriptionColumn = "NoteDescription";

    //Constructor
    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, databaseName, factory, databaseVersion);
    }

    //1. OnOpen
    @Override
    public void onOpen(SQLiteDatabase db){
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON"); //Enables foreign key constraints
    }

    //1. OnCreate
    @Override
    public void onCreate(SQLiteDatabase db) {
        //On Delete Cascade refers to the fact that if a row of data is deleted, all data rows containing
        //the deleted data row's primary key as a foreign key will also be deleted.
        String CREATE_Accounts_TABLE = "CREATE TABLE " + accountsTable +
                "(" + accountsUsernameColumn + " TEXT NOT NULL," +
                accountsPasswordColumn + " TEXT NOT NULL," +
                accountsEmailColumn + " TEXT NOT NULL," +
                "PRIMARY KEY (" + accountsUsernameColumn + ")" +
                ");";
        db.execSQL(CREATE_Accounts_TABLE);

        String CREATE_Projects_TABLE = "CREATE TABLE " + projectsTable +
                "(" + projectsIdColumn + " INTEGER NOT NULL," +
                accountsUsernameColumn + " TEXT NOT NULL," +
                projectsTitleColumn + " TEXT NOT NULL," +
                projectsIconColumn + " BLOB," +
                "PRIMARY KEY (" + projectsIdColumn + ")," +
                "FOREIGN KEY (" + accountsUsernameColumn + ") REFERENCES " + accountsTable + "(" +
                accountsUsernameColumn + ") ON DELETE CASCADE ON UPDATE NO ACTION" +
                ");";
        db.execSQL(CREATE_Projects_TABLE);

        String CREATE_Category_TABLE = "CREATE TABLE " + categoryTable +
                "(" + categoryIdColumn + " INTEGER NOT NULL," +
                projectsIdColumn + " INTEGER NOT NULL," +
                categoryTitleColumn + " TEXT NOT NULL," +
                "PRIMARY KEY (" + categoryIdColumn + ")," +
                "FOREIGN KEY (" + projectsIdColumn + ") REFERENCES " + projectsTable + "(" +
                projectsIdColumn + ") ON DELETE CASCADE ON UPDATE NO ACTION" +
                ");";
        db.execSQL(CREATE_Category_TABLE);

        String CREATE_Task_TABLE = "CREATE TABLE " + taskTable +
                "(" + taskIdColumn + " INTEGER NOT NULL," +
                categoryIdColumn + " INTEGER NOT NULL," +
                taskTitleColumn + " TEXT NOT NULL," +
                taskDescriptionColumn + " TEXT NOT NULL," +
                taskPriorityColumn + " TEXT NOT NULL," +
                "PRIMARY KEY (" + taskIdColumn + ")," +
                "FOREIGN KEY (" + categoryIdColumn + ") REFERENCES " + categoryTable + "(" +
                categoryIdColumn + ") ON DELETE CASCADE ON UPDATE NO ACTION" +
                ");";
        db.execSQL(CREATE_Task_TABLE);

        String CREATE_Note_TABLE = "CREATE TABLE " + noteTable +
                "(" + noteIdColumn + " INTEGER NOT NULL," +
                taskIdColumn + " INTEGER NOT NULL," +
                noteDescriptionColumn + " TEXT NOT NULL," +
                "PRIMARY KEY (" + noteIdColumn + ")," +
                "FOREIGN KEY (" + taskIdColumn + ") REFERENCES " + taskTable + "(" +
                taskIdColumn + ") ON DELETE CASCADE ON UPDATE NO ACTION" +
                ");";
        db.execSQL(CREATE_Note_TABLE);
    }

    //2. OnUpgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + accountsTable);
        db.execSQL("DROP TABLE IF EXISTS " + projectsTable);
        db.execSQL("DROP TABLE IF EXISTS " + categoryTable);
        db.execSQL("DROP TABLE IF EXISTS " + taskTable);
        db.execSQL("DROP TABLE IF EXISTS " + noteTable);
        onCreate(db);
    }

    //------------User
    //3. Add new user
    /*public void addUser(UserModel userData) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(accountsUsernameColumn, userData.getName());
        values.put(accountsPasswordColumn, userData.getPassword());
        values.put(accountsEmailColumn, userData.getEmail());

        db.insert(accountsTable, null, values);

        db.close();
    }

    //4. Get user
    public UserModel getUser(String username) {
        String query = "SELECT * FROM " + accountsTable + " WHERE " +
                accountsUsernameColumn + " = \"" + username + "\"";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (!cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return null;
        }

        String password = cursor.getString(1);
        String email = cursor.getString(2);

        cursor.close();
        db.close();
        UserModel queryData = new UserModel(username, password, email);
        return queryData;
    }

    //5. Validate user
    public boolean validateUser(String username) {
        String query = "SELECT * FROM " + accountsTable + " WHERE " +
                accountsUsernameColumn + " = \"" + username + "\"";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (!cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return false;
        } else {
            cursor.close();
            db.close();
            return true;
        }
    }

    //6. Authenticate password
    public boolean authenticatePassword(String username, String password) {
        String query = "SELECT * FROM " + accountsTable + " WHERE " +
                accountsUsernameColumn + " = \"" + username + "\"" + " AND " +
                accountsPasswordColumn + " = \"" + password + "\"";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (!cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return false;
        } else {
            cursor.close();
            db.close();
            return true;
        }
    }

    //7. Update user details
    public void updateUser(UserModel userData, String password, String email) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(accountsPasswordColumn, password);
        values.put(accountsEmailColumn, email);
        db.update(accountsTable, values, accountsUsernameColumn + "=\"" + userData.getName() + "\"", null);
    }
    */


    //------------Project
    //8. Get user's projects
    public ArrayList<ProjectModel> getAllUserProjects(UserModel user) {
        String query = "SELECT * FROM " + projectsTable + " WHERE " +
                accountsUsernameColumn + " = \"" + user.getName() + "\"";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (!cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return null;
        }

        ArrayList<ProjectModel> queryData = new ArrayList<>();
        do {
            Integer id = cursor.getInt(0);
            String projectTitle = cursor.getString(2);
            if (cursor.getBlob(3) != null) {
                queryData.add(new ProjectModel(id, user.getName(), projectTitle, cursor.getBlob(3)));
            } else {
                queryData.add(new ProjectModel(id, user.getName(), projectTitle));
            }
        } while (cursor.moveToNext());

        cursor.close();
        db.close();
        return queryData;
    }

    //9. Add new project
    public void addProjectToUser(String username, String title, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(accountsUsernameColumn, username);
        values.put(projectsTitleColumn, title);
        if (image != null) {
            values.put(projectsIconColumn, image);
        }
        db.insert(projectsTable, null, values);
        db.close();
    }

    //10. Get project
    public ProjectModel getProject(Integer projectId) {
        String query = "SELECT * FROM " + projectsTable + " WHERE " +
                projectsIdColumn + " = " + projectId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (!cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return null;
        }

        ProjectModel queryData;
        String name = cursor.getString(1);
        String projectTitle = cursor.getString(2);
        if (cursor.getBlob(3) != null) {
            queryData = new ProjectModel(projectId, name, projectTitle, cursor.getBlob(3));
        } else {
            queryData = (new ProjectModel(projectId, name, projectTitle));
        }

        cursor.close();
        db.close();
        return queryData;
    }

    //11. Delete project
    public void deleteProject(Integer projectId) {
        String query = "SELECT * FROM " + projectsTable + " WHERE " +
                projectsIdColumn + " = \"" + projectId + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (!cursor.moveToFirst()) {
            cursor.close();
            db.close();
        } else {
            do {
                db.delete(projectsTable, projectsIdColumn + " =" + projectId,
                        null);
            } while (cursor.moveToNext());

            cursor.close();
            db.close();
        }
    }

    //12. Update project details
    public void updateProject(ProjectModel projectData, String title, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(projectsTitleColumn, title);
        if (image != null) {
            values.put(projectsIconColumn, image);
        }
        db.update(projectsTable, values, projectsIdColumn + "=" + projectData.getProjectId(), null);
    }

    //------------Categories
    //13. Get project's categories
    public ArrayList<CategoryModel> getAllProjectCategories(ProjectModel project) {
        String query = "SELECT * FROM " + categoryTable + " WHERE " +
                projectsIdColumn + " = \"" + project.getProjectId() + "\"";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (!cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return null;
        }

        ArrayList<CategoryModel> queryData = new ArrayList<>();
        do {
            Integer id = cursor.getInt(0);
            String categoryTitle = cursor.getString(2);
            queryData.add(new CategoryModel(id, project.getProjectId(), categoryTitle));
        } while (cursor.moveToNext());

        cursor.close();
        db.close();
        return queryData;
    }

    //14. Add new category
    public void addCategoryToProject(Integer projectId, String title) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(projectsIdColumn, projectId);
        values.put(categoryTitleColumn, title);
        db.insert(categoryTable, null, values);
        db.close();
    }

    //15. Get category
    public CategoryModel getCategory(Integer categoryId) {
        String query = "SELECT * FROM " + categoryTable + " WHERE " +
                categoryIdColumn + " = " + categoryId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (!cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return null;
        }

        CategoryModel queryData;
        Integer id = cursor.getInt(1);
        String categoryTitle = cursor.getString(2);
        queryData = new CategoryModel(categoryId, id, categoryTitle);

        cursor.close();
        db.close();
        return queryData;
    }

    //16. Delete category
    public void deleteCategory(Integer categoryId) {
        String query = "SELECT * FROM " + categoryTable + " WHERE " +
                categoryIdColumn + " = \"" + categoryId + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (!cursor.moveToFirst()) {
            cursor.close();
            db.close();
        } else {
            do {
                db.delete(categoryTable, categoryIdColumn + " =" + categoryId,
                        null);
            } while (cursor.moveToNext());

            cursor.close();
            db.close();
        }
    }

    //17. Update category details
    public void updateCategory(CategoryModel categoryData, String title) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(categoryTitleColumn, title);
        db.update(categoryTable, values, categoryIdColumn + "=" + categoryData.getCategoryId(), null);
    }

    //------------Tasks
    //18. Get category's tasks
    public ArrayList<TaskModel> getAllCategoryTask(CategoryModel category) {
        String query = "SELECT * FROM " + taskTable + " WHERE " +
                categoryIdColumn + " = \"" + category.getCategoryId() + "\"";

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

    //19. Get all priority tasks
    public ArrayList<TaskModel> getAllPriorityTask(UserModel user) {
        /*
            Subquery description:
            The 4th select in the query gets a list of project rows with a specific userId attached.
            The 3rd select in the query gets a list of category rows with project Ids that are in the previous list.
            The 2nd select in the query gets a list of task rows with category Ids that are in the previous list.
            The 1st select in the query gets a list of task rows with task Ids that are in the previous list
            and also have a priority of "VeryImpt".

            The use of a subquery ensures that no unnecessary data is stored or accessed through inner joins or
            storing in Models.
        */
        String query = "SELECT * FROM " + taskTable + " WHERE " + taskPriorityColumn + " = \"" + "VeryImpt" + "\" AND " +
                taskIdColumn + " IN ( SELECT " + taskIdColumn + " FROM " + taskTable + " WHERE " +
                categoryIdColumn + " IN ( SELECT " + categoryIdColumn + " FROM " + categoryTable + " WHERE " +
                projectsIdColumn + " IN ( SELECT " + projectsIdColumn + " FROM " + projectsTable + " WHERE " + accountsUsernameColumn + " = \"" + user.getName() + "\"" + ")))";

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
            Integer categoryId = cursor.getInt(1);
            String taskTitle = cursor.getString(2);
            String taskDescription = cursor.getString(3);
            String taskPriority = cursor.getString(4);
            queryData.add(new TaskModel(id, categoryId, taskTitle, taskDescription, taskPriority));
        } while (cursor.moveToNext());

        cursor.close();
        db.close();
        return queryData;
    }

    //20. Add task
    public void addTaskToCategory(Integer categoryId, String title, String description, String priority) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(categoryIdColumn, categoryId);
        values.put(taskTitleColumn, title);
        values.put(taskDescriptionColumn, description);
        values.put(taskPriorityColumn, priority);
        db.insert(taskTable, null, values);
        db.close();
    }

    //21. Get task
    public TaskModel getTask(Integer taskId) {
        String query = "SELECT * FROM " + taskTable + " WHERE " +
                taskIdColumn + " = " + taskId;

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
        String query = "SELECT * FROM " + taskTable + " WHERE " +
                taskIdColumn + " = \"" + taskId + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (!cursor.moveToFirst()) {
            cursor.close();
            db.close();
        } else {
            do {
                db.delete(taskTable, taskIdColumn + " =" + taskId,
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
        values.put(categoryIdColumn, categoryId);
        values.put(taskTitleColumn, title);
        values.put(taskDescriptionColumn, description);
        values.put(taskPriorityColumn, priority);
        db.update(taskTable, values, taskIdColumn + "=" + taskData.getTaskId(), null);
    }

    //------------Notes
    //24. Get task's notes
    public ArrayList<NoteModel> getAllTaskNotes(TaskModel task) {
        String query = "SELECT * FROM " + noteTable + " WHERE " +
                taskIdColumn + " = \"" + task.getTaskId() + "\"";

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
        values.put(taskIdColumn, taskId);
        values.put(noteDescriptionColumn, description);
        db.insert(noteTable, null, values);
        db.close();
    }

    //26. Delete note
    public void deleteNote(Integer noteId) {
        String query = "SELECT * FROM " + noteTable + " WHERE " +
                noteIdColumn + " = \"" + noteId + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (!cursor.moveToFirst()) {
            cursor.close();
            db.close();
        } else {
            do {
                db.delete(noteTable, noteIdColumn + " =" + noteId,
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
        values.put(noteDescriptionColumn, description);
        db.update(noteTable, values, noteIdColumn + "=" + noteData.getNoteId(), null);
    }
}
