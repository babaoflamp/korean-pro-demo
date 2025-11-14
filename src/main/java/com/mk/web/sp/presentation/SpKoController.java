package com.mk.web.sp.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpKoController {

	@GetMapping("/speechpro/ko")
	public String spKo() {

		return "/sp/sp-ko";
	}

	@GetMapping("/speechpro/demo")
	public String demo() {

		return "/sp/sp-ko-demo";
	}
}
