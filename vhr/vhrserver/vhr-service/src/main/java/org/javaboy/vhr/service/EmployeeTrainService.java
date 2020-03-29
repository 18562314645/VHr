package org.javaboy.vhr.service;

import org.javaboy.vhr.mapper.EmployeetrainMapper;
import org.javaboy.vhr.model.Employeetrain;
import org.javaboy.vhr.model.RespBean;
import org.javaboy.vhr.model.RespPageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Service
public class EmployeeTrainService {

    @Autowired
    private EmployeetrainMapper employeetrainMapper;

    /**
     * 查询培训员工列表
     * @param page
     * @param size
     * @param employeetrain
     * @return
     */
    public RespPageBean getEmployeeTrainByPage(Integer page, Integer size, Employeetrain employeetrain){
        if(page !=null && size !=null){
            page = (page - 1) * size;
        }
        List<Employeetrain> data = employeetrainMapper.getEmployeeTrainByPage(page, size, employeetrain);
        Long total = employeetrainMapper.getTotal(employeetrain);
       RespPageBean respPageBean=new RespPageBean();
       respPageBean.setData(data);
       respPageBean.setTotal(total);

        return respPageBean;
    }

    /**
     * 添加员工培训
     * @param employeetrain
     */

    public Integer addEmployeeTrain(Employeetrain employeetrain){
        return employeetrainMapper.insertSelective(employeetrain);

    }

    /**
     * 更新员工培训
     * @param employeetrain
     * @return
     */
    public Integer updataEmployeeTrain(Employeetrain employeetrain){

       return employeetrainMapper.updateByPrimaryKeySelective(employeetrain);


    }

    /**
     * 删除员工培训
     * @param id
     * @return
     */
    public Integer deleEmployeeTrain(Integer id){
        return employeetrainMapper.deleteByPrimaryKey(id);
    }
}
