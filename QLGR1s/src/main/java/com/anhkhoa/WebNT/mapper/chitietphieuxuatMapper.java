package com.anhkhoa.WebNT.mapper;

import com.anhkhoa.WebNT.model.chitietphieuxuat;
import com.anhkhoa.WebNT.model.chitietphieuxuatExample;
import com.anhkhoa.WebNT.model.chitietphieuxuatKey;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface chitietphieuxuatMapper {
    
    long countByExample(chitietphieuxuatExample example);

    int deleteByExample(chitietphieuxuatExample example);

    int deleteByPrimaryKey(chitietphieuxuatKey key);

    int insert(chitietphieuxuat row);

    int insertSelective(chitietphieuxuat row);

    List<chitietphieuxuat> selectByExample(chitietphieuxuatExample example);

    chitietphieuxuat selectByPrimaryKey(chitietphieuxuatKey key);

    int updateByExampleSelective(@Param("row") chitietphieuxuat row, @Param("example") chitietphieuxuatExample example);

    int updateByExample(@Param("row") chitietphieuxuat row, @Param("example") chitietphieuxuatExample example);

    int updateByPrimaryKeySelective(chitietphieuxuat row);

    int updateByPrimaryKey(chitietphieuxuat row);
}