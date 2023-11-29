package com.anhkhoa.WebNT.controller;

import java.sql.Date;
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
import com.anhkhoa.WebNT.mapper.chitietphieuxuatMapper;
import com.anhkhoa.WebNT.mapper.khachhangMapper;
import com.anhkhoa.WebNT.mapper.phieuxuatMapper;
import com.anhkhoa.WebNT.model.banthanhphan;
import com.anhkhoa.WebNT.model.banthanhphanExample;

import com.anhkhoa.WebNT.model.chitietphieuxuat;
import com.anhkhoa.WebNT.model.khachhang;
import com.anhkhoa.WebNT.model.khachhangExample;
import com.anhkhoa.WebNT.model.phieuxuat;
import com.anhkhoa.WebNT.model.phieuxuatExample;
import com.anhkhoa.WebNT.model.xuatsanpham;

@Controller
public class xuatsanphamController {

	@Autowired
	banthanhphanMapper map_banthanhphan;

	@Autowired
	khachhangMapper map_khachhang;

	@Autowired
	phieuxuatMapper map_phieuxuat;

	@Autowired
	chitietphieuxuatMapper map_chitietphieuxuat;
	
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@GetMapping("/xuatsanpham")
	public String ShowView(Model model, @RequestParam(value = "page", defaultValue = "1") int trang,
			@RequestParam(value = "keyword", defaultValue = "") String timkiem) {
		model.addAttribute("keyword", timkiem);
		model.addAttribute("title", "Quản lý đơn hàng");

		String sql = "SELECT khachhang.*, phieuxuat.NgayPhieuXuat, phieuxuat.SoPhieuXuat, SUM(banthanhphan.GiaBan*chitietphieuxuat.SoLuong) AS 'TongTien'\r\n"
				+ "FROM banthanhphan JOIN chitietphieuxuat\r\n"
				+ "ON banthanhphan.MaBanThanhPhan = chitietphieuxuat.MaBanThanhPhan\r\n"
				+ "JOIN phieuxuat on chitietphieuxuat.SoPhieuXuat = phieuxuat.SoPhieuXuat\r\n"
				+ "JOIN khachhang ON phieuxuat.MaKH = khachhang.MaKH\r\n" + "WHERE phieuxuat.SoPhieuXuat LIKE '%"
				+ timkiem + "%'" + "OR khachhang.TenKH LIKE '%" + timkiem + "%'" + "GROUP BY phieuxuat.SoPhieuXuat"
				+" ORDER BY phieuxuat.NgayPhieuXuat DESC";

		List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
		Integer sotimduoc = results.size();
		Integer sospmoitrang = 6;
		Integer sotrang = (int) Math.ceil((double) sotimduoc / sospmoitrang);
		Integer next = sospmoitrang * (trang - 1);
		String query = sql + " LIMIT " + sospmoitrang + " OFFSET " + next;
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		if (result.size() < 1) {
			sotrang = 1;
			model.addAttribute("notfound", "Không có thông tin đơn hàng");
		}
		model.addAttribute("model", result);
		model.addAttribute("sotrang", sotrang);
		if (trang == 1) {
			trang += 1;
			model.addAttribute("trang", trang);
		}
		model.addAttribute("trang", trang);

//		System.out.println(query);
		return "xuatsanpham";
	}

	@GetMapping("/xuatsanpham/them")
	public String Them(Model model) {
		/*
		 * banthanhphanExample ex = new banthanhphanExample(); List<banthanhphan> list =
		 * map_banthanhphan.selectByExample(ex); model.addAttribute("dsbanthanhphan",
		 * list);
		 */
		String sql = "SELECT \r\n"
				+ "	B.MaBanThanhPhan,\r\n"
				+ "    B.TenBanThanhPhan,\r\n"
				+ "    COALESCE(SUM(CPN.SoLuong), 0) - COALESCE(SUM(CPX.SoLuong), 0) AS Kho\r\n"
				+ "FROM \r\n"
				+ "    banthanhphan B\r\n"
				+ "LEFT JOIN \r\n"
				+ "    chitietphieunhap CPN ON B.MaBanThanhPhan = CPN.MaBanThanhPhan\r\n"
				+ "LEFT JOIN \r\n"
				+ "    chitietphieuxuat CPX ON B.MaBanThanhPhan = CPX.MaBanThanhPhan\r\n"
				+ "GROUP BY \r\n"
				+ "    B.MaBanThanhPhan, B.TenBanThanhPhan\r\n";
		List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
		model.addAttribute("dssanpham", results);
		model.addAttribute("title", "phiếu bán hàng");
		xuatsanpham them = new xuatsanpham();
		model.addAttribute("model", them);
		return "form_themxuatsanpham";
	}
	
