package com.zjj.mapper;

import com.zjj.entity.FileInfo;
import com.zjj.entity.FileInfoExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FileInfoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_info
     *
     * @mbg.generated
     */
    long countByExample(FileInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_info
     *
     * @mbg.generated
     */
    int deleteByExample(FileInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_info
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_info
     *
     * @mbg.generated
     */
    int insert(FileInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_info
     *
     * @mbg.generated
     */
    int insertSelective(FileInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_info
     *
     * @mbg.generated
     */
    List<FileInfo> selectByExampleWithBLOBs(FileInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_info
     *
     * @mbg.generated
     */
    List<FileInfo> selectByExample(FileInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_info
     *
     * @mbg.generated
     */
    FileInfo selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_info
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") FileInfo record, @Param("example") FileInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_info
     *
     * @mbg.generated
     */
    int updateByExampleWithBLOBs(@Param("record") FileInfo record, @Param("example") FileInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_info
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") FileInfo record, @Param("example") FileInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_info
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(FileInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_info
     *
     * @mbg.generated
     */
    int updateByPrimaryKeyWithBLOBs(FileInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_info
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(FileInfo record);
}