package com.monch.database;

import android.database.sqlite.SQLiteDatabase;

import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.DataBase;
import com.litesuits.orm.db.DataBaseConfig;
import com.litesuits.orm.db.TableManager;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.assit.SQLStatement;
import com.litesuits.orm.db.assit.SQLiteHelper;
import com.litesuits.orm.db.assit.WhereBuilder;
import com.litesuits.orm.db.model.ColumnsValue;
import com.litesuits.orm.db.model.ConflictAlgorithm;
import com.litesuits.orm.db.model.RelationKey;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by monch on 2017/5/5.
 */

public class DB implements DataBase {

    private LiteOrm mLiteOrm;

    private DB(DataBaseConfig config) {
        mLiteOrm = LiteOrm.newSingleInstance(config);
    }

    private static volatile DB instance;

    public static void initialize(DataBaseConfig config) {
        instance = new DB(config);
    }

    public static DB get() {
        if (instance == null) {
            throw new IllegalArgumentException("DB instance is null.");
        }
        return instance;
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase() {
        return mLiteOrm.openOrCreateDatabase();
    }

    @Override
    public long save(Object entity) {
        return mLiteOrm.save(entity);
    }

    @Override
    public <T> int save(Collection<T> collection) {
        return mLiteOrm.save(collection);
    }

    @Override
    public long insert(Object entity) {
        return mLiteOrm.insert(entity);
    }

    @Override
    public long insert(Object entity, ConflictAlgorithm conflictAlgorithm) {
        return mLiteOrm.insert(entity, conflictAlgorithm);
    }

    @Override
    public <T> int insert(Collection<T> collection) {
        return mLiteOrm.insert(collection);
    }

    @Override
    public <T> int insert(Collection<T> collection, ConflictAlgorithm conflictAlgorithm) {
        return mLiteOrm.insert(collection, conflictAlgorithm);
    }

    @Override
    public int update(Object entity) {
        return mLiteOrm.update(entity);
    }

    @Override
    public int update(Object entity, ConflictAlgorithm conflictAlgorithm) {
        return mLiteOrm.update(entity, conflictAlgorithm);
    }

    @Override
    public int update(Object entity, ColumnsValue cvs, ConflictAlgorithm conflictAlgorithm) {
        return mLiteOrm.update(entity, cvs, conflictAlgorithm);
    }

    @Override
    public <T> int update(Collection<T> collection) {
        return mLiteOrm.update(collection);
    }

    @Override
    public <T> int update(Collection<T> collection, ConflictAlgorithm conflictAlgorithm) {
        return mLiteOrm.update(collection, conflictAlgorithm);
    }

    @Override
    public <T> int update(Collection<T> collection, ColumnsValue cvs, ConflictAlgorithm conflictAlgorithm) {
        return mLiteOrm.update(collection, cvs, conflictAlgorithm);
    }

    @Override
    public int update(WhereBuilder builder, ColumnsValue cvs, ConflictAlgorithm conflictAlgorithm) {
        return mLiteOrm.update(builder, cvs, conflictAlgorithm);
    }

    @Override
    public int delete(Object entity) {
        return mLiteOrm.delete(entity);
    }

    @Override
    public <T> int delete(Class<T> claxx) {
        return mLiteOrm.delete(claxx);
    }

    @Override
    public <T> int deleteAll(Class<T> claxx) {
        return mLiteOrm.deleteAll(claxx);
    }

    @Override
    public <T> int delete(Class<T> claxx, long start, long end, String orderAscColu) {
        return mLiteOrm.delete(claxx, start, end, orderAscColu);
    }

    @Override
    public <T> int delete(Collection<T> collection) {
        return mLiteOrm.delete(collection);
    }

    @Override
    public <T> int delete(Class<T> claxx, WhereBuilder where) {
        return mLiteOrm.delete(claxx, where);
    }

    @Override
    public int delete(WhereBuilder where) {
        return mLiteOrm.delete(where);
    }

    @Override
    public <T> ArrayList<T> query(Class<T> claxx) {
        return mLiteOrm.query(claxx);
    }

    @Override
    public <T> ArrayList<T> query(QueryBuilder<T> qb) {
        return mLiteOrm.query(qb);
    }

    @Override
    public <T> T queryById(long id, Class<T> clazz) {
        return mLiteOrm.queryById(id, clazz);
    }

    @Override
    public <T> T queryById(String id, Class<T> clazz) {
        return mLiteOrm.queryById(id, clazz);
    }

    @Override
    public <T> long queryCount(Class<T> claxx) {
        return mLiteOrm.queryCount(claxx);
    }

    @Override
    public long queryCount(QueryBuilder qb) {
        return mLiteOrm.queryCount(qb);
    }

    @Override
    public SQLStatement createSQLStatement(String sql, Object[] bindArgs) {
        return mLiteOrm.createSQLStatement(sql, bindArgs);
    }

    @Override
    public boolean execute(SQLiteDatabase db, SQLStatement statement) {
        return mLiteOrm.execute(db, statement);
    }

    @Override
    public boolean dropTable(Object entity) {
        return mLiteOrm.dropTable(entity);
    }

    @Override
    public boolean dropTable(Class<?> claxx) {
        return mLiteOrm.dropTable(claxx);
    }

    @Override
    public boolean dropTable(String tableName) {
        return mLiteOrm.dropTable(tableName);
    }

    @Override
    public ArrayList<RelationKey> queryRelation(Class class1, Class class2, List<String> key1List) {
        return mLiteOrm.queryRelation(class1, class2, key1List);
    }

    @Override
    public <E, T> boolean mapping(Collection<E> col1, Collection<T> col2) {
        return mLiteOrm.mapping(col1, col2);
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        return mLiteOrm.getReadableDatabase();
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        return mLiteOrm.getWritableDatabase();
    }

    @Override
    public TableManager getTableManager() {
        return mLiteOrm.getTableManager();
    }

    @Override
    public SQLiteHelper getSQLiteHelper() {
        return mLiteOrm.getSQLiteHelper();
    }

    @Override
    public DataBaseConfig getDataBaseConfig() {
        return mLiteOrm.getDataBaseConfig();
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String path, SQLiteDatabase.CursorFactory factory) {
        return mLiteOrm.openOrCreateDatabase(path, factory);
    }

    @Override
    public boolean deleteDatabase() {
        return mLiteOrm.deleteDatabase();
    }

    @Override
    public boolean deleteDatabase(File file) {
        return mLiteOrm.deleteDatabase(file);
    }

    @Override
    public void close() {
        mLiteOrm.close();
    }
}