	private Integer checknhap(Integer mabanthanhphan)
	{
		Integer kho =0;
		String sql ="SELECT \r\n"
				+ "    B.MaBanThanhPhan,\r\n"
				+ "    B.TenBanThanhPhan,\r\n"
				+ "    COALESCE(SUM(CPN.SoLuong), 0) - COALESCE(SUM(CPX.SoLuong), 0) AS Kho\r\n"
				+ "FROM \r\n"
				+ "    banthanhphan B\r\n"
				+ "LEFT JOIN \r\n"
				+ "    chitietphieunhap CPN ON B.MaBanThanhPhan = CPN.MaBanThanhPhan\r\n"
				+ "LEFT JOIN \r\n"
				+ "    chitietphieuxuat CPX ON B.MaBanThanhPhan = CPX.MaBanThanhPhan\r\n"
				+ "    \r\n"
				+ "WHERE B.MaBanThanhPhan = "+mabanthanhphan
				+ " GROUP BY \r\n"
				+ "    B.MaBanThanhPhan, B.TenBanThanhPhan;\r\n"
				+ "";
		List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
		Map<String, Object> firstRow = results.get(0);
		Object TakeSL = firstRow.get("Kho");
		kho = Integer.valueOf(TakeSL.toString());
		return kho;
	}
	@PostMapping("/xuatsanpham/save")
	public String buyProducts(@RequestParam("sanpham") List<Integer> sanpham,
			@RequestParam("soluong") List<Integer> soluong, @ModelAttribute("model") xuatsanpham model,
			RedirectAttributes ra) {

		//Kiểm tra tồn kho
		//---------->Nạp chồng SL----->
		List<Integer> sanphamMoi = new ArrayList<>();
		List<Integer> soluongMoi = new ArrayList<>();

		for (int i = 0; i < sanpham.size(); i++) {
			Integer sp = sanpham.get(i);
			int sl = soluong.get(i);
			if (!sanphamMoi.contains(sp)) {
				sanphamMoi.add(sp);
				soluongMoi.add(sl);
			} else {
				int index = sanphamMoi.indexOf(sp);
				soluongMoi.set(index, soluongMoi.get(index) + sl);
			} 
		}
		//---------->Check kho----------->>>>
		
		for(int i = 0; i < sanphamMoi.size(); i++ )
		{
			Integer takesl = checknhap(sanphamMoi.get(i));
			if(takesl < soluongMoi.get(i))
			{
				ra.addFlashAttribute("error", "Thêm thất bại: kho không đủ sản phẩm!!!");
				return "redirect:/xuatsanpham";
				
			}
		}
		
		// Xử lý thông tin khách hàng

		khachhangExample exkh = new khachhangExample();
		exkh.createCriteria().andTenkhEqualTo(model.getTenkh()).andSdtEqualTo(model.getSdt());
		List<khachhang> lkh = map_khachhang.selectByExample(exkh);
		Integer makhthem;
		if (lkh.size() < 1) {
			Integer makh = 1000;
			khachhangExample exkh1 = new khachhangExample();
			List<khachhang> lkh1 = map_khachhang.selectByExample(exkh1);
			if (lkh1.size() > 0) {
				khachhang lay = lkh1.get(lkh1.size() - 1);
				makh = lay.getMakh() + 1;
			}
			khachhang them = new khachhang();
			them.setMakh(makh);
			them.setTenkh(model.getTenkh());
			them.setSdt(model.getSdt());
			them.setDiachi(model.getDiachi());
			map_khachhang.insert(them);
			makhthem = makh;
			System.out.println("Insert into khachhang successfully");
		} else {
			makhthem = lkh.get(0).getMakh();
		}
		System.out.println(makhthem);
		// Xử lý phiếu xuất

		phieuxuatExample expn = new phieuxuatExample();
		List<phieuxuat> lpx = map_phieuxuat.selectByExample(expn);
		Integer sophieuxuat = 200;
		if (lpx.size() > 0) {
			phieuxuat laycuoi = lpx.get(lpx.size() - 1);
			sophieuxuat = laycuoi.getSophieuxuat() + 1;
		}
		phieuxuat thempx = new phieuxuat();
		thempx.setSophieuxuat(sophieuxuat);
		thempx.setMakh(makhthem);
		thempx.setNgayphieuxuat(model.getNgayphieuxuat());
		map_phieuxuat.insert(thempx);
		System.out.println("Insert into phieuxuat successfully");
		// Xử lý nhiều chi tiết phiếu xuât

		chitietphieuxuat taomoi = new chitietphieuxuat();

		for (int i = 0; i < sanphamMoi.size(); i++) {
			taomoi.setMabanthanhphan(sanphamMoi.get(i));
			taomoi.setSoluong(soluongMoi.get(i));
			taomoi.setSophieuxuat(sophieuxuat);
			map_chitietphieuxuat.insert(taomoi);
		}

		System.out.println("Insert " + sanpham.size() + " rows into chitietphieuxuat successfully");
		ra.addFlashAttribute("success", "Thêm thành công đơn hàng");
		return "redirect:/xuatsanpham";
	}

