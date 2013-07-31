package com.keita.pinganautoinsurance.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

	@Override
	public void onOpen(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		super.onOpen(db);
	}

	// 数据库名称
	private static final String DATABASE_NAME = "PingAnDB";
	// 数据库版本
	private static final int DATABSE_VERSION = 1;
	// 表名
	// 保单表
	private static final String INSURANCE_POLICY_TABLE = "insurance_policy_table";
	// 带评论的照片表
	private static final String TEXT_IMAGE_TABLE = "text_image_table";
	// 保单对应图片表
	private static final String INSURANCE_PHOTO_TABLE = "insurance_photo_table";
	// 保单对应的文本表
	private static final String INSURANCE_TEXT_TABLE = "insruace_text_table";

	// 创建表的sql语句
	private static final String TEXT_IMAGE_TABLE_SQL = "CREATE TABLE "
			+ TEXT_IMAGE_TABLE
			+ "(id integer primary key,img_path text,img_text text,img_date text);";

	private static final String INSURANCE_PHOTO_TABLE_SQL = "CREATE TABLE "
			+ INSURANCE_PHOTO_TABLE
			+ "(id integer primary key autoincrement,img1_id integer,img2_id integer ,"
			+ "img3_id integer ,img4_id integer ,img5_id integer ,img6_id integer,"
			+ "foreign key(img1_id) references text_image_table(id),foreign key(img2_id) references text_image_table(id),"
			+ "foreign key(img3_id) references text_image_table(id) ,foreign key(img4_id) references text_image_table(id)"
			+ "foreign key(img5_id) references text_image_table(id),foreign key(img6_id) references text_image_table(id));";

	private static final String INSURANCE_TEXT_SQL = "CREATE TABLE "
			+ INSURANCE_TEXT_TABLE
			+ "(text_id integer primary key autoincrement,case_no text,case_owner text,case_driver text,realtion text,"
			+ "case_owner_phone text,case_driver_phone text,case_drvier_lience text,case_car_no text,case_car_type,vin text,"
			+ "case_third_car_no text,case_third_car_type,hurt_num integer,dead_num integer,case_reason text,accident_reason text,"
			+ "accident_detail text);";
	private static final String INSURANCE_POLICY_SQL = "CREATE TABLE "
			+ INSURANCE_POLICY_TABLE
			+ "(policy_id integer primary key autoincrement,location text,record_path text,date text,photos_id integer,text_id integer,"
			+ "foreign key(photos_id) references insurance_photo_table(id),foreign key(text_id) references insurance_text_table(text_id));";

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABSE_VERSION);
		// TODO Auto-generated constructor stub
		Log.v("db", "const");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		Log.v("db", "created");
		db.execSQL(TEXT_IMAGE_TABLE_SQL);
		db.execSQL(INSURANCE_PHOTO_TABLE_SQL);
		db.execSQL(INSURANCE_TEXT_SQL);
		db.execSQL(INSURANCE_POLICY_SQL);
	}

	// 插入数据 根据表
	public void insertData(SQLiteDatabase db, ContentValues cv, String tableName) {
		// 单个照片表
		if (tableName.equals(TEXT_IMAGE_TABLE)) {
			db.insert(tableName, null, cv);
		}
		// 保单照片集表
		if (tableName.equals(INSURANCE_PHOTO_TABLE)) {
			db.insert(tableName, "id", cv);
		}
		if (tableName.equals(INSURANCE_TEXT_TABLE)) {
			db.insert(tableName, "text_id", cv);
		}
		if (tableName.equals(INSURANCE_POLICY_TABLE)) {
			db.insert(tableName, "policy_id", cv);
		}
	}

	public void deleteData(SQLiteDatabase db, String whereClause,
			String[] whereArgs, String tableName) {
		if (tableName.equals(TEXT_IMAGE_TABLE)) {
			db.delete(tableName, whereClause, whereArgs);
		}
	}

	public void update() {

	};

	// 按列查询
	public Cursor queryByCol(SQLiteDatabase db, String tableName,
			String[] columns) {
		return db.query(tableName, columns, null, null, null, null, null);
	}

	// 查询所有
	public Cursor queryAll(SQLiteDatabase db, String tableName) {
		return db.query(tableName, null, null, null, null, null, null);
	}

	// 常规查询
	public Cursor query(SQLiteDatabase db, String tableName, String[] columns,
			String selection, String[] selectionArgs, String groupBy,
			String having, String orderBy) {
		return db.query(tableName, columns, selection, selectionArgs, groupBy,
				having, orderBy);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.v("DB", "update");
	}

}
