package com.anhkhoa.WebNT.mapper;

import com.anhkhoa.WebNT.model.phieuxuat;
import com.anhkhoa.WebNT.model.phieuxuatExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface phieuxuatMapper {
  
    long countByExample(phieuxuatExample example);

    int deleteByExample(phieuxuatExample example);

    int deleteByPrimaryKey(Integer sophieuxuat);

    int insert(phieuxuat row);

    int insertSelective(phieuxuat row);

    List<phieuxuat> selectByExample(phieuxuatExample example);

    phieuxuat selectByPrimaryKey(Integer sophieuxuat);

    int updateByExampleSelective(@Param("row") phieuxuat row, @Param("example") phieuxuatExample example);

    int updateByExample(@Param("row") phieuxuat row, @Param("example") phieuxuatExample example);

    int updateByPrimaryKeySelective(phieuxuat row);

   
    int updateByPrimaryKey(phieuxuat row);
}