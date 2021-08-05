package com.yuankun.exception;

import com.yuankun.response.Result;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(value = NoHandlerFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public Result  IllegalArgumentException(NoHandlerFoundException e){
        System.out.println("异常"+e);
        Result result=new Result();
        result.setMsg("错误");
        return result;
    }

}
