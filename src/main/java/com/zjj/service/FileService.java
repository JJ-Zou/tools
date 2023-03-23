package com.zjj.service;

import com.github.pagehelper.PageInfo;
import com.zjj.entity.FileInfo;

import java.util.concurrent.Future;

public interface FileService {

    PageInfo<FileInfo> listFileInfo(long parentId, int offset, int limit);

    FileInfo selectByFileId(long fileId);

    void deleteByFileId(long fileId);

    FileInfo selectWithContentByFileId(long fileId);

    Future<Long> createNewFile(String filename, byte[] content, long size, String md5, long parentId);

    Long createNewDir(String filename, long parentId);

}
