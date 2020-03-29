package org.javaboy.vhr.org.javaboy.vhr.vo;

import lombok.Data;
import org.javaboy.vhr.enums.ExceptionEnums;

@Data
public class ExcceptionResult {
    private int status;
    private String mssage;
    private Long timestamp;

    public ExcceptionResult(ExceptionEnums em) {
        this.status = em.getCode();
        this.mssage = em.getMsg();
        this.timestamp = System.currentTimeMillis();
    }
}
