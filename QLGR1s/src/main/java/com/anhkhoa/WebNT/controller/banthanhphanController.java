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

import com.anhkhoa.WebNT.mapper.banthanhphanMapper;
import com.anhkhoa.WebNT.mapper.sourceimageMapper;
import com.anhkhoa.WebNT.model.banthanhphan;
import com.anhkhoa.WebNT.model.banthanhphanExample;
import com.anhkhoa.WebNT.model.sourceimage;
import com.anhkhoa.WebNT.model.sourceimageExample;

@Controller
public class banthanhphanController {

	@Autowired
	banthanhphanMapper map_banthanhphan;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	sourceimageMapper map_anh;

	@GetMapping("/banthanhphan")
	public String ShowView(Model model, @RequestParam(value = "page", defaultValue = "1") int trang,
			@RequestParam(value = "keyword", defaultValue = "") String timkiem) {
		model.addAttribute("keyword", timkiem);
		model.addAttribute("title", "Sản phẩm");

		String sql = "SELECT * FROM banthanhphan WHERE banthanhphan.TenBanThanhPhan LIKE " + "'%" + timkiem + "%'";
		List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
		Integer sotimduoc = results.size();
		Integer sospmoitrang = 6;
		Integer sotrang = (int) Math.ceil((double) sotimduoc / sospmoitrang);
		Integer next = sospmoitrang * (trang - 1);
		String query = "SELECT * FROM banthanhphan WHERE banthanhphan.TenBanThanhPhan LIKE " + "'%" + timkiem
				+ "%' LIMIT " + sospmoitrang + " OFFSET " + next;
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		if (result.size() < 1) {
			sotrang = 1;
			model.addAttribute("notfound", "Không có thông tin sản phẩm");
		}
		model.addAttribute("model", result);
		model.addAttribute("sotrang", sotrang);
		if (trang == 1) {
			trang += 1;
			model.addAttribute("trang", trang);
		}
		model.addAttribute("trang", trang);
		return "banthanhphan";
	}

	@GetMapping("/banthanhphan/new")
	public String Create(Model model) {
		banthanhphan them = new banthanhphan();
		model.addAttribute("title", "Thêm sản phẩm");
		model.addAttribute("model", them);
		return "form_banthanhphan";
	}

