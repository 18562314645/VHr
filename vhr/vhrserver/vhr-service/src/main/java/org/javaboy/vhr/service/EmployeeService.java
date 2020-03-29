package org.javaboy.vhr.service;

import org.javaboy.vhr.mapper.*;
import org.javaboy.vhr.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @作者 江南一点雨
 * @公众号 江南一点雨
 * @微信号 a_java_boy
 * @GitHub https://github.com/lenve
 * @博客 http://wangsong.blog.csdn.net
 * @网站 http://www.javaboy.org
 * @时间 2019-10-29 7:44
 */
@Service
public class EmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    MailSendLogService mailSendLogService;
    @Autowired
    EmployeeremoveMapper employeeremoveMapper;
    @Autowired
    SalaryMapper salaryMapper;
    @Autowired
    EmpSalaryMapper empSalaryMapper;
    @Autowired
    AdjustSalaryMapper adjustSalaryMapper;

    public final static Logger logger = LoggerFactory.getLogger(EmployeeService.class);
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
    SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
    DecimalFormat decimalFormat = new DecimalFormat("##.00");

    public RespPageBean getEmployeeByPage(Integer page, Integer size, Employee employee, Date[] beginDateScope) {
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<Employee> data = employeeMapper.getEmployeeByPage(page, size, employee, beginDateScope);
        Long total = employeeMapper.getTotal(employee, beginDateScope);
        RespPageBean bean = new RespPageBean();
        bean.setData(data);
        bean.setTotal(total);
        return bean;
    }

    public Integer addEmp(Employee employee) {
        Date beginContract = employee.getBeginContract();
        Date endContract = employee.getEndContract();
        double month = (Double.parseDouble(yearFormat.format(endContract)) - Double.parseDouble(yearFormat.format(beginContract))) * 12 + (Double.parseDouble(monthFormat.format(endContract)) - Double.parseDouble(monthFormat.format(beginContract)));
        employee.setContractTerm(Double.parseDouble(decimalFormat.format(month / 12)));
        int result = employeeMapper.insertSelective(employee);
        if (result == 1) {
            Employee emp = employeeMapper.getEmployeeById(employee.getId());
            //生成消息的唯一id
            String msgId = UUID.randomUUID().toString();
            MailSendLog mailSendLog = new MailSendLog();
            mailSendLog.setMsgId(msgId);
            mailSendLog.setCreateTime(new Date());
            mailSendLog.setExchange(MailConstants.MAIL_EXCHANGE_NAME);
            mailSendLog.setRouteKey(MailConstants.MAIL_ROUTING_KEY_NAME);
            mailSendLog.setEmpId(emp.getId());
            mailSendLog.setTryTime(new Date(System.currentTimeMillis() + 1000 * 60 * MailConstants.MSG_TIMEOUT));
            mailSendLogService.insert(mailSendLog);
            rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME, MailConstants.MAIL_ROUTING_KEY_NAME, emp, new CorrelationData(msgId));
            rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME, MailConstants.MAIL_ROUTING_KEY_NAME, emp, new CorrelationData(msgId));
        }
        return result;
    }

    public Integer maxWorkID() {
        return employeeMapper.maxWorkID();
    }

    public Integer deleteEmpByEid(Integer id) {
        return employeeMapper.deleteByPrimaryKey(id);
    }

    public Integer updateEmp(Employee employee) {
        return employeeMapper.updateByPrimaryKeySelective(employee);
    }

    public Integer addEmps(List<Employee> list) {
        return employeeMapper.addEmps(list);
    }

    public RespPageBean getEmployeeByPageWithSalary(Integer page, Integer size,Employee employee) {
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<Employee> list = employeeMapper.getEmployeeByPageWithSalary(page, size,employee);
        RespPageBean respPageBean = new RespPageBean();
        respPageBean.setData(list);
        respPageBean.setTotal(employeeMapper.getTotal( employee,null));
        return respPageBean;
    }

    public Integer updateEmployeeSalaryById(Integer eid, Integer sid) {
        return employeeMapper.updateEmployeeSalaryById(eid, sid);
    }

    public Employee getEmployeeById(Integer empId) {
        return employeeMapper.getEmployeeById(empId);
    }

    public Employee searchEmpForName(Integer id){
        return employeeMapper.searchEmpForName(id);
    }

    /**
     * 获取当前员工部门信息
     * @param page
     * @param size
     * @param employee
     * @return
     */
    public RespPageBean getEmpNowDep(Integer page,Integer size,Employee employee){
        if(page!=null && size!=null){
            page = (page - 1) * size;
        }
        List<Employee> data = employeeMapper.getEmpNowDep(page, size, employee);
        Long total = employeeMapper.getTotal(employee, null);
        RespPageBean bean=new RespPageBean();
        bean.setData(data);
        bean.setTotal(total);
        return bean;
    }

    /**
     * 执行员工部门调动和更新员工基本信息表
     * @param employeeremove
     * @return
     */
    public Integer EmpMoveAndUpdataEmp(Employeeremove employeeremove){
        int j = employeeremoveMapper.insertSelective(employeeremove);
        if(j!=1){
            throw new RuntimeException("员工调动失败");
        }
        Employee employee=new Employee();
        employee.setId(employeeremove.getEid());
        employee.setJobLevelId(employeeremove.getAfterjobid());
        employee.setDepartmentId(employeeremove.getAfterdepid());
        int i = employeeMapper.updateByPrimaryKeySelective(employee);
        return i;
    }

    /**
     * 新增调薪员工到调薪表
     * @param employee
     * @return
     */
    public Integer insertEmpSalarySelective(Employee employee){
        return employeeMapper.insertEmpSalarySelective(employee);
    }

    /**
     * 更新empsalary表里的工资
     * @return
     */
    public Integer updateEmpSalarys(Integer eid,Integer sid){
        return empSalaryMapper.updateByPrimaryKeySelective(eid,sid);
    }

    /**
     * 通过eid获取单个员工调薪详情
     * @param id
     * @return
     */
    public AdjustSalary getAdjustSalaryById(Integer id){
        return adjustSalaryMapper.selectByPrimaryKey(id);

    }

    /**
     * 获取所有调薪员工信息
     * @return
     */
    public List<AdjustSalary> getAllAdjustSalary(){
        List<AdjustSalary> list = adjustSalaryMapper.getAllAdjustSalary();
        return list;
    }
}
