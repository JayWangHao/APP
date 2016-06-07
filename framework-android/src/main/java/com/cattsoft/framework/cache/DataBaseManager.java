package com.cattsoft.framework.cache;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;

/**
 * ORMLite数据库
 */
public class DataBaseManager {

	private static DataBaseManager mCacheManager;
	private ExecutorService mThreadPool;
	private static final int MAX_POOL_SIZE = 2;
	private DatabaseHelper mDatabaseHelper;

	private DataBaseManager(Context context) {
		mThreadPool = Executors.newFixedThreadPool(MAX_POOL_SIZE);
		mDatabaseHelper = new DatabaseHelper(context);
	}

	public static synchronized DataBaseManager getInstance(Context context) {
		if (mCacheManager == null) {
			mCacheManager = new DataBaseManager(context);
		}
		return mCacheManager;
	}

	public void restartThreadPool() {
		if (mThreadPool != null) {
			shutdownThreadPool();
		}
		mThreadPool = Executors.newFixedThreadPool(MAX_POOL_SIZE);
	}

	public void shutdownThreadPool() {
		if (mThreadPool != null) {
			mThreadPool.shutdown();
			mThreadPool = null;
		}
	}

    /**
     * 批量插入数据
     * @param classArg
     * @param list
     * @param clearOld 是否清空表中所有数据
     * @param <T>
     */
	public <T> int insert(Class<T> classArg, List<T> list, boolean clearOld) {
		if (list == null || list.isEmpty())
			return -1;
		try {

			TableUtils.createTableIfNotExists(mDatabaseHelper.getConnectionSource(), classArg);
			if (clearOld) {
				TableUtils.clearTable(mDatabaseHelper.getConnectionSource(), classArg);
			}
			Dao<T, Integer> objDAO = mDatabaseHelper.getDao(classArg);
			for (T item : list) {

                int effectedRow =objDAO.create(item);

                if(effectedRow != 1){

                    return -1;
                }
			}

            return 1;

		} catch (SQLException e) {
			Log.e("DEMO", "insertObjs",e);
		} finally {
			// mDatabaseHelper.close();
		}

        return -1;
	}

    /**
     * 插入一条数据
     * @param item
     * @param clazz
     * @param <T>
     * @return
     */
	public <T> int insert(T item, Class<T> clazz) {
		if (item == null)
			return -1;
		try {

			TableUtils.createTableIfNotExists(mDatabaseHelper.getConnectionSource(), item.getClass());
			Dao<T, Integer> objDAO = mDatabaseHelper.getDao(clazz);
			int effectedRow = objDAO.create(item);
			return effectedRow;
		} catch (SQLException e) {
			Log.e("DEMO", "insertObjs",e);
		}
		return -1;
	}

    /**
     * 更新某条数据
     * @param classArg
     * @param obj
     * @param <T>
     */
	public <T> void saveOrUpdate(Class<T> classArg, T obj) {
		if (obj == null)
			return;
		try {
			TableUtils.createTableIfNotExists(mDatabaseHelper.getConnectionSource(), classArg);
			Dao<T, Integer> objDAO = mDatabaseHelper.getDao(classArg);
			objDAO.createOrUpdate(obj);
		} catch (SQLException e) {
			 Log.e("DEMO", "insertObjs",e);
		} finally {
			// mDatabaseHelper.close();
		}
	}

    /**
     * 批量删除
     * @param classArg
     * @param list
     * @param clearOld
     * @param <T>
     */
	public <T> void delete(Class<T> classArg, List<T> list, boolean clearOld) {
		if (list == null || list.isEmpty())
			return;
		try {

			TableUtils.createTableIfNotExists(mDatabaseHelper.getConnectionSource(), classArg);
			if (clearOld) {
				TableUtils.clearTable(mDatabaseHelper.getConnectionSource(), classArg);
			}
			Dao<T, Integer> objDAO = mDatabaseHelper.getDao(classArg);
			for (T item : list) {
				objDAO.delete(item);
			}

		} catch (SQLException e) {
			Log.e("DEMO", "insertObjs",e);
		} finally {
			// mDatabaseHelper.close();
		}
	}

    /**
     * 删除某条数据
     * @param item
     * @param clazz
     * @param <T>
     * @return
     */
	public <T> int delete(T item, Class<T> clazz) {
		if (item == null)
			return -1;
		try {

			TableUtils.createTableIfNotExists(mDatabaseHelper.getConnectionSource(), item.getClass());
			Dao<T, Integer> objDAO = mDatabaseHelper.getDao(clazz);
			int effectedRow = objDAO.delete(item);
			return effectedRow;
		} catch (SQLException e) {
			Log.e("DEMO", "insertObjs",e);
		}
		return -1;
	}

    /**
     * 查询所有数据
     * @param arg0
     * @param <T>
     * @return
     */
	public <T> List<T> selectAll(Class<T> arg0) {
		try {
			Dao<T, Integer> objDAO = mDatabaseHelper.getDao(arg0);
			List<T> objs = objDAO.queryForAll();
			return objs;
		} catch (SQLException e) {
		} finally {
			// mDatabaseHelper.close();
		}
		return null;
	}

    /**
     * 根据id查询数据
     * @param arg0
     * @param id
     * @param <T>
     * @return
     */
	public <T> T selectByID(Class<T> arg0, int id) {
		try {
			Dao<T, Integer> objDAO = mDatabaseHelper.getDao(arg0);
			return objDAO.queryForId(id);
		} catch (SQLException e) {
		} finally {
			// mDatabaseHelper.close();
		}
		return null;
	}
	
	/**
	 * 根据某个字段查询
	 * @return
	 */
	public <T> List<T> selectByField(Class<T> arg0, String filedName,String fieldValue){
		try {
			Dao<T, Integer> objDAO = mDatabaseHelper.getDao(arg0);
			return objDAO.queryBuilder().where().eq(filedName, fieldValue).query();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
		}
		return null;
	}

    /**
     * 通过某个字段排序
     * @param arg0
     * @param filedName
     * @param asc
     * @param <T>
     * @return
     */
    public <T> List<T> orderByField(Class<T> arg0, String filedName,boolean asc){

        try {
            Dao<T, Integer> objDAO = mDatabaseHelper.getDao(arg0);
            return objDAO.queryBuilder().orderBy(filedName,asc).query();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {

        }
        return null;

    }

	/**
	 * 删除全部本地数据库
	 */
	public void clearDataCache() {
		mDatabaseHelper.clearDataCache();
	}


	/**
	 * 删除本地数据库中Class对应的表
	 */
	public void clearTableDataCache(Class<?> itemClass) {
		try {
			TableUtils.clearTable(mDatabaseHelper.getConnectionSource(), itemClass);
			// close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
