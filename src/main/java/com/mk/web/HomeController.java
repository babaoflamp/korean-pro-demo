package com.mk.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

  @GetMapping("/")
  public String moveMain() {
    return "sp/sp-ko-demo";
  }
}
  