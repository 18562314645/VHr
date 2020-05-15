package org.javaboy.vhr.controller.emp;

import org.javaboy.vhr.logaop.Operation;
import org.javaboy.vhr.mapper.AdjustSalaryMapper;
import org.javaboy.vhr.model.*;
import org.javaboy.vhr.service.*;
import org.javaboy.vhr.utils.POIUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @作者 江南一点雨
 * @公众号 江南一点雨
 * @微信号 a_java_boy
 * @GitHub https://github.com/lenve
 * @博客 http://wangsong.blog.csdn.net
 * @网站 http://www.javaboy.org
 * @时间 2019-10-29 7:41
 */
@RestController
@RequestMapping("/employee/basic")
public class EmpBasicController {
    @Autowired
    EmployeeService employeeService;
    @Autowired
    NationService nationService;
    @Autowired
    PoliticsstatusService politicsstatusService;
    @Autowired
    JobLevelService jobLevelService;
    @Autowired
    PositionService positionService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    SalaryService salaryService;

    @GetMapping("/")

    public RespPageBean getEmployeeByPage(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, Employee employee, Date[] beginDateScope) {
        return employeeService.getEmployeeByPage(page, size, employee,beginDateScope);
    }

    @PostMapping("/")
    @Operation("新增员工")
    public RespBean addEmp(@RequestBody Employee employee) {
        if (employeeService.addEmp(employee) == 1) {
            return RespBean.ok("添加成功!");
        }
        return RespBean.error("添加失败!");
    }

    @DeleteMapping("/{id}")
    @Operation("删除员工")
    public RespBean deleteEmpByEid(@PathVariable Integer id) {
        if (employeeService.deleteEmpByEid(id) == 1) {
            return RespBean.ok("删除成功!");
        }
        return RespBean.error("删除失败!");
    }

    @PutMapping("/")
    @Operation("更新员工")
    public RespBean updateEmp(@RequestBody Employee employee) {
        if (employeeService.updateEmp(employee) == 1) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    @GetMapping("/nations")
    public List<Nation> getAllNations() {
        return nationService.getAllNations();
    }

    @GetMapping("/politicsstatus")
    public List<Politicsstatus> getAllPoliticsstatus() {
        return politicsstatusService.getAllPoliticsstatus();
    }

    @GetMapping("/joblevels")
    public List<JobLevel> getAllJobLevels() {
        return jobLevelService.getAllJobLevels();
    }

    @GetMapping("/positions")
    public List<Position> getAllPositions() {
        return positionService.getAllPositions();
    }

    @GetMapping("/maxWorkID")
    public RespBean maxWorkID() {
        RespBean respBean = RespBean.build().setStatus(200)
                .setObj(String.format("%08d", employeeService.maxWorkID() + 1));
        return respBean;
    }

    @GetMapping("/deps")
    public List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportData() {
        List<Employee> list = (List<Employee>) employeeService.getEmployeeByPage(null, null, new Employee(),null).getData();
        return POIUtils.employee2Excel(list);
    }

    @PostMapping("/import")
    public RespBean importData(MultipartFile file) throws IOException {
        List<Employee> list = POIUtils.excel2Employee(file, nationService.getAllNations(), politicsstatusService.getAllPoliticsstatus(), departmentService.getAllDepartmentsWithOutChildren(), positionService.getAllPositions(), jobLevelService.getAllJobLevels());
        if (employeeService.addEmps(list) == list.size()) {
            return RespBean.ok("上传成功");
        }
        return RespBean.error("上传失败");
    }

    @GetMapping("/searchName")
    public Employee searchEmpForName(@RequestParam("id") Integer id){
        return employeeService.searchEmpForName(id);
    }

    /**
     * 查询员工工资详情
     * @param page
     * @param size
     * @param employee
     * @return
     */
    @GetMapping("/adjustSalary")
    public RespPageBean getEmpSalaryByPage(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10")Integer size, Employee employee){
        return employeeService.getEmployeeByPageWithSalary(page,size,employee);
    }

    /**
     * 修该员工工资
     * @param eid
     * @param sid
     * @return
     */
    @PutMapping("/updataSalary")
    @Operation("修改员工工资")
    public RespBean updateEmployeeSalaryById(Integer eid,Integer sid){
        Integer result = employeeService.updateEmployeeSalaryById(eid, sid);

        if(result==1){
            return RespBean.ok("修改成功");
        }else{
            return RespBean.error("修改失败");
        }
    }


    @PostMapping("/empmove")
    @Operation("调动员工部门")
    public RespBean EmpMoveAndUpdataEmp(@RequestBody Employeeremove employeeremove){
        Integer i = employeeService.EmpMoveAndUpdataEmp(employeeremove);
        if(i==1){
            return RespBean.ok("员工部门以调动");
        }else {
            return RespBean.error("员工调动失败");
        }
    }

    @PostMapping("/addEmpAdjustSalary")
    @Operation("添加员工工资")
    public RespBean insertEmpSalarySelective(@RequestBody Employee employee){
        Integer result = employeeService.insertEmpSalarySelective(employee);
        if(result==1){
            return RespBean.ok("添加成功");
        }else {
            return RespBean.error("添加失败");
        }
    }
    @PutMapping("updateEmpSalarys")
    @Operation("修改员工工资")
    public RespBean updateEmpSalarys(@RequestParam("eid") Integer eid,@RequestParam("sid") Integer sid){
        Integer result = employeeService.updateEmpSalarys(eid, sid);
        if(result==1){
            return RespBean.ok("员工工资更新成功");
        }else {
            return RespBean.error("员工工资更新失败");
        }
    }

    /**
     * 获取所有调薪员工信息
     */
    @GetMapping("/getAllAdjustSalary")

    public List<AdjustSalary> getAllAdjustSalary(){
        return employeeService.getAllAdjustSalary();
    }

    /**
     * 通过eid获取单个员工调薪详情
     * @param id
     * @return
     */
    @GetMapping("/getAdjustSalaryById")
    @Operation("查询单个员工工资")
    public AdjustSalary getAdjustSalaryById(@RequestParam("id") Integer id){
       return   employeeService.getAdjustSalaryById(id);
    }
}
