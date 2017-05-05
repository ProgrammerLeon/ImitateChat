package com.monch.database.test;

import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

import java.io.Serializable;

/**
 * Created by monch on 2017/5/5.
 */
// @Table是设置表名的注解，如果不设置，表名默认为包名+类名
// 比如此类将以com_monch_database_test_Class为表名
@Table("Class")
public class ClassEntity implements Serializable {

    private static final long serialVersionUID = -1;

    // 主键，这是表示自定义ID，操作数据库时，此属性一定要赋值
    @PrimaryKey(AssignType.BY_MYSELF)
    public long id;

    // 班级名称
    public String name;
    // 班级人数
    public int count;

}
