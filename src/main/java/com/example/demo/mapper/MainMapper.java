package com.example.demo.mapper;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.dto.DaeDto;
import com.example.demo.dto.JungDto;
import com.example.demo.dto.SoDto;

@Mapper
public interface MainMapper {
   public ArrayList<DaeDto> getDae();
   public ArrayList<JungDto> getJung(String daecode);
   public ArrayList<SoDto> getSo(String daejung);
}
