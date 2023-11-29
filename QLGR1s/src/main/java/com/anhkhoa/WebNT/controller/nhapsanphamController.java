package com.anhkhoa.WebNT.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.anhkhoa.WebNT.mapper.banthanhphanMapper;
import com.anhkhoa.WebNT.mapper.chitietphieunhapMapper;
import com.anhkhoa.WebNT.mapper.phanxuongMapper;
import com.anhkhoa.WebNT.mapper.phieunhapMapper;
import com.anhkhoa.WebNT.model.banthanhphan;
import com.anhkhoa.WebNT.model.banthanhphanExample;
import com.anhkhoa.WebNT.model.chitietphieunhap;
import com.anhkhoa.WebNT.model.chitietphieunhapExample;
import com.anhkhoa.WebNT.model.nhapsanpham;
import com.anhkhoa.WebNT.model.phanxuong;
import com.anhkhoa.WebNT.model.phanxuongExample;
import com.anhkhoa.WebNT.model.phieunhap;
import com.anhkhoa.WebNT.model.phieunhapExample;

@Controller
public class nhapsanphamController {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired 
	banthanhphanMapper map_banthanhphan;
	
	@Autowired
	phanxuongMapper map_phanxuong;
	
	@Autowired
	phieunhapMapper map_phieunhap;
	
	@Autowired
	chitietphieunhapMapper map_chitietphieunhap;
	
	@GetMapping("/nhapsanpham/them")
	public String Create(Model model)
	{
		banthanhphanExample ex = new banthanhphanExample();
		List<banthanhphan> list = map_banthanhphan.selectByExample(ex);
		model.addAttribute("dsbanthanhphan", list);
		
		phanxuongExample ex1 = new phanxuongExample();
		List<phanxuong> lis1 = map_phanxuong.selectByExample(ex1);
		model.addAttribute("dsphanxuong", lis1);
		
		nhapsanpham them = new nhapsanpham();
		model.addAttribute("model", them);
		model.addAttribute("title", "Thêm sản phẩm nhập");
		
		return "form_nhapsanpham";
	}
	
	@PostMapping("/nhapsanpham/save")
	public String SaveCreate(@ModelAttribute("model") nhapsanpham model,@RequestParam("sanpham") List<Integer> sanpham,
			@RequestParam("soluong") List<Integer> soluong, @RequestParam("gianhap") List<Integer> gianhap, RedirectAttributes ra )
	{
		
		//Xử lý phiếu nhập
		phieunhapExample exn = new phieunhapExample();
		List<phieunhap> list_pn = map_phieunhap.selectByExample(exn);
		Integer sophieunhap = 300;
		if(list_pn.size() > 0)
		{
			phieunhap take = list_pn.get(list_pn.size()-1);
			sophieunhap = take.getSophieunhap()+1;
		}
		phieunhap them = new phieunhap();
		them.setMaphanxuong(model.getMaphanxuong());
		them.setNgayphieunhap(model.getNgayphieunhap());
		them.setSophieunhap(sophieunhap);
		map_phieunhap.insert(them);
		System.out.println("Insert phieunhap successfully");
		
		List<Integer> sanphamMoi = new ArrayList<>();
		List<Integer> soluongMoi = new ArrayList<>();
		List<Integer> gianhapMoi = new ArrayList<>();

		for (int i = 0; i < sanpham.size(); i++) {
			Integer sp = sanpham.get(i);
			int sl = soluong.get(i);
			int gn = gianhap.get(i);
			if (!sanphamMoi.contains(sp)) {
				sanphamMoi.add(sp);
				soluongMoi.add(sl);
				gianhapMoi.add(gn);
			} else {
				int index = sanphamMoi.indexOf(sp);
				soluongMoi.set(index, soluongMoi.get(index) + sl);
			}
		}
		
		chitietphieunhap themmoi = new chitietphieunhap();
		for (int i = 0; i < sanphamMoi.size(); i++)
		{
			themmoi.setSophieunhap(sophieunhap);
			themmoi.setMabanthanhphan(sanphamMoi.get(i));
			themmoi.setSoluong(soluongMoi.get(i));
			themmoi.setGianhap(gianhapMoi.get(i));
			map_chitietphieunhap.insert(themmoi);
		}
		System.out.println("Insert "+sanphamMoi.size()+" rows into chitietphieunhap successfully");
		ra.addFlashAttribute("success", "Thêm thành công phiếu nhập "+sophieunhap);
		return "redirect:/nhapsanpham";
	}
	
	@GetMapping("/nhapsanpham")
	public String ShowView(Model model, @RequestParam(value = "page", defaultValue = "1") int trang,
			@RequestParam(value = "keyword", defaultValue = "") String timkiem) {
		model.addAttribute("keyword", timkiem);
		model.addAttribute("title", "Quản lý đơn hàng");

		String sql = "SELECT chitietphieunhap.*, phieunhap.NgayPhieuNhap, phanxuong.*, SUM(chitietphieunhap.GiaNhap*chitietphieunhap.SoLuong) AS TongTien\r\n"
				+ "FROM chitietphieunhap\r\n"
				+ "JOIN phieunhap ON chitietphieunhap.SoPhieuNhap = phieunhap.SoPhieuNhap\r\n"
				+ "JOIN phanxuong ON phieunhap.MaPhanXuong = phanxuong.MaPhanXuong\r\n"
				+ "WHERE phieunhap.SoPhieuNhap LIKE '%"+timkiem+"%'"
				+ "OR phanxuong.TenPhanXuong LIKE '%"+timkiem+"%'"
				+ "OR phanxuong.HoTenQuanDoc LIKE '%"+timkiem+"%'"
				+ "GROUP BY phieunhap.SoPhieuNhap"
				+" ORDER BY phieunhap.NgayPhieuNhap ";

		List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
		Integer sotimduoc = results.size();
		Integer sospmoitrang = 6;
		Integer sotrang = (int) Math.ceil((double) sotimduoc / sospmoitrang);
		Integer next = sospmoitrang * (trang - 1);
		String query = sql + " LIMIT " + sospmoitrang + " OFFSET " + next;
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		if (result.size() < 1) {
			sotrang = 1;
			model.addAttribute("notfound", "Không có thông tin phiếu nhập hàng");
		}
		model.addAttribute("model", result);
		model.addAttribute("sotrang", sotrang);
		if (trang == 1) {
			trang += 1;
			model.addAttribute("trang", trang);
		}
		model.addAttribute("trang", trang);

//		System.out.println(query);
		return "nhapsanpham";
	}
	@GetMapping("/nhapsanpham/details/{id}")
	public String Details(@PathVariable Integer id, Model model)
	{
		String query ="SELECT banthanhphan.TenBanThanhPhan, banthanhphan.GiaBan, chitietphieunhap.SoLuong, chitietphieunhap.GiaNhap, phieunhap.NgayPhieuNhap, phanxuong.*\r\n"
				+ "FROM banthanhphan JOIN chitietphieunhap ON banthanhphan.MaBanThanhPhan = chitietphieunhap.MaBanThanhPhan\r\n"
				+ "JOIN phieunhap ON chitietphieunhap.SoPhieuNhap = phieunhap.SoPhieuNhap\r\n"
				+ "JOIN phanxuong ON phieunhap.MaPhanXuong = phanxuong.MaPhanXuong\r\n"
				+ "WHERE phieunhap.SoPhieuNhap ="+id;
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		model.addAttribute("model", result);
		model.addAttribute("sophieunhap", id);
		int total = 0;
		for (Map<String, Object> row : result) {
			int giaNhap = (int) row.get("GiaNhap");
			int soLuong = (int) row.get("SoLuong");
			int subtotal = giaNhap * soLuong;
			total += subtotal;
		}
		model.addAttribute("tong", total);
		
		Map<String, Object> firstRow = result.get(0);
		Object ngayNhap = firstRow.get("NgayPhieuNhap");
		Object anh = firstRow.get("avt");
		Object name = firstRow.get("HoTenQuanDoc");
		Object phanxuong = firstRow.get("TenPhanXuong");
		
		model.addAttribute("ngayphieunhap", ngayNhap);
		model.addAttribute("anh", anh);
		model.addAttribute("name", name);
		model.addAttribute("phanxuong", phanxuong);
		model.addAttribute("title", "Chi tiết");
		
		
		return "chitiet_phieunhap";
	}
	
	@GetMapping("/nhapsanpham/delete/{id}")
	public String RequestDelete(@PathVariable Integer id, Model model)
	{
		String query ="SELECT banthanhphan.TenBanThanhPhan, banthanhphan.GiaBan, chitietphieunhap.SoLuong, chitietphieunhap.GiaNhap, phieunhap.NgayPhieuNhap, phanxuong.*\r\n"
				+ "FROM banthanhphan JOIN chitietphieunhap ON banthanhphan.MaBanThanhPhan = chitietphieunhap.MaBanThanhPhan\r\n"
				+ "JOIN phieunhap ON chitietphieunhap.SoPhieuNhap = phieunhap.SoPhieuNhap\r\n"
				+ "JOIN phanxuong ON phieunhap.MaPhanXuong = phanxuong.MaPhanXuong\r\n"
				+ "WHERE phieunhap.SoPhieuNhap ="+id;
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		model.addAttribute("model", result);
		model.addAttribute("sophieunhap", id);
		int total = 0;
		for (Map<String, Object> row : result) {
			int giaNhap = (int) row.get("GiaNhap");
			int soLuong = (int) row.get("SoLuong");
			int subtotal = giaNhap * soLuong;
			total += subtotal;
		}
		model.addAttribute("tong", total);
		
		Map<String, Object> firstRow = result.get(0);
		Object ngayNhap = firstRow.get("NgayPhieuNhap");
		Object anh = firstRow.get("avt");
		Object name = firstRow.get("HoTenQuanDoc");
		Object phanxuong = firstRow.get("TenPhanXuong");
		
		model.addAttribute("ngayphieunhap", ngayNhap);
		model.addAttribute("anh", anh);
		model.addAttribute("name", name);
		model.addAttribute("phanxuong", phanxuong);
		
		return "delete_nhapsanpham";
	}
	
	@PostMapping("/nhapsanpham/deleteok")
	public String Delete(@PathParam("sophieunhap") Integer sophieunhap, RedirectAttributes ra)
	{
		chitietphieunhapExample ex = new chitietphieunhapExample();
		ex.createCriteria().andSophieunhapEqualTo(sophieunhap);
		map_chitietphieunhap.deleteByExample(ex);
		
		map_phieunhap.deleteByPrimaryKey(sophieunhap);
		ra.addFlashAttribute("success", "Xóa thành công phiếu nhập "+sophieunhap+" !");
		return "redirect:/nhapsanpham";
	}
	
	@GetMapping("/nhapsanpham/edit/{id}")
	public String Update (@PathVariable Integer id, Model model)
	{
		phieunhap take = map_phieunhap.selectByPrimaryKey(id);
		nhapsanpham luu = new nhapsanpham();
		luu.setMaphanxuong(take.getMaphanxuong());
		luu.setNgayphieunhap(take.getNgayphieunhap());
		luu.setSophieunhap(id);
		phanxuongExample ex = new phanxuongExample();
		List<phanxuong>dsphanxuong = map_phanxuong.selectByExample(ex);
		
		model.addAttribute("model", luu);
		model.addAttribute("dsphanxuong", dsphanxuong);
		
		
		chitietphieunhapExample ex1 = new chitietphieunhapExample();
		ex1.createCriteria().andSophieunhapEqualTo(id);
		List<chitietphieunhap> list = map_chitietphieunhap.selectByExample(ex1);
		
		banthanhphanExample ex2 = new banthanhphanExample();
		List<banthanhphan>dsbanthanhphan = map_banthanhphan.selectByExample(ex2);
		
		model.addAttribute("model2", list);
		model.addAttribute("dsbanthanhphan", dsbanthanhphan);
		
		return "edit_nhapsanpham";
	}
	
	@PostMapping("/nhapsanpham/editsave")
	public String EditSave(@ModelAttribute("model") nhapsanpham model,@RequestParam("sanpham") List<Integer> sanpham,
			@RequestParam("soluong") List<Integer> soluong, @RequestParam("gianhap") List<Integer> gianhap, RedirectAttributes ra )
	{
		//Xử lý phiếu nhập
		phieunhap change = new phieunhap();
		change.setSophieunhap(model.getSophieunhap());
		change.setMaphanxuong(model.getMaphanxuong());
		change.setNgayphieunhap(model.getNgayphieunhap());
		map_phieunhap.updateByPrimaryKey(change);
		
		
		//Xử lý chitietphieunhap
		chitietphieunhapExample ex = new chitietphieunhapExample();
		ex.createCriteria().andSophieunhapEqualTo(model.getSophieunhap());
		map_chitietphieunhap.deleteByExample(ex);
		
		List<Integer> sanphamMoi = new ArrayList<>();
		List<Integer> soluongMoi = new ArrayList<>();
		List<Integer> gianhapMoi = new ArrayList<>();

		for (int i = 0; i < sanpham.size(); i++) {
			Integer sp = sanpham.get(i);
			int sl = soluong.get(i);
			int gn = gianhap.get(i);
			if (!sanphamMoi.contains(sp)) {
				sanphamMoi.add(sp);
				soluongMoi.add(sl);
				gianhapMoi.add(gn);
			} else {
				int index = sanphamMoi.indexOf(sp);
				soluongMoi.set(index, soluongMoi.get(index) + sl);
			}
		}
		
		chitietphieunhap themmoi = new chitietphieunhap();
		for (int i = 0; i < sanphamMoi.size(); i++)
		{
			themmoi.setSophieunhap(model.getSophieunhap());
			themmoi.setMabanthanhphan(sanphamMoi.get(i));
			themmoi.setSoluong(soluongMoi.get(i));
			themmoi.setGianhap(gianhapMoi.get(i));
			map_chitietphieunhap.insert(themmoi);
		}
		
		ra.addFlashAttribute("success", "Cập nhật thành công phiếu nhập "+model.getSophieunhap());
		return "redirect:/nhapsanpham";
	}
	
}
