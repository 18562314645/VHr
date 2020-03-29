package org.javaboy.vhr.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.javaboy.vhr.enums.ExceptionEnums;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VhrException extends RuntimeException{

    private ExceptionEnums exceptionEnums;


}
