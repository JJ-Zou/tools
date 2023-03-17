package com.zjj.controller;

import com.github.pagehelper.PageInfo;
import com.zjj.entity.FileInfo;
import com.zjj.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/file")
public class FileServerController {

    @Autowired
    private FileService fileService;

    @GetMapping("/list")
    public String listFileInfos(@RequestParam(name = "offset", defaultValue = "1") int offset,
                                @RequestParam(name = "limit", defaultValue = "10") int limit,
                                Model model) {
        PageInfo<FileInfo> pageInfo = fileService.listFileInfo(offset, limit);
        model.addAttribute("file_list", pageInfo);
        return "list";
    }

    @GetMapping("/page")
    public ResponseEntity<?> pageFileInfos(@RequestParam(name = "offset", defaultValue = "1") int offset,
                                           @RequestParam(name = "limit", defaultValue = "10") int limit) {
        PageInfo<FileInfo> pageInfo = fileService.listFileInfo(offset, limit);
        return ResponseEntity.ok(pageInfo);
    }

    @GetMapping
    public ResponseEntity<?> getFileInfo(@RequestParam(name = "file_id") long fileId) {
        FileInfo fileInfo = fileService.selectByFileId(fileId);
        if (fileInfo == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(fileInfo);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile uploadFile) {
        String filename = uploadFile.getOriginalFilename();
        byte[] content;
        try {
            content = uploadFile.getBytes();
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
        long size = uploadFile.getSize();
        String type = "file";
        String md5 = DigestUtils.md5DigestAsHex(content);
        Long fileId = fileService.createNewFile(filename, type, content, size, md5);
        return ResponseEntity.ok(fileId);
    }

    @RequestMapping("/download")
    public ResponseEntity<?> downloadFile(@RequestParam(name = "file_id") long fileId) {
        FileInfo fileInfo = fileService.selectWithContentByFileId(fileId);
        if (fileInfo == null) {
            return ResponseEntity.badRequest().build();
        }
        String filename = fileInfo.getFileName();
        long size = fileInfo.getSize();
        String content = fileInfo.getContent();
        //设置响应头
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", filename));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(size)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(new ByteArrayInputStream(content.getBytes(StandardCharsets.ISO_8859_1))));
    }

}
