package cn.oalo.system.mapper;

import cn.oalo.system.entity.SysFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文件Mapper接口
 */
@Mapper
public interface SysFileMapper extends BaseMapper<SysFile> {

    /**
     * 根据文件ID查询文件信息
     *
     * @param fileId 文件ID
     * @return 文件信息
     */
    SysFile selectFileById(@Param("fileId") Long fileId);

    /**
     * 根据文件名查询文件列表
     *
     * @param fileName 文件名
     * @return 文件列表
     */
    List<SysFile> selectFilesByName(@Param("fileName") String fileName);

    /**
     * 根据文件MD5查询文件
     *
     * @param fileMd5 文件MD5
     * @return 文件信息
     */
    SysFile selectFileByMd5(@Param("fileMd5") String fileMd5);
} 