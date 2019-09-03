package com.atguigu.gmall.manage;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallManageWebApplicationTests {

    @Test
    public void contextLoads() throws IOException, MyException {
        //加载配置文件
        String file = this.getClass().getResource("/tracker.conf").getFile();
        ClientGlobal.init(file);
        TrackerClient client = new TrackerClient();
        TrackerServer server = client.getConnection();
        StorageClient storageClient = new StorageClient(server, null);
        String[] results = storageClient.upload_file("D:\\2019-02\\494f14957d2596414c1e734c7a715ca5.jpg", "jpg", null);
        for (String result : results) {
            System.out.println(result);
        }

    }

}
