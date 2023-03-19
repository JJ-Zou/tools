package com.zjj.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zjj.constants.FileInfoConstants;
import com.zjj.constants.FileInfoConstants.FileStatusEnum;
import com.zjj.entity.FileInfo;
import com.zjj.entity.FileInfoExample;
import com.zjj.mapper.FileInfoMapper;
import com.zjj.service.FileService;
import com.zjj.utils.FileUtil;
import com.zjj.utils.GenerateIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.Future;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Resource
    private FileInfoMapper fileInfoMapper;

    @Override
    public PageInfo<FileInfo> listFileInfo(int offset, int limit) {
        FileInfoExample example = new FileInfoExample();
        example.createCriteria().andStatusEqualTo(FileStatusEnum.ENABLE.getStatus());
        example.setOrderByClause("created_at desc");
        PageHelper.startPage(offset, limit);
        List<FileInfo> allFileInfos = fileInfoMapper.selectByExample(example);
        return new PageInfo<>(allFileInfos);
    }

    @Override
    public FileInfo selectByFileId(long fileId) {
        FileInfoExample example = new FileInfoExample();
        example.createCriteria().andFileIdEqualTo(fileId);
        List<FileInfo> fileInfoList = fileInfoMapper.selectByExample(example);
        if (fileInfoList == null || fileInfoList.isEmpty()) {
            return null;
        }
        return fileInfoList.get(0);
    }

    @Override
    public void deleteByFileId(long fileId) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileId(fileId);
        fileInfo.setStatus(FileStatusEnum.DELETED.getStatus());
        FileInfoExample example = new FileInfoExample();
        example.createCriteria().andFileIdEqualTo(fileId);
        int row = fileInfoMapper.updateByExampleSelective(fileInfo, example);
        if (row != 1) {
            throw new IllegalStateException("delete file error!");
        }
    }

    @Override
    public FileInfo selectWithContentByFileId(long fileId) {
        FileInfoExample example = new FileInfoExample();
        example.createCriteria().andFileIdEqualTo(fileId);
        List<FileInfo> fileInfoList = fileInfoMapper.selectByExampleWithBLOBs(example);
        if (fileInfoList == null || fileInfoList.isEmpty()) {
            throw new IllegalStateException("file not exist!");
        }
        return fileInfoList.get(0);
    }

    @Async("fileUploadPoolTaskExecutor")
    @Override
    public Future<Long> createNewFile(String filename, byte[] content, long size, String md5) {
        String filePath;
        try {
            filePath = FileUtil.getUploadFilePath(filename);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("get filePath error!");
        }
        File file = new File(filePath);
        try (FileOutputStream fileOutputStream = new FileOutputStream(file);) {
            fileOutputStream.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long fileId = GenerateIdUtil.generateId();
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileId(fileId);
        fileInfo.setFileName(filename);
        fileInfo.setType(FileInfoConstants.FileTypeEnum.FILE.getType());
        fileInfo.setContent(new String(content, StandardCharsets.ISO_8859_1));
        fileInfo.setSize(size);
        fileInfo.setStatus(FileStatusEnum.ENABLE.getStatus());
        fileInfo.setMd5(md5);
        int row = fileInfoMapper.insertSelective(fileInfo);
        if (row != 1) {
            throw new IllegalStateException("create file error!");
        }
        log.info("upload file {} success.", filename);
        return AsyncResult.forValue(fileId);
    }
}
