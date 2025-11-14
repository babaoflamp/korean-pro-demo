package com.mk.web.error.presentation;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class CustomErrorController implements ErrorController {

  @GetMapping("/403")
  public String forbidden() {

    return "error/403";
  }

  @GetMapping("/400")
  public String badRequest() {

    return "error/400";
  }

  @GetMapping("/404")
  public String notFound() {

    return "error/404";
  }

}
