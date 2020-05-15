package org.javaboy.vhr.controller.system;

import org.javaboy.vhr.logaop.Operation;
import org.javaboy.vhr.model.OpLog;
import org.javaboy.vhr.model.RespPageBean;
import org.javaboy.vhr.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys/log")
public class SysLogController {

    @Autowired
    private SysLogService sysLogService;
    @GetMapping("/")

    public RespPageBean getSysLog(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size,OpLog opLog){
       return sysLogService.getSysLog(page,size,opLog);
    }
}
