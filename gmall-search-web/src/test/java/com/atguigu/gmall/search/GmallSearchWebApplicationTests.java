package com.atguigu.gmall.search;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallSearchWebApplicationTests {

    @Test
    public void contextLoads() {

        List<List> listList = new ArrayList<>();
        listList.add(Arrays.asList("1", "2", "3", "6", "9"));
        listList.add(Arrays.asList("zhangsan", "lisi", "wangwu", "liuliu", "waaa"));
        listList.add(Arrays.asList("121", "3123", "4341", "222", "3333"));
        List<Object> collect = listList.stream().flatMap(Collection::stream).distinct().collect(Collectors.toList());
        System.out.println(collect);
    }

}
