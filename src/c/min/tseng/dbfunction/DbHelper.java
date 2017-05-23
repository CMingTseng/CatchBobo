package c.min.tseng.dbfunction;

import android.database.Cursor;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;
import android.content.ContentValues;




public class DbHelper extends SQLiteOpenHelper {

//資料庫版本關係到App更新時，資料庫是否要調用onUpgrade()
  private static final int VERSION = 1;//資料庫版本
  public String  sCreateTableCommand;

//建構子

public DbHelper(Context context, String name, CursorFactory factory, int version)
	{
		super(context, name, factory,version);
	
		Log.d("DbHelper", "DbHelper()");
	}
public DbHelper(Context context,String name) { 
   this(context, name, null, VERSION); 
   } 
   
public DbHelper(Context context, String name, int version) {  
    this(context, name, null, version);  
      }  

 //輔助類建立時運行該方法
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		Log.d("DbHelper", "onCreate()");

		
		if (sCreateTableCommand.isEmpty())
			return;		
		db.execSQL(sCreateTableCommand);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.d("DbHelper", "onUpgrade()");

	}
  @Override   
   public void onOpen(SQLiteDatabase db) {     
           super.onOpen(db);       
           // TODO 每次成功打開數據庫後首先被執行     
       } 
 
   @Override
        public synchronized void close() {
            super.close();
        }
 
}