	@PostMapping("/banthanhphan/new/save")
	public String CreateSave(RedirectAttributes ra, @ModelAttribute("model") banthanhphan model,
			@RequestParam("anh") MultipartFile[] anh) throws IOException {
		// xử lý banthanhphan
		banthanhphanExample ex = new banthanhphanExample();
		List<banthanhphan> listbtp = map_banthanhphan.selectByExample(ex);
		Integer masp = 100;
		if (listbtp.size() > 0) {
			banthanhphan take = listbtp.get(listbtp.size() - 1);
			masp = take.getMabanthanhphan() + 1;
		}
		model.setMabanthanhphan(masp);
		map_banthanhphan.insert(model);

		// xử lý ảnh

		sourceimage moi = new sourceimage();
		moi.setMabanthanhphan(masp);
		for (MultipartFile file : anh) {
			if (!file.isEmpty()) {
				moi.setLinkimg(file.getOriginalFilename());
				map_anh.insert(moi);

				byte[] data = file.getBytes();

				File saveFile = new ClassPathResource("static/photo").getFile();
				String path = saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename();
				System.out.println(path);
				try {
					FileOutputStream fos = new FileOutputStream(path);
					fos.write(data);
					fos.close();
					System.out.println("success  " + file.getOriginalFilename());
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}

		ra.addFlashAttribute("success", "Thêm thành công sản phẩm " + masp + " !");

		return "redirect:/banthanhphan";
	}

	@GetMapping("/banthanhpham/edit/{id}")
	public String Edit(Model model, @PathVariable Integer id) {

		banthanhphan change = map_banthanhphan.selectByPrimaryKey(id);
		model.addAttribute("model", change);
		model.addAttribute("title", "Cập nhật sản phẩm");

		sourceimageExample ex = new sourceimageExample();
		ex.createCriteria().andMabanthanhphanEqualTo(id);
		List<sourceimage> list = map_anh.selectByExample(ex);

		model.addAttribute("anh", list);

		return "update_banthanhphan";
	}

	@PostMapping("/banthanhphan/edit/save")
	public String EditSave(RedirectAttributes ra, @ModelAttribute("model") banthanhphan model,
			@RequestParam("anh") MultipartFile[] anh) throws IOException {
		// act banthanhphan
		map_banthanhphan.updateByPrimaryKey(model);

		// Xử lý sourceimage

		boolean check = false;
		for (MultipartFile file : anh) {
			if (!file.isEmpty()) {
				check = true;
				break;
			}
		}
		if (check == true) {
			sourceimageExample ex = new sourceimageExample();
			ex.createCriteria().andMabanthanhphanEqualTo(model.getMabanthanhphan());
			map_anh.deleteByExample(ex);
		}

		sourceimage moi = new sourceimage();
		moi.setMabanthanhphan(model.getMabanthanhphan());
		for (MultipartFile file : anh) {
			if (!file.isEmpty()) {
				moi.setLinkimg(file.getOriginalFilename());
				map_anh.insert(moi);

				byte[] data = file.getBytes();

				File saveFile = new ClassPathResource("static/photo").getFile();
				String path = saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename();
				System.out.println(path);
				try {
					FileOutputStream fos = new FileOutputStream(path);
					fos.write(data);
					fos.close();
					System.out.println("success  " + file.getOriginalFilename());
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}

		ra.addFlashAttribute("success", "Cập nhật thành công sản phẩm " + model.getMabanthanhphan());

		return "redirect:/banthanhphan";
	}

	@GetMapping("/banthanhpham/delete/{id}")
//	@PreAuthorize("hasRole('ADMIN')")
	public String RequestDelete(Model model, @PathVariable Integer id) {

		banthanhphan change = map_banthanhphan.selectByPrimaryKey(id);
		model.addAttribute("model", change);
		model.addAttribute("title", "Xác nhận xóa sản phẩm");

		return "delete_banthanhphan";
	}

	@PostMapping("/banthanhphan/deleteok")
	public String Delete(@ModelAttribute("model") banthanhphan model, RedirectAttributes ra) {
		Integer mabanthanhphan = model.getMabanthanhphan();

		/// check 1
		String query1 = "SELECT * FROM chitietphieunhap WHERE MaBanThanhPhan =" + mabanthanhphan;
		List<Map<String, Object>> result1 = jdbcTemplate.queryForList(query1);

		String query2 = "SELECT * FROM chitietphieuxuat WHERE MaBanThanhPhan =" + mabanthanhphan;
		List<Map<String, Object>> result2 = jdbcTemplate.queryForList(query2);

		if (result1.size() < 1 && result2.size() < 1) {
			
			sourceimageExample ext = new sourceimageExample();
			ext.createCriteria().andMabanthanhphanEqualTo(mabanthanhphan);
			map_anh.deleteByExample(ext);
			
			map_banthanhphan.deleteByPrimaryKey(model.getMabanthanhphan());
		
			ra.addFlashAttribute("success", "Xóa thành công!");
		} else if (result1.size() > 0 && result2.size() < 1) {
			ra.addFlashAttribute("error","Xóa thất bại: "+
					model.getTenbanthanhphan() + " tồn tại " + result1.size() + "- chi tiết phiếu nhập");
		} else if (result1.size() < 1 && result2.size() > 0) {
			ra.addFlashAttribute("error","Xóa thất bại: "+
					model.getTenbanthanhphan() + " tồn tại " + result2.size() + "- chi tiết phiếu xuất");
		} else if (result1.size() > 0 && result2.size() > 0) {
			ra.addFlashAttribute("error","Xóa thất bại: "+ model.getTenbanthanhphan() + " tồn tại " + result1.size()
					+ "-chi tiết phiếu nhập & " + result2.size() + "- chi tiết phiếu xuất");
		}

		return "redirect:/banthanhphan";
	}

	@GetMapping("/banthanhphan/chitiet/{id}")
	public String chitiet(@PathVariable Integer id, Model model) {

		sourceimageExample ex = new sourceimageExample();
		ex.createCriteria().andMabanthanhphanEqualTo(id);
		List<sourceimage> list = map_anh.selectByExample(ex);
		
		if (list.size() < 1)
		{
			sourceimage dephong = new sourceimage();
			dephong.setLinkimg("no_image.png");
			model.addAttribute("anhdau", dephong);
		}
		else
		{
			sourceimage first = list.get(0);
			list.remove(0);
			model.addAttribute("anhdau", first);
		}

		model.addAttribute("model", list);
		
		model.addAttribute("title", "Chi tiết sản phẩm");

		banthanhphan sp = map_banthanhphan.selectByPrimaryKey(id);
		model.addAttribute("info", sp);

		String sql = "SELECT SUM(chitietphieunhap.SoLuong) AS 'TongNhap'\r\n" + "FROM chitietphieunhap\r\n"
				+ "WHERE chitietphieunhap.MaBanThanhPhan = " + id + " GROUP BY chitietphieunhap.MaBanThanhPhan";
		List<Map<String, Object>> nhapl = jdbcTemplate.queryForList(sql);

		String sql1 = "SELECT SUM(chitietphieuxuat.SoLuong) AS 'TongXuat'\r\n" + "FROM chitietphieuxuat\r\n"
				+ "WHERE chitietphieuxuat.MaBanThanhPhan = " + id + " GROUP BY chitietphieuxuat.MaBanThanhPhan";
		List<Map<String, Object>> xuat = jdbcTemplate.queryForList(sql1);

		Integer kho = 0;
		Integer daban = 0;

		if (nhapl.size() > 0) {
			Map<String, Object> take = nhapl.get(0);
			Object slnhap = take.get("TongNhap");
			kho = Integer.valueOf(slnhap.toString());
		}
		if (xuat.size() > 0) {
			Map<String, Object> take = xuat.get(0);
			Object slxuat = take.get("TongXuat");
			daban = Integer.valueOf(slxuat.toString());
		}

		model.addAttribute("kho", kho-daban);
		model.addAttribute("daban", daban);

		return "chitiet_banthanhphan";
	}

}
