package org.javaboy.vhr.mapper;

import org.apache.ibatis.annotations.Param;
import org.javaboy.vhr.model.Employeeec;

import java.util.Date;
import java.util.List;

public interface EmployeeecMapper {

    //查询奖罚员工信息
    List<Employeeec> getEmployeeECByPage(@Param("page") Integer page,@Param("size") Integer size,@Param("employeeec") Employeeec employeeec,Date[] beginDateScope);

    //获取总条数
    Long getTotal(@Param("employeeec") Employeeec employeeec);

    int deleteByPrimaryKey(Integer id);

    int insert(Employeeec record);

    int insertSelective(Employeeec record);

    Employeeec selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Employeeec record);

    int updateByPrimaryKey(Employeeec record);
}