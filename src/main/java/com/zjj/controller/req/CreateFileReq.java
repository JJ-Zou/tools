package com.zjj.controller.req;

import lombok.Data;

@Data
public class CreateFileReq {
    private long parentId = 0;

    private String filename;
}
