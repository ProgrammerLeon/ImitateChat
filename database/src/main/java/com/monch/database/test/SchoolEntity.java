package com.monch.database.test;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

import java.io.Serializable;
import java.util.List;

/**
 * Created by monch on 2017/5/5.
 */
@Table("School")    // 这是表名
public class SchoolEntity implements Serializable {

    // 每个实体类一定要实现 Serializable 接口，并且添加此变量
    // 了解序列化的同学都知道，Serializable是一种稳定的序列化方式
    // 这种序列化的优点是稳定，缺点是序列化时会生成较多的变量占用内存
    // 这个属性是Serializable的版本号，一旦改变，将无法反序列化，
    // 所以这个值，确定后就不要修改
    private static final long serialVersionUID = -1;

    @PrimaryKey(AssignType.AUTO_INCREMENT)  // 主键，这是表示自增ID
    public long id;
    // 学校名称
    public String name;
    // 学校地址，@Column是设置列名的注解
    @Column("address")
    public String address;
    // LiteOrm数据库可以直接将引用类型的数据，序列化为字节，存入表中
    // 前提是这个引用类型一定要实现Serializable接口
    public List<ClassEntity> classes;

}
