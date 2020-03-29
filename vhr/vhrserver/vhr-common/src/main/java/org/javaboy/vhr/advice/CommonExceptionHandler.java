package org.javaboy.vhr.advice;

import org.javaboy.vhr.enums.ExceptionEnums;
import org.javaboy.vhr.exception.VhrException;
import org.javaboy.vhr.org.javaboy.vhr.vo.ExcceptionResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(VhrException.class)
    public ResponseEntity handlerRunTimeException(VhrException e){
        ExceptionEnums enums = e.getExceptionEnums();
        return ResponseEntity.status(enums.getCode()).body(new ExcceptionResult(e.getExceptionEnums()));
    }
}
