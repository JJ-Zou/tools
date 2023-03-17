package com.zjj.controller.req;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileUploadReq {

    private MultipartFile file;

}
