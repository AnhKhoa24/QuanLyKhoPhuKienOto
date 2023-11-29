package com.anhkhoa.WebNT.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class thongkeController {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@GetMapping("/thongkenhapxuat")
	public String ShowView(Model model, @RequestParam(value = "page", defaultValue = "1") int trang,
			@RequestParam(value = "keyword", defaultValue = "") String timkiem) {
		model.addAttribute("keyword", timkiem);
		model.addAttribute("title", "Quản lý đơn hàng");

		String sql = "SELECT\r\n"
				+ "    bt.MaBanThanhPhan,\r\n"
				+ "    bt.TenBanThanhPhan,\r\n"
				+ "    COALESCE(SUM(combined.SoLuongNhap), 0) AS SoLuongNhap,\r\n"
				+ "    COALESCE(SUM(combined.SoLuongXuat), 0) AS SoLuongXuat\r\n"
				+ "FROM\r\n"
				+ "    banthanhphan bt\r\n"
				+ "LEFT JOIN (\r\n"
				+ "    SELECT\r\n"
				+ "        ctpn.MaBanThanhPhan,\r\n"
				+ "        0 AS SoLuongXuat,\r\n"
				+ "        SUM(SoLuong) AS SoLuongNhap\r\n"
				+ "    FROM\r\n"
				+ "        chitietphieunhap ctpn\r\n"
				+ "    GROUP BY\r\n"
				+ "        ctpn.MaBanThanhPhan\r\n"
				+ "\r\n"
				+ "    UNION ALL\r\n"
				+ "\r\n"
				+ "    SELECT\r\n"
				+ "        ctpx.MaBanThanhPhan,\r\n"
				+ "        SUM(SoLuong) AS SoLuongXuat,\r\n"
				+ "        0 AS SoLuongNhap\r\n"
				+ "    FROM\r\n"
				+ "        chitietphieuxuat ctpx\r\n"
				+ "    GROUP BY\r\n"
				+ "        ctpx.MaBanThanhPhan\r\n"
				+ ") AS combined ON bt.MaBanThanhPhan = combined.MaBanThanhPhan\r\n"
				+ "\r\n"
				+ "WHERE bt.MaBanThanhPhan = '%"+timkiem+"%' OR bt.TenBanThanhPhan LIKE '%"+timkiem+"%'\r\n"
				+ "GROUP BY\r\n"
				+ "    bt.MaBanThanhPhan, bt.TenBanThanhPhan ";

		/* System.out.println(sql); */
		List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
		Integer sotimduoc = results.size();
		Integer sospmoitrang = 6;
		Integer sotrang = (int) Math.ceil((double) sotimduoc / sospmoitrang);
		Integer next = sospmoitrang * (trang - 1);
		String query = sql + " LIMIT " + sospmoitrang + " OFFSET " + next;
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		if (result.size() < 1) {
			sotrang = 1;
			model.addAttribute("notfound", "Không tìm thông tin sản phẩm thống kê");
		}
		model.addAttribute("model", result);
		model.addAttribute("sotrang", sotrang);
		if (trang == 1) {
			trang += 1;
			model.addAttribute("trang", trang);
		}
		model.addAttribute("trang", trang);

		return "thongkenhapxuat";
	}
	
	@GetMapping("/thongkedoanhthu")
	public String TKDT(Model model,
			@RequestParam(name = "begin", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date begin,
			@RequestParam(name = "end", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date end) {
		
	   //jjjjjjjjjjjjjjjjjjjjjjj
		
		LocalDate ketthuc = LocalDate.now();
		LocalDate batdau = ketthuc.minus(15, ChronoUnit.DAYS);
		if (begin != null && end != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String formattedBegin = dateFormat.format(begin);
			String formattedEnd = dateFormat.format(end);
			model.addAttribute("begin", formattedBegin);
			model.addAttribute("end", formattedEnd);
			batdau = LocalDate.parse(formattedBegin);
			ketthuc = LocalDate.parse(formattedEnd);
		}
		
		
		
		Integer khoangNgay = (int)ChronoUnit.DAYS.between(batdau, ketthuc);   
        if(khoangNgay < 0)
        {
        	return "redirect:/";
        }
        String nhap = "SELECT \r\n"
        		+ "  phieunhap.NgayPhieuNhap, SUM(chitietphieunhap.SoLuong * chitietphieunhap.GiaNhap) AS TongNhap\r\n"
        		+ "FROM chitietphieunhap INNER JOIN phieunhap ON chitietphieunhap.SoPhieuNhap = phieunhap.SoPhieuNhap\r\n"
        		+ "\r\n"
        		+ "WHERE phieunhap.NgayPhieuNhap BETWEEN '"+batdau+"' AND '"+ketthuc+"'\r\n"
        		+ "GROUP BY (phieunhap.NgayPhieuNhap)\r\n";
        List<Map<String, Object>> dsnhap = jdbcTemplate.queryForList(nhap);
       
        String xuat ="SELECT \r\n"
        		+ "	phieuxuat.NgayPhieuXuat, SUM(banthanhphan.GiaBan* chitietphieuxuat.SoLuong) AS TongBan\r\n"
        		+ "FROM banthanhphan JOIN chitietphieuxuat ON banthanhphan.MaBanThanhPhan = chitietphieuxuat.MaBanThanhPhan\r\n"
        		+ "\r\n"
        		+ "JOIN phieuxuat ON chitietphieuxuat.SoPhieuXuat = phieuxuat.SoPhieuXuat\r\n"
        		+ "\r\n"
        		+ "WHERE phieuxuat.NgayPhieuXuat BETWEEN '"+batdau+"' AND '"+ketthuc+"'\r\n"
        		+ "\r\n"
        		+ "GROUP BY phieuxuat.NgayPhieuXuat";
        
        List<Map<String, Object>> dsxuat = jdbcTemplate.queryForList(xuat);
      
		/*
		 * System.out.println(dsnhap.size()); System.out.println(dsxuat.size());
		 * System.out.println(xuat);
		 */     
        
        LocalDate arr1[] = new LocalDate[khoangNgay+1];
        LocalDate temp = batdau;
        Integer nhapvao[] = new Integer[khoangNgay+1];
        Integer xuatra[] = new Integer[khoangNgay+1];
        for(int i = 0; i <= khoangNgay; i ++)
        {
        	arr1[i] = temp;
        	temp = temp.plusDays(1);
        	nhapvao[i] = 0;
        	xuatra[i] = 0;
        }
       
        
        for (Map<String, Object> row : dsxuat) {
            
            Object ngayValue = row.get("NgayPhieuXuat");
            Object takeXuat = row.get("TongBan");
        	Integer ganXuat = Integer.valueOf(takeXuat.toString());
            String test = ngayValue.toString();
            LocalDate kt = LocalDate.parse(test);
          
            
            for(int i = 0; i <= khoangNgay; i ++)
            {
            	if(kt.isEqual(arr1[i]))
            	{
            		xuatra[i] = ganXuat ;
            	}
            }
            
        }
        for (Map<String, Object> row : dsnhap) {
        	
        	Object ngayValue = row.get("NgayPhieuNhap");
        	Object takeNhap = row.get("TongNhap");
        	Integer ganNhap = Integer.valueOf(takeNhap.toString());
        	String test = ngayValue.toString();
        	LocalDate kt = LocalDate.parse(test);
        	
        	for(int i = 0; i <= khoangNgay; i ++)
        	{
        		if(kt.isEqual(arr1[i]))
        		{
        			nhapvao[i] = ganNhap;
        		}
        	}
        	
        }
              
     // Định dạng kiểu "dd-MM"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM");

        // Mảng mới chứa các phần tử đã được định dạng lại
        String[] formattedArr = new String[arr1.length];
        for (int i = 0; i < arr1.length; i++) {
            formattedArr[i] = arr1[i].format(formatter);
        }
        model.addAttribute("dsngay", formattedArr);
        model.addAttribute("xuatra", xuatra);
        model.addAttribute("nhapvao", nhapvao);
        
      //Xử lý mốc trục tung
       Integer maxtrucY_1 = 100000;
       Integer maxtrucY_2 = 100000;
       String sqlxuatmax ="SELECT \r\n"
       		+ "	phieuxuat.NgayPhieuXuat, SUM(banthanhphan.GiaBan* chitietphieuxuat.SoLuong) AS TongBan\r\n"
       		+ "FROM banthanhphan JOIN chitietphieuxuat ON banthanhphan.MaBanThanhPhan = chitietphieuxuat.MaBanThanhPhan\r\n"
       		+ "\r\n"
       		+ "JOIN phieuxuat ON chitietphieuxuat.SoPhieuXuat = phieuxuat.SoPhieuXuat\r\n"
       		+ "\r\n"
       		+ "WHERE phieuxuat.NgayPhieuXuat BETWEEN '"+batdau+"' AND '"+ketthuc+"'\r\n"
       		+ "\r\n"
       		+ "GROUP BY phieuxuat.NgayPhieuXuat\r\n"
       		+ "\r\n"
       		+ "ORDER BY TongBan DESC\r\n"
       		+ "\r\n"
       		+ "LIMIT 1";
       List<Map<String, Object>> dsxuatmax = jdbcTemplate.queryForList(sqlxuatmax);
       if(dsxuatmax.size()>0)
       {
    	   Map<String, Object> takemaxxuat = dsxuatmax.get(0);
           Object takeField = takemaxxuat.get("TongBan");
           maxtrucY_1 = Integer.valueOf(takeField.toString());
       }
       
       String sqlnhapmax ="SELECT \r\n"
       		+ "  phieunhap.NgayPhieuNhap, SUM(chitietphieunhap.SoLuong * chitietphieunhap.GiaNhap) AS TongNhap\r\n"
       		+ "FROM chitietphieunhap INNER JOIN phieunhap ON chitietphieunhap.SoPhieuNhap = phieunhap.SoPhieuNhap\r\n"
       		+ "\r\n"
       		+ "WHERE phieunhap.NgayPhieuNhap BETWEEN '"+batdau+"' AND '"+ketthuc+"'\r\n"
       		+ "GROUP BY (phieunhap.NgayPhieuNhap)\r\n"
       		+ "ORDER BY TongNhap DESC\r\n"
       		+ "LIMIT 1";
       List<Map<String, Object>> dsnhapmax = jdbcTemplate.queryForList(sqlnhapmax);
       if(dsnhapmax.size() >0) 
       {
    	   Map<String, Object> takemaxnhap = dsnhapmax.get(0);
           Object takeField1 = takemaxnhap.get("TongNhap");
           maxtrucY_2 = Integer.valueOf(takeField1.toString());
       }
    	   
       
       
       
       Integer maxtrucY = maxtrucY_1>maxtrucY_2? maxtrucY_1:maxtrucY_2;
      
       model.addAttribute("maxY", maxtrucY);
       
       //Tổng tiền nhập vào
       Integer TongTienNhap =0;
       Integer TongTienXuat = 0;
       for(int i =0; i < nhapvao.length; i++)
       {
    	   TongTienNhap += nhapvao[i];
    	   TongTienXuat += xuatra[i];
       }
       
       model.addAttribute("tongtiennhap", TongTienNhap); 
       model.addAttribute("tongtienxuat", TongTienXuat); 
       model.addAttribute("loinhuan", TongTienXuat-TongTienNhap); 
     
       
       
       
		return "thongkedoanhthu";
	}

}
