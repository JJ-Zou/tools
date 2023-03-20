package com.zjj.controller.req;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateFileReq {
    private long parentId;

    private String filename;

    private String type;

    private boolean sync;

}
