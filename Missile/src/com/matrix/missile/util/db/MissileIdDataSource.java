package com.matrix.missile.util.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class MissileIdDataSource {

	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.COLUMN_ID };

	public MissileIdDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public void insertMissileId(int id) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_ID, id);
		database.insert(MySQLiteHelper.TABLE_MISSILEIDS, null, values);
	}

	public List<Integer> getAllMissileIds() {
		List<Integer> missileIdsList = new ArrayList<Integer>();
		Cursor cursor = database.query(MySQLiteHelper.TABLE_MISSILEIDS,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			missileIdsList.add(cursor.getInt(cursor
					.getColumnIndex(MySQLiteHelper.COLUMN_ID)));
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return missileIdsList;
	}
}