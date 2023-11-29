package com.anhkhoa.WebNT.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.anhkhoa.WebNT.mapper.phanxuongMapper;
import com.anhkhoa.WebNT.mapper.phieunhapMapper;
import com.anhkhoa.WebNT.model.phanxuong;
import com.anhkhoa.WebNT.model.phanxuongExample;
import com.anhkhoa.WebNT.model.phieunhap;
import com.anhkhoa.WebNT.model.phieunhapExample;

@Controller
public class phanxuongController {

	@Autowired
	phanxuongMapper map_phanxuong;
	
	@Autowired
	phieunhapMapper map_phieunhap;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@GetMapping("/phanxuong")
	public String ShowView(Model model, @RequestParam(value = "page", defaultValue = "1") int trang,
			@RequestParam(value = "keyword", defaultValue = "") String timkiem) {

		model.addAttribute("keyword", timkiem);
		model.addAttribute("title", "Phiếu nhập");
		String query1 = "SELECT * FROM phanxuong WHERE TenPhanXuong LIKE '%" + timkiem + "%' OR HoTenQuanDoc LIKE '%"
				+ timkiem + "%' OR MaPhanXuong LIKE '%" + timkiem + "%'";

		List<Map<String, Object>> results = jdbcTemplate.queryForList(query1);
		Integer sotimduoc = results.size();
		Integer sospmoitrang = 6;
		Integer sotrang = (int) Math.ceil((double) sotimduoc / sospmoitrang);
		Integer next = sospmoitrang * (trang - 1);

		String query2 = query1+" LIMIT " + sospmoitrang + " OFFSET " + next;
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query2);
		if (result.size() < 1) {
			sotrang = 1;
			model.addAttribute("notfound", "Không có thông tin phân xưởng");
		}
		model.addAttribute("model", result);
		model.addAttribute("sotrang", sotrang);
		if (trang == 1) {
			trang += 1;
			model.addAttribute("trang", trang);
		}
		model.addAttribute("trang", trang);

		
		return "phanxuong";
	}
	
	@GetMapping("/phanxuong/new")
	public String Create(Model model)
	{
		phanxuong them = new phanxuong();
		
		model.addAttribute("title","Thêm phân xưởng");
		model.addAttribute("model",them);
		return "form_phanxuong";
	}
	
	@PostMapping("/phanxuong/savenew")
	public String Save(@RequestParam("img") MultipartFile file, @ModelAttribute("model") phanxuong model, RedirectAttributes ra) throws IOException {	
		phanxuongExample ex = new phanxuongExample();
		List<phanxuong> list = map_phanxuong.selectByExample(ex);
		Integer maphanxuong = 500;
		if(list.size() > 0)
		{
			phanxuong take = list.get(list.size() - 1);
			maphanxuong = take.getMaphanxuong()+1;
		}
		model.setMaphanxuong(maphanxuong);
		model.setAvt(file.getOriginalFilename());
		map_phanxuong.insert(model);

		byte[] data = file.getBytes();
		
		File saveFile = new ClassPathResource("static/photo").getFile();
		String path = saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename();
		System.out.println(path);
		try {
			FileOutputStream fos = new FileOutputStream(path);
			fos.write(data);
			fos.close();
			System.out.println("success");
		} catch (IOException e) {
			e.printStackTrace();
		}
		ra.addFlashAttribute("success", "Thêm thành công!");
		return "redirect:/phanxuong";
	}
	
	@GetMapping("/phanxuong/edit/{id}")
	public String Edit(@PathVariable Integer id, Model model)
	{
		phanxuong change = map_phanxuong.selectByPrimaryKey(id);
		model.addAttribute("model", change);
		model.addAttribute("title", "Cập nhật phân xưởng");
		
		
		
		
		return "edit_phanxuong";
	}
	
	@PostMapping("/phanxuong/editsave")
	public String EditSave(@RequestParam("img") MultipartFile file,
			@ModelAttribute("model") phanxuong model,
			RedirectAttributes ra) throws IOException
	{
		
		if(!file.isEmpty())
		{
			model.setAvt(file.getOriginalFilename());
			byte[] data = file.getBytes();
			
			File saveFile = new ClassPathResource("static/photo").getFile();
			String path = saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename();
			System.out.println(path);
			try {
				FileOutputStream fos = new FileOutputStream(path);
				fos.write(data);
				fos.close();
				System.out.println("success");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		map_phanxuong.updateByPrimaryKey(model);
		
		
		return "redirect:/phanxuong";
	}
	
	
	@GetMapping("/phanxuong/delete/{id}")
	public String RequestDelete(@PathVariable Integer id, Model model)
	{
	
		phanxuong take = map_phanxuong.selectByPrimaryKey(id);
		
		model.addAttribute("model", take);
		model.addAttribute("title","Yêu cầu xóa");
		
		return "delete_phanxuong";
	}
	
	
	@PostMapping("/phanxuong/deleteok")
	public String Delete(@ModelAttribute("model") phanxuong model, RedirectAttributes ra)
	{
		phieunhapExample ex = new phieunhapExample();
		ex.createCriteria().andMaphanxuongEqualTo(model.getMaphanxuong());
		List<phieunhap> lisst = map_phieunhap.selectByExample(ex);
		if(lisst.size() > 0)
		{
			ra.addFlashAttribute("error", "Phân xưởng "+model.getTenphanxuong()+" vẫn còn tồn tại "+lisst.size());
		}
		else
		{
			map_phanxuong.deleteByPrimaryKey(model.getMaphanxuong());
			ra.addFlashAttribute("success", "Xóa thành công phân xưởng "+model.getTenphanxuong());
		}
	
		return "redirect:/phanxuong";
	}
	
	
	
	
	
	
}