	@GetMapping("/xuatsanpham/details/{id}")
	public String Details(Model model, @PathVariable Integer id) {
		String sql = "SELECT khachhang.*, banthanhphan.TenBanThanhPhan, banthanhphan.GiaBan, phieuxuat.NgayPhieuXuat, phieuxuat.SoPhieuXuat, chitietphieuxuat.SoLuong\r\n"
				+ "FROM banthanhphan JOIN chitietphieuxuat\r\n"
				+ "ON banthanhphan.MaBanThanhPhan = chitietphieuxuat.MaBanThanhPhan\r\n"
				+ "JOIN phieuxuat ON chitietphieuxuat.SoPhieuXuat = phieuxuat.SoPhieuXuat\r\n"
				+ "JOIN khachhang ON phieuxuat.MaKH = khachhang.MaKH\r\n" + "WHERE phieuxuat.SoPhieuXuat =" + id;
		List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);

		Map<String, Object> firstRow = results.get(0);
		Object tenKh = firstRow.get("TenKh");
		Object sdt = firstRow.get("Sdt");
		Object diachi = firstRow.get("DiaChi");
		Object sophieuxuat = firstRow.get("SoPhieuXuat");
		Object ngayphieuxuat = firstRow.get("NgayPhieuXuat");

		int total = 0;

		for (Map<String, Object> row : results) {
			int giaBan = (int) row.get("GiaBan");
			int soLuong = (int) row.get("SoLuong");
			int subtotal = giaBan * soLuong;
			total += subtotal;
		}

