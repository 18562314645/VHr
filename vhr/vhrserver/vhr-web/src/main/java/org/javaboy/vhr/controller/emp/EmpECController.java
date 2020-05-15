package org.javaboy.vhr.controller.emp;

import org.javaboy.vhr.logaop.Operation;
import org.javaboy.vhr.model.Employeeec;
import org.javaboy.vhr.model.RespBean;
import org.javaboy.vhr.model.RespPageBean;
import org.javaboy.vhr.service.EmployeeECService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/personnel/ec")
public class EmpECController {

    @Autowired
    private EmployeeECService employeeECService;

    /**
     * 查询奖罚员工信息
     * @param page
     * @param size
     * @param employeeec
     * @param beginDateScope
     * @return
     */
    @GetMapping("/")
    public RespPageBean getEmployeeECByPage (@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, Employeeec employeeec, Date[] beginDateScope){
        return employeeECService.getEmployeeECByPage(page,size,employeeec,beginDateScope);
    }

    /**
     * 添加员工奖罚信息
     * @param employeeec
     * @return
     */
    @PostMapping("/")
    @Operation("添加员工降罚信息")
    public RespBean addEmployeeEC(@RequestBody Employeeec employeeec){
        Integer i = employeeECService.addEmployeeEC(employeeec);
        if(i==1){
            return  RespBean.ok("添加成功");
        }
        return RespBean.error("添加失败");
    }

    /**
     * 通过id查询单个员工奖罚实体
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Employeeec getEmployeeECById(@PathVariable("id") Integer id){
        Employeeec employeeec = employeeECService.getEmployeeECById(id);
        if(employeeec!=null){
            return employeeec;
        }
        return null;
    }

    /**
     * 修改员工奖罚消息
     * @param employeeec
     * @return
     */
    @PutMapping("/")
    @Operation("修改员奖罚信息")
    public RespBean updataEmployeeEC(Employeeec employeeec){
        Integer i = employeeECService.updataEmployeeEC(employeeec);
        if(i==1){
            return  RespBean.ok("修改成功");
        }
        return RespBean.error("修改失败");
    }

    /**
     * 删除员工奖罚
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @Operation("删除员工奖罚信息")
    public RespBean deleteEmployeeECById(@PathVariable("id") Integer id){
        Integer i = employeeECService.deleteEmployeeECById(id);
        if(i==1){
            return RespBean.ok("删除成功");
        }
        return RespBean.error("删除失败");
    }
}
