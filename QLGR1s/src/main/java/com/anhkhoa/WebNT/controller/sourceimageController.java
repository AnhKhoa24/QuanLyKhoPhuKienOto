package com.anhkhoa.WebNT.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.anhkhoa.WebNT.mapper.banthanhphanMapper;
import com.anhkhoa.WebNT.model.banthanhphan;
import com.anhkhoa.WebNT.model.banthanhphanExample;
import com.anhkhoa.WebNT.model.sourceimage;

@Controller
public class sourceimageController {

	@Autowired
	banthanhphanMapper btp;

	@GetMapping("/nguonanh")
	public String Form(Model model)
	{
		banthanhphanExample ex = new banthanhphanExample();
		List<banthanhphan> l = btp.selectByExample(ex);
		
		sourceimage them = new sourceimage();
		model.addAttribute("dsthanhphan", l);	
		model.addAttribute("model", them);
		return "form_nguonanh";
	}
	
	@PostMapping("/savenguonanh")
	public String Save(@RequestParam("img") MultipartFile file, @ModelAttribute("model") sourceimage model) throws IOException {

//		System.out.println(file.getSize());
//		System.out.println(file.getContentType());
//		System.out.println(file.getName());
//		System.out.println(file.getOriginalFilename());

		model.setLinkimg(file.getOriginalFilename());
		
		
		byte[] data = file.getBytes();
		
		File saveFile = new ClassPathResource("static/photo").getFile();
		String path = saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename();
		System.out.println(path);
		try {
			FileOutputStream fos = new FileOutputStream(path);
			fos.write(data);
			fos.close();
			System.out.println("upload success");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "redirect:/";
	}
}
