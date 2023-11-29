package com.anhkhoa.WebNT.mapper;

import com.anhkhoa.WebNT.model.khachhang;
import com.anhkhoa.WebNT.model.khachhangExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface khachhangMapper {

	long countByExample(khachhangExample example);

	int deleteByExample(khachhangExample example);

	int insert(khachhang row);

	
	int insertSelective(khachhang row);

	List<khachhang> selectByExample(khachhangExample example);

	int updateByExampleSelective(@Param("row") khachhang row, @Param("example") khachhangExample example);

	int updateByExample(@Param("row") khachhang row, @Param("example") khachhangExample example);
}