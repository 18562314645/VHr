package org.javaboy.vhr.controller.emp;

import org.javaboy.vhr.logaop.Operation;
import org.javaboy.vhr.model.EmpSalary;
import org.javaboy.vhr.model.Employee;
import org.javaboy.vhr.model.RespBean;
import org.javaboy.vhr.model.RespPageBean;
import org.javaboy.vhr.service.EmpSalaryService;
import org.javaboy.vhr.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/emp/salary")
public class EmpSalaryController {

    @Autowired
    private EmpSalaryService empSalaryService;
    @Autowired
    private EmployeeService employeeService;

    /**
     * 查询员工工资表
     * @param page
     * @param size
     * @param employee
     * @return
     */
    @GetMapping("/")
    public RespPageBean getEmpSalaryByPage(@RequestParam("page") Integer page, @RequestParam("size") Integer size, Employee employee){
        return empSalaryService.getEmpSalaryByPage(page, size, employee);
    }

    /**
     * 添加员工工资表
     * @param empSalary
     * @return
     */
    @PostMapping("/")
    @Operation("添加员工工资表")
    public RespBean addEmpSalary(@RequestBody EmpSalary empSalary){
        Integer result = empSalaryService.addEmpSalary(empSalary);
        if(result==1){
            return RespBean.ok("添加成功");
        }else {
            return RespBean.error("添加失败");
        }
    }

    /**
     * 修改员工工资表
     * @param empSalary
     * @return
     */
    @PutMapping("/")
    @Operation("修改员工工资表")
    public RespBean updateEmpSalary(@RequestBody EmpSalary empSalary){
        if(empSalaryService.updateEmpSalary(empSalary)==1){
            return RespBean.ok("修改成功");
        }else {
            return RespBean.error("修改失败");
        }
    }

    /**
     * 删除员工工资
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @Operation("删除员工工资表")
    public RespBean delteEmpSalary(@PathVariable("id") Integer id){
        if(empSalaryService.delteEmpSalary(id)==1){
            return RespBean.ok("删除成功");
        }else {
            return RespBean.error("删除失败");
        }
    }

}
