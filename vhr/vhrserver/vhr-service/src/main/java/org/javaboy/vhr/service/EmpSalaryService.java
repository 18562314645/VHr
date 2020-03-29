package org.javaboy.vhr.service;

import org.javaboy.vhr.mapper.EmpSalaryMapper;
import org.javaboy.vhr.mapper.EmployeeMapper;
import org.javaboy.vhr.model.EmpSalary;
import org.javaboy.vhr.model.Employee;
import org.javaboy.vhr.model.RespPageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpSalaryService {

    @Autowired
    private EmpSalaryMapper empSalaryMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 查询员工工资表
     * @param page
     * @param size
     * @param employee
     * @return
     */
    public RespPageBean getEmpSalaryByPage(Integer page, Integer size, Employee employee){
        if(page!=null && size!=null){
            page=(page-1)*size;
        }
        List<Employee> data = employeeMapper.getEmpSalaryByPageWithSalary(page, size, employee);
        Long total = employeeMapper.getTotalEmpSalary(employee, null);
        RespPageBean bean=new RespPageBean();
        bean.setData(data);
        bean.setTotal(total);
        return bean;
    }

    /**
     * 添加员工工资表
     * @param empSalary
     * @return
     */
    public Integer addEmpSalary(EmpSalary empSalary){
        return empSalaryMapper.insertSelective(empSalary);

    }

    /**
     * 修改员工工资表
     * @param empSalary
     * @return
     */
    public Integer updateEmpSalary(EmpSalary empSalary){
        return empSalaryMapper.updateByPrimaryKey(empSalary);
    }

    /**
     * 删除员工工资
     * @param id
     * @return
     */
    public Integer delteEmpSalary(Integer id){
        return empSalaryMapper.deleteByPrimaryKey(id);
    }
}
