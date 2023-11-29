package com.anhkhoa.WebNT.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.anhkhoa.WebNT.mapper.chitietphieunhapMapper;


@Controller
public class HomeController {

	@Autowired
	chitietphieunhapMapper c;

	@GetMapping("/404")
	public String Error() {
		return "404";
	}

	@GetMapping("/ui")
	public String Test(Model model) {
		model.addAttribute("title", "Home");
		return "layout";
	}

	@GetMapping(value = { "/", "/home" })
	public String Index(Model model) {
		model.addAttribute("title", "Home");
		return "index";
	}

	@GetMapping("/login")
	public String Login() {
		return "login";
	}

	@GetMapping("/upload")
	public String Upload() {
		return "form_sourceimage";
	}

	@PostMapping("/sourceimage/save")
	public String handleFileUpload(@RequestParam("anh") MultipartFile anh) {
		if (!anh.isEmpty()) {
			try {
				// Lưu trữ tệp tin
				String fileName = anh.getOriginalFilename();
				Path filePath = Paths.get("src/main/resources/static/img/" + fileName);
				Files.write(filePath, anh.getBytes());

				// Xử lý nghiệp vụ khác nếu cần
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return "redirect:/upload";
	}	

}
