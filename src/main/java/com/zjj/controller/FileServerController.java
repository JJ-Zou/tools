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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Controller
@RequestMapping("/file")
public class FileServerController {

    @Autowired
    private FileService fileService;

    @GetMapping("/list")
    public String listFileInfos(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                                @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
                                Model model) {
        PageInfo<FileInfo> pageInfo = fileService.listFileInfo(pageNum, pageSize);
        model.addAttribute("file_list", pageInfo);
        return "list";
    }

    @GetMapping("/page")
    public ResponseEntity<?> pageFileInfos(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                                           @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
        PageInfo<FileInfo> pageInfo = fileService.listFileInfo(pageNum, pageSize);
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

    @DeleteMapping
    public ResponseEntity<?> deleteFile(@RequestParam(name = "file_id") long fileId) {
        fileService.deleteByFileId(fileId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile uploadFile,
                                        @RequestParam(value = "sync", defaultValue = "False") boolean sync) throws ExecutionException, InterruptedException {
        String filename = uploadFile.getOriginalFilename();
        byte[] content;
        try {
            content = uploadFile.getBytes();
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
        long size = uploadFile.getSize();
        String md5 = DigestUtils.md5DigestAsHex(content);
        Future<Long> fileIdFuture = fileService.createNewFile(filename, content, size, md5);
        if (sync) {
            Long fileId = fileIdFuture.get();
            return ResponseEntity.ok(fileId);
        }
        return ResponseEntity.ok().build();
    }

    @RequestMapping("/download")
    public ResponseEntity<?> downloadFile(@RequestParam(name = "file_id") long fileId) throws IOException {
        FileInfo fileInfo = fileService.selectByFileId(fileId);
        if (fileInfo == null) {
            return ResponseEntity.badRequest().build();
        }
        String filename = fileInfo.getFileName();
        String md5 = fileInfo.getMd5();
        long size = fileInfo.getSize();
        String path = fileInfo.getPath();
        Path filePath = Paths.get(path);
        HttpHeaders headers = new HttpHeaders();
        String encodeFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8);
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition",
                String.format("attachment; filename=%s;filename*=utf-8''%s", encodeFilename, encodeFilename));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(size)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(Files.newInputStream(filePath)));
    }

}