		model.addAttribute("tong", total);
		model.addAttribute("ten", tenKh);
		model.addAttribute("sdt", sdt);
		model.addAttribute("diachi", diachi);
		model.addAttribute("sophieuxuat", sophieuxuat);
		model.addAttribute("ngayphieuxuat", ngayphieuxuat);
		model.addAttribute("model", results);
		model.addAttribute("title", "Chi tiết");
		return "chitiet";
	}

	@GetMapping("/xuatsanpham/edit/{id}")
	public String Edt(@PathVariable Integer id, Model model) {

		String sql = "SELECT *\r\n"
				+ "FROM banthanhphan JOIN chitietphieuxuat ON banthanhphan.MaBanThanhPhan = chitietphieuxuat.MaBanThanhPhan\r\n"
				+ "JOIN phieuxuat ON chitietphieuxuat.SoPhieuXuat = phieuxuat.SoPhieuXuat\r\n"
				+ "JOIN khachhang ON phieuxuat.MaKH = khachhang.MaKH\r\n" + "WHERE phieuxuat.SoPhieuXuat = " + id;
		List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
		Map<String, Object> firstRow = results.get(0);
		xuatsanpham them = new xuatsanpham();
		them.setTenkh((String) firstRow.get("TenKh"));
		them.setDiachi((String) firstRow.get("DiaChi"));
		them.setSdt((String) firstRow.get("Sdt"));
		them.setNgayphieuxuat((Date) firstRow.get("NgayPhieuXuat"));
		them.setSophieuxuat((Integer) firstRow.get("SoPhieuXuat"));
		them.setMakh((Integer) firstRow.get("MaKh"));

		model.addAttribute("model2", results);
		model.addAttribute("model", them);

		banthanhphanExample ex = new banthanhphanExample();
		List<banthanhphan> list = map_banthanhphan.selectByExample(ex);
		model.addAttribute("dsbanthanhphan", list);
		return "edit_xuatsanpham";
	}

	@PostMapping("/xuatsanpham/editsave")
	public String EditSave(@RequestParam("sanpham") List<Integer> sanpham,
			@RequestParam("soluong") List<Integer> soluong, @ModelAttribute("model") xuatsanpham model,
			RedirectAttributes ra) {
		
		
		
		// Update khachhang
		String updateQuery = "UPDATE khachhang SET TenKH = ?, SDT = ?, DiaChi = ? WHERE MaKH = ?";
		jdbcTemplate.update(updateQuery, model.getTenkh(), model.getSdt(), model.getDiachi(), model.getMakh());

		// Update phieuxuat
		String updateQuery1 = "UPDATE phieuxuat SET NgayPhieuXuat=? WHERE SoPhieuXuat = ?";
		jdbcTemplate.update(updateQuery1, model.getNgayphieuxuat(), model.getSophieuxuat());
			
		// Update chitietphieuxuat
		String delete = "DELETE FROM chitietphieuxuat WHERE SoPhieuXuat = ?";
		jdbcTemplate.update(delete, model.getSophieuxuat());
		
		chitietphieuxuat taomoi = new chitietphieuxuat();

		List<Integer> sanphamMoi = new ArrayList<>();
		List<Integer> soluongMoi = new ArrayList<>();

		for (int i = 0; i < sanpham.size(); i++) {
			Integer sp = sanpham.get(i);
			int sl = soluong.get(i);
			if (!sanphamMoi.contains(sp)) {
				sanphamMoi.add(sp);
				soluongMoi.add(sl);
			} else {
				int index = sanphamMoi.indexOf(sp);
				soluongMoi.set(index, soluongMoi.get(index) + sl);
			}
		}
		for (int i = 0; i < sanphamMoi.size(); i++) {
			taomoi.setMabanthanhphan(sanphamMoi.get(i));
			taomoi.setSoluong(soluongMoi.get(i));
			taomoi.setSophieuxuat(model.getSophieuxuat());
			map_chitietphieuxuat.insert(taomoi);
		}
		
		
		ra.addFlashAttribute("success", "Cập nhật thành công đơn hàng "+model.getSophieuxuat()+" !");
		return "redirect:/xuatsanpham";
	}
	
	@GetMapping("/xuatsanpham/delete/{id}")
	public String RequestDel(Model model, @PathVariable Integer id)
	{
		String sql = "SELECT khachhang.*, banthanhphan.TenBanThanhPhan, banthanhphan.GiaBan, phieuxuat.NgayPhieuXuat, phieuxuat.SoPhieuXuat, chitietphieuxuat.SoLuong\r\n"
				+ "FROM banthanhphan JOIN chitietphieuxuat\r\n"
				+ "ON banthanhphan.MaBanThanhPhan = chitietphieuxuat.MaBanThanhPhan\r\n"
				+ "JOIN phieuxuat ON chitietphieuxuat.SoPhieuXuat = phieuxuat.SoPhieuXuat\r\n"
				+ "JOIN khachhang ON phieuxuat.MaKH = khachhang.MaKH\r\n" + "WHERE phieuxuat.SoPhieuXuat =" + id;
		List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);

		Map<String, Object> firstRow = results.get(0);
		Object tenKh = firstRow.get("TenKh");
		Object sdt = firstRow.get("Sdt");
		Object diachi = firstRow.get("DiaChi");
		Object sophieuxuat = firstRow.get("SoPhieuXuat");
		Object ngayphieuxuat = firstRow.get("NgayPhieuXuat");

		int total = 0;

		for (Map<String, Object> row : results) {
			int giaBan = (int) row.get("GiaBan");
			int soLuong = (int) row.get("SoLuong");
			int subtotal = giaBan * soLuong;
			total += subtotal;
		}

		model.addAttribute("tong", total);
		model.addAttribute("ten", tenKh);
		model.addAttribute("sdt", sdt);
		model.addAttribute("diachi", diachi);
		model.addAttribute("sophieuxuat", sophieuxuat);
		model.addAttribute("ngayphieuxuat", ngayphieuxuat);
		model.addAttribute("model", results);
		model.addAttribute("title", "Xóa đơn hàng ");
		model.addAttribute("makh",firstRow.get("MaKh"));
		
		return "delete_donhang";
	}
	
	@PostMapping("/xuatsanpham/deleteok")
	public String Delete(@PathParam("sophieuxuat") Integer sophieuxuat, @PathParam("makh") Integer makh, RedirectAttributes ra)
	{
		//Xóa chi tiết phiếu xuất
		String sql1 = "DELETE FROM chitietphieuxuat WHERE SoPhieuXuat =?";
		jdbcTemplate.update(sql1, sophieuxuat);
		
		//Xóa phiếu xuất
		String sql2 = "DELETE FROM phieuxuat WHERE SoPhieuXuat =?";
		jdbcTemplate.update(sql2, sophieuxuat);
		
		//Xử lý khách hàng
		
		phieuxuatExample ex = new phieuxuatExample();
		ex.createCriteria().andMakhEqualTo(makh);
		List<phieuxuat> list = map_phieuxuat.selectByExample(ex);
		if(list.size() > 0)
		{
			System.out.println(sophieuxuat+" exist "+list.size());
		}
		else if(list.size()<1)
		{
			String sql3 = "DELETE FROM khachhang WHERE MaKh =?";
			jdbcTemplate.update(sql3, makh);
			System.out.println("Deleted the khachhang");
		}
		ra.addFlashAttribute("success", "Xóa thành công đơn hàng "+sophieuxuat +" !");
		return "redirect:/xuatsanpham";
	}
	
}
