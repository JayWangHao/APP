package com.cattsoft.framework.cache;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * ORMLite数据库
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
	public static final String DB_NAME = "mos_db";
	public static final int DB_VERSION = 1;//当前数据库版本号

	private static List<Class<?>> cacheBeanClasses = new ArrayList<Class<?>>();

	public static List<Class<?>> getCacheBeanClasses() {
		return cacheBeanClasses;
	}

	public static void setCacheBeanClasses(List<Class<?>> cacheBeanClasses) {
		DatabaseHelper.cacheBeanClasses = cacheBeanClasses;
	}

	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

    /**
     * 数据库创建时调用
     * @param arg0
     * @param arg1
     */
	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		try {
			for (Class<?> itemClass : cacheBeanClasses) {
				TableUtils.createTable(connectionSource, itemClass);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

    /**
     * 数据库升级时调用该方法
     * @param arg0
     * @param arg1
     * @param arg2
     * @param arg3
     */
	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2, int arg3) {
		try {
			for (Class<?> itemClass : cacheBeanClasses) {
				TableUtils.dropTable(connectionSource, itemClass, true);
			}
			// after we drop the old databases, we create the new ones
			onCreate(arg0, connectionSource);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}

	}

	/**
	 * 删除全部本地数据库
	 */
	public void clearDataCache() {
		for (Class<?> itemClass : cacheBeanClasses) {
			try {
				TableUtils.clearTable(connectionSource, itemClass);
				// close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
