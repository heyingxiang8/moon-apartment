package com.atguigu.lease.web.admin.service.impl;

import com.atguigu.lease.common.minio.MinioProperties;
import com.atguigu.lease.web.admin.service.FileService;
import io.minio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private MinioProperties properties;

    @Autowired
    private MinioClient client;

    @Override
    public String upload(MultipartFile file) {
        try {
            boolean bucketExists = client.bucketExists(BucketExistsArgs.builder().bucket(properties.getBucketName()).build());
            //判断桶是否存在，不存在就创建
            if(!bucketExists){
                client.makeBucket(MakeBucketArgs.builder().bucket(properties.getBucketName()).build());
                client.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(properties.getBucketName()).
                        config(createBucketPolicyConfig(properties.getBucketName())).build());
            }
            String fileName = new SimpleDateFormat("yyyyMMdd").format(new Date()) + "/" +
                    UUID.randomUUID() + "-" + file.getOriginalFilename();
            /*
            * 在 .stream(...) 方法中，第三个参数 -1 代表 分片上传的阈值。
            方法签名通常如下：
                stream(InputStream stream, long size, long partSize)
                第一个参数：数据流。
                第二个参数：对象总大小（MinIO 必须预先知道大小，除非使用 -1 的特殊流处理，但这里传入了具体大小）。
                第三个参数（即 -1）：分片大小。
            当设置为 -1 时：
                含义：表示使用 SDK 的 默认分片策略 或 禁用自定义分片（取决于具体版本，但在大多数现代 MinIO SDK 中，-1 通常意味着让 SDK 自动决定，或者如果文件大小小于默认阈值则不分片）。
                实际效果：MinIO SDK 内部通常有一个默认的分片大小（例如 5MB 或 16MB）。
                如果 file.getSize() 小于这个默认阈值，文件将作为 单个对象 直接上传。
                如果 file.getSize() 大于这个默认阈值，SDK 会自动将文件切分成多个块进行 分片上传，最后在服务端合并。
            * */
            client.putObject(PutObjectArgs.builder().
                                bucket(properties.getBucketName()).
                                object(fileName).
                                stream(file.getInputStream(), file.getSize(), -1).
                                contentType(file.getContentType()).build());
            return String.join("/",properties.getEndpoint(),properties.getBucketName(),fileName);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String createBucketPolicyConfig(String bucketName) {

        //"Action": "s3:GetObject"：允许执行下载/获取对象的操作。
        //"Effect": "Allow"：表示允许上述操作。
        //"Principal": "*"：表示允许任何人（匿名用户）访问，即公开读取。
        //"Resource": "arn:aws:s3:::%s/*"：这是关键点。%s 是一个占位符，代表存储桶名称。
        //.formatted(bucketName) 是 Java 15+ 引入的字符串新方法。它的作用类似于 String.format()，它会将文本块中的 %s 替换为传入的 bucketName 参数。
        return """
            {
              "Statement" : [ {
                "Action" : "s3:GetObject",
                "Effect" : "Allow",
                "Principal" : "*",
                "Resource" : "arn:aws:s3:::%s/*"
              } ],
              "Version" : "2012-10-17"
            }
            """.formatted(bucketName);
        /*
        * “三个引号”（实际上是三个双引号）是 Java 15 正式引入的 文本块 特性。
        * 在 Java 15 之前，如果要写一段多行的 JSON 字符串，代码会非常难看且难以维护：
        * String json = "{\n" +
              "  \"Statement\": [\n" +
              "    {\n" +
              "      \"Action\": \"s3:GetObject\",\n" +
              "      \"Resource\": \"arn:aws:s3:::" + bucketName + "/*\"\n" +
              "    }\n" +
              "  ]\n" +
              "}";
           使用三个双引号 """ 有以下巨大优势：
            多行支持：可以直接换行，代码结构清晰，所见即所得。
            无需转义：在 JSON 中常用的双引号 " 可以直接写，不需要写成 \"。
            自动格式化：编译器会自动处理缩进和换行符，保持代码整洁。
        * */
    }
}
