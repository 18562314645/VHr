package org.javaboy.vhr.service;

import org.javaboy.vhr.mapper.EmployeeecMapper;
import org.javaboy.vhr.model.Employeeec;
import org.javaboy.vhr.model.RespPageBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class EmployeeECService {

    @Autowired
    private EmployeeecMapper employeeecMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public final static Logger logger = LoggerFactory.getLogger(EmployeeECService.class);

    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
    SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
    DecimalFormat decimalFormat = new DecimalFormat("##.00");

    /**
     * 查询奖罚员工信息
     * @param page
     * @param size
     * @param employeeec
     * @param beginDateScope
     * @return
     */
    public RespPageBean getEmployeeECByPage(Integer page, Integer size, Employeeec employeeec, Date[] beginDateScope){
        if(page !=null && size !=null){
            page=(page-1)*size;
        }
        List<Employeeec> employeeECByPage = employeeecMapper.getEmployeeECByPage(page, size, employeeec, beginDateScope);
        Long total = employeeecMapper.getTotal(employeeec);
        RespPageBean bean=new RespPageBean();
        bean.setData(employeeECByPage);
        bean.setTotal(total);
        return bean;
    }

    /**
     * 添加员工奖罚
     * @param employeeec
     * @return
     */
    public Integer addEmployeeEC(Employeeec employeeec){
        return  employeeecMapper.insertSelective(employeeec);

    }

    /**
     * 通过员工ID查询单个员工的奖罚信息
     * @param empId
     * @return
     */
    public Employeeec getEmployeeECById(Integer empId){
       return employeeecMapper.selectByPrimaryKey(empId);
    }

    /**
     * 修改员工奖罚信息
     * @param employeeec
     * @return
     */
    public Integer updataEmployeeEC(Employeeec employeeec){
        return employeeecMapper.updateByPrimaryKeySelective(employeeec);
    }

    /**
     * 删除员工奖罚信息
     * @param empId
     * @return
     */
    public Integer deleteEmployeeECById(Integer empId){
        return employeeecMapper.deleteByPrimaryKey(empId);
    }
}
