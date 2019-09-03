package com.atguigu.gmall.manage.utils;

import com.alibaba.dubbo.remoting.Client;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author liumw
 * @date 2019/9/2
 * @describe
 */
public class PmsUploadUtils {
    private static final Logger logger = LoggerFactory.getLogger(PmsUploadUtils.class);

    public static String uploadImage(MultipartFile file) {

        String file1 = PmsUploadUtils.class.getResource("/tracker.conf").getFile();
        try {
            ClientGlobal.init(file1);
        }  catch (Exception e) {
            e.printStackTrace();
            logger.error("初始化fdfs失败！");
        }

        TrackerClient client = new TrackerClient();
        TrackerServer server = null;
        try {
            server = client.getConnection();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("获取fdfs-server失败");
        }
        StorageClient storageClient = new StorageClient(server,null);
        StringBuilder sb = new StringBuilder("http://192.168.111.129");
        try {
            String[] strings = storageClient.upload_file(file.getBytes(), file.getOriginalFilename().substring(file.getOriginalFilename().indexOf(".") + 1), null);
            for (String string : strings) {
                sb.append("/").append(string);
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("上传文件失败");
        }
        return sb.toString();

    }
}
