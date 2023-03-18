package com.zjj.constants;

public class FileInfoConstants {

    public enum FileTypeEnum {
        FILE("file", "文件"),
        DIR("dir", "文件夹");

        private final String type;

        private final String comment;

        FileTypeEnum(String type, String comment) {
            this.type = type;
            this.comment = comment;
        }

        public String getType() {
            return type;
        }

        public String getComment() {
            return comment;
        }
    }

    public enum FileStatusEnum {
        ENABLE(1, "可使用"),
        DELETED(-1, "被删除");

        private final int status;

        private final String comment;

        FileStatusEnum(int status, String comment) {
            this.status = status;
            this.comment = comment;
        }

        public int getStatus() {
            return status;
        }

        public String getComment() {
            return comment;
        }
    }

}
