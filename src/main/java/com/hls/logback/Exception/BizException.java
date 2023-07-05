package com.hls.logback.Exception;

import com.hls.logback.Enum.ExceptionEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BizException extends RuntimeException {

    private int code;

    private String msg;

    public BizException(){
        super();
    }

    public BizException(ExceptionEnum exceptionEnum) {
        super();
        code = exceptionEnum.getCode();
        msg = exceptionEnum.getMsg();
    }
}

