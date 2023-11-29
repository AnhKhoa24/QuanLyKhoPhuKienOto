package com.anhkhoa.WebNT.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
import com.anhkhoa.WebNT.mapper.sourceimageMapper;
import com.anhkhoa.WebNT.model.banthanhphan;
import com.anhkhoa.WebNT.model.banthanhphanExample;
import com.anhkhoa.WebNT.model.sourceimage;

@Controller
public class anhbtpController {

	@Autowired
	banthanhphanMapper btp;
	@Autowired
	sourceimageMapper si;

	@GetMapping("/themanh")
	public String Them(Model model) {
		banthanhphanExample ex = new banthanhphanExample();
		List<banthanhphan> ds = btp.selectByExample(ex);

		model.addAttribute("dsbanthanhphan", ds);
		sourceimage moi = new sourceimage();
		model.addAttribute("model", moi);
		return "a_test";
	}

	@PostMapping("/savethem")
	public String Save(@RequestParam MultipartFile img, @ModelAttribute("model") sourceimage model) {
		model.setLinkimg(img.getOriginalFilename());

		if (img != null) {
			si.insert(model);
			try {
				File saveFile = new ClassPathResource("static/photo").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + img.getOriginalFilename());
				System.out.println(path);
				Files.copy(img.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				

			} catch (Exception e) {
				
			}
		}

		return "redirect:/xemanh";
	}

}
