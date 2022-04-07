package com.nowcoder.community.entity;

/**
 * 文件类型
 */
public enum FileType {


    PNG(0,"PNG"),
    JPG(1,"JPG"),
    JPEG(2,"JPEG");

    private int code;
    private String mes;

     FileType(int code,String mes) {
         this.code = code;
         this.mes=mes;
     }
}
