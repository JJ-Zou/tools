package com.zjj.controller.req;

import lombok.Data;
import org.springframework.web.bind.annotation.ModelAttribute;

@Data
public class CreateFileReq {
    private long parentId = 0;

    private String filename;
}
