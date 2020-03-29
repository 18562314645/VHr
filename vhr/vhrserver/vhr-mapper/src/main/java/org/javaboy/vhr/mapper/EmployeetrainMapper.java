package org.javaboy.vhr.mapper;

import org.apache.ibatis.annotations.Param;
import org.javaboy.vhr.model.Employeetrain;

import java.util.List;

public interface EmployeetrainMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Employeetrain record);

    int insertSelective(Employeetrain record);

    Employeetrain selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Employeetrain record);

    int updateByPrimaryKey(Employeetrain record);

    /**
     * 查询培训员工列表
     * @param page
     * @param size
     * @param employeetrain
     * @return
     */
     List<Employeetrain> getEmployeeTrainByPage(@Param("page") Integer page,@Param("size") Integer size,@Param("employeetrain") Employeetrain employeetrain);

    /**
     * 查询总记录数
     * @param employeetrain
     * @return
     */
     Long  getTotal(@Param("employeetrain") Employeetrain employeetrain);
}