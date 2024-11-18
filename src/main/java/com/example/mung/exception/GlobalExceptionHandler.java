package com.example.mung.exception;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException() {
        return "redirect:/mainPage"; // /mainPage로 리다이렉트
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public String handle404(Model model, HttpServletRequest httr) {
        model.addAttribute("errorMessage", "페이지를 찾을 수 없습니다.");
        model.addAttribute("referer","http://localhost:8088/mainPage");
        model.addAttribute("referer","http://localhost:8088/mainPage");
        System.out.println(model.getAttribute("referer"));
        return "error/404"; // 404 에러 페이지로 이동
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public String handle405(Model model, HttpServletRequest httr) {
        model.addAttribute("errorMessage", "허용되지 않은 메서드입니다.");
        model.addAttribute("referer","http://localhost:8088/mainPage");
        model.addAttribute("referer","http://localhost:8088/mainPage");
        System.out.println(model.getAttribute("referer"));
        return "error/405"; // 405 에러 페이지로 이동
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public String handle500(Model model, HttpServletRequest httr) {
        model.addAttribute("errorMessage", "서버 오류가 발생했습니다.");
        model.addAttribute("referer","http://localhost:8088/mainPage");
        model.addAttribute("referer","http://localhost:8088/mainPage");
        System.out.println(model.getAttribute("referer"));
        return "error/500"; // 500 에러 페이지로 이동
    }


}
