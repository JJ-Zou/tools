package com.zjj.service;

import com.github.pagehelper.PageInfo;
import com.zjj.entity.FileInfo;

public interface FileService {

    PageInfo<FileInfo> listFileInfo(int offset, int limit);

    FileInfo selectByFileId(long fileId);

    FileInfo selectWithContentByFileId(long fileId);

    Long createNewFile(String filename, String type, byte[] content, long size, String md5);

}
