package org.iesvdm.controlador;


import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(Exception.class)
    public String manejarException(Exception exception) {

        log.info(exception.getMessage());

        return "404";
    }

    @ExceptionHandler(RuntimeException.class)
    public String manejarTodasLasExceptions(RuntimeException exception){


        log.info(exception.getMessage());

        return "404";
    }

}
