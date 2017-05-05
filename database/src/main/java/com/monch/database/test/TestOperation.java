package com.monch.database.test;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.litesuits.orm.db.DataBaseConfig;
import com.litesuits.orm.db.assit.SQLiteHelper;
import com.monch.database.DB;

import java.util.ArrayList;

/**
 * Created by monch on 2017/5/5.
 */

public class TestOperation {

    public static void main(Context context) {
        // 初始化数据库
        DB.initialize(new DataBaseConfig(context, "数据库名称.db", false, 1,
                new SQLiteHelper.OnUpdateListener() {
            @Override
            public void onUpdate(SQLiteDatabase db, int oldVersion, int newVersion) {
                if (newVersion > oldVersion) {
                    // 数据库更新回调
                }
            }
        }));

        // 创建学校
        SchoolEntity school = new SchoolEntity();
        school.name = "测试小学";
        school.address = "北京市测试街道110号";
        school.classes = new ArrayList<>(2);
        // 创建1班
        ClassEntity class1 = new ClassEntity();
        class1.id = 1;
        class1.name = "一年1班";
        class1.count = 30;
        school.classes.add(class1); // 将1班添加到学校中
        // 创建2班
        ClassEntity class2 = new ClassEntity();
        class2.id = 2;
        class2.name = "一年2班";
        class2.count = 20;
        school.classes.add(class2); // 将2班添加到学校中

        DB db = DB.get();
        // 保存学校，此时班级也被直接保存至学校表中
        // 说明一下save方法和insert方法：
        // insert方法是直接插入
        // save方法是以id为准，如果实例中有id并在表中存在此数据则执行更新，否则为插入
        long schoolId = db.save(school);
        // 按照id查询学校
        SchoolEntity querySchool = db.queryById(schoolId, SchoolEntity.class);
        // 删除学校
        int count = db.delete(SchoolEntity.class);
    }

}
