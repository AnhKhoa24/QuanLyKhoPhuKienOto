package com.anhkhoa.WebNT.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.anhkhoa.WebNT.mapper.accountMapper;
import com.anhkhoa.WebNT.model.account;
import com.anhkhoa.WebNT.service.EmailService;

@Controller
public class SendMailController {

	@Autowired
    private EmailService emailService;
	
	@Autowired
	accountMapper ac_map;
	
	@GetMapping("/laylaimk")
	public String laymk()
	{
		return "nhaptentaikhoan";
	}
	
	private Integer otp = 0;
	@PostMapping("/buoc2")
	public String buoc2(@RequestParam("ten") String name, RedirectAttributes ra, Model model)
	{
		account search = ac_map.selectByPrimaryKey(name);
		
		if(search == null)
		{
			ra.addFlashAttribute("error", "Tài khoản "+ name+" không tồn tại!");
			return "redirect:/laylaimk";
		}	
		
		model.addAttribute("info", search);
		
		double randomDouble = Math.random();
        randomDouble = randomDouble * 1000000 + 1;
        int randomInt = (int) randomDouble;
        otp = randomInt;
		String to = search.getGmail();
		String td =	"Mã xác minh KhoalaT";
		String nd = "Xin chào! "
				+ "Mã xác minh bạn cần dùng để truy cập vào tài khoản "+to+" là: "
				+randomInt;
		
		emailService.sendEmail(to, td, nd);
		
		return "nhap_otp";
	}
	@PostMapping("/setmatkhau")
	public String Setmk(@RequestParam("name") String name, @RequestParam("maotp") Integer maotp, Model model, RedirectAttributes ra)
	{
		if(!maotp.equals(otp))
		{
			ra.addFlashAttribute("error", "Mã OTP không chính xác!");
			return "redirect:/laylaimk";
		}
		
		account change = ac_map.selectByPrimaryKey(name);
		model.addAttribute("model", change);		
		return "setmatkhau";
	}
	@PostMapping("/setmatkhauok")
	public String setmkok(@ModelAttribute("model") account model, RedirectAttributes ra, @RequestParam("password") String password)
	{
		model.setPassword(password);
		ac_map.updateByPrimaryKey(model);
		return "redirect:/login";
	}
	
	
	@GetMapping("/gui")
	public String hahah()
	{
		double randomDouble = Math.random();
        randomDouble = randomDouble * 1000000 + 1;
        int randomInt = (int) randomDouble;
		String to = "anhkhoa.24052003@gmail.com";
		String td =	"Spring test by anhkhoa";
		String nd = "Xin chào!"
				+ "Mã xác minh bạn cần dùng để truy cập vào tài khoản "+to+" là: "
				+randomInt;
		
		emailService.sendEmail(to, td, nd);
		return "redirect:/";
	}
	
}
