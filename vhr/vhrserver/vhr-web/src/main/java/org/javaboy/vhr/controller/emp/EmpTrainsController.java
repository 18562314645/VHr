package org.javaboy.vhr.controller.emp;

import org.apache.ibatis.annotations.Options;
import org.javaboy.vhr.logaop.Operation;
import org.javaboy.vhr.model.Employee;
import org.javaboy.vhr.model.Employeetrain;
import org.javaboy.vhr.model.RespBean;
import org.javaboy.vhr.model.RespPageBean;
import org.javaboy.vhr.service.EmployeeService;
import org.javaboy.vhr.service.EmployeeTrainService;
import org.javaboy.vhr.utils.POIUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/personnel/train")
public class EmpTrainsController {

    @Autowired
    private EmployeeTrainService employeeTrainService;

    @Autowired
    private EmployeeService employeeService;

    /**
     * 查询培训员工列表
     * @param page
     * @param size
     * @param employeetrain
     * @return
     */
    @GetMapping("/")

    public RespPageBean getEmployeeTrainByPage(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, Employeetrain employeetrain){
        return employeeTrainService.getEmployeeTrainByPage(page,size,employeetrain);
    }

    /**
     * 添加员工培训
     * @param employeetrain
     * @return
     */
    @PostMapping("/")
    @Operation("添加员工培训")
    public RespBean addEmployeeTrain(@RequestBody Employeetrain employeetrain){
        Integer result = employeeTrainService.addEmployeeTrain(employeetrain);
        if(result==1){
            return RespBean.ok("添加成功");
        }else{
            return RespBean.error("添加失败");
        }
    }

    /**
     * 更新员工培训
     * @param employeetrain
     * @return
     */
    @PutMapping("/")
    @Operation("更新员工培训")
    public RespBean updataEmployeeTrain(@RequestBody Employeetrain employeetrain){
        if(employeeTrainService.updataEmployeeTrain(employeetrain)==1){
            return RespBean.ok("更新数据成功");
        }else{
            return RespBean.error("更新数据失败");

        }
    }

    /**
     * 删除员工培训
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @Operation("删除员工培训")
    public RespBean deleEmployeeTrain(@PathVariable("id") Integer id){
        if(employeeTrainService.deleEmployeeTrain(id)==1){
            return RespBean.ok("删除数据成功");
        }else{
            return RespBean.error("删除数据失败");
        }
    }

    /**
     * 导出员工培训表
     * @return
     */
    @GetMapping("/export")
    @Operation("导出员工培训")
    public ResponseEntity<byte[]> exportData(){
        List<Employeetrain> list = (List<Employeetrain>) employeeTrainService.getEmployeeTrainByPage(null, null, new Employeetrain()).getData();
        return POIUtils.employeeTrain2Excel(list);
    }

    /*@PostMapping("/import")
    public RespBean importData(MultipartFile file){
        List<Employeetrain> list = POIUtils.excel2EmployeeTrain(file, (List<Employee>) employeeService.getEmployeeByPage(null, null, new Employee(), null));
       if(employeeTrainService.addEmployeeTrain()==list.size()){
           return RespBean.ok("上传成功");
       }else {

           return RespBean.error("上传失败");
       }
    }*/
}
