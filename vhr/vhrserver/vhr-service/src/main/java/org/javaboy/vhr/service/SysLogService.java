package org.javaboy.vhr.service;

import org.javaboy.vhr.mapper.OpLogMapper;
import org.javaboy.vhr.model.OpLog;
import org.javaboy.vhr.model.RespPageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysLogService {

    @Autowired
    private OpLogMapper opLogMapper;

    public RespPageBean getSysLog(Integer page, Integer size, OpLog opLog){
        if(page!=null && size!=null){
            page=(page-1)*size;
        }
        List<OpLog> data = opLogMapper.selectAll(page,size,opLog);
        Long total = opLogMapper.getTotal(opLog);
        RespPageBean respPageBean=new RespPageBean();
        respPageBean.setTotal(total);
        respPageBean.setData(data);
        return respPageBean;
    }

    public Integer saveSysLog(OpLog opLog){
        return opLogMapper.insertSelective(opLog);

    }
}
