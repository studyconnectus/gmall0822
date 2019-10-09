package com.atguigu.gmall.search;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.PmsSearchSkuInfo;
import com.atguigu.gmall.bean.PmsSkuInfo;
import com.atguigu.gmall.service.SkuService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.elasticsearch.jest.JestAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallSearchServiceApplicationTests {

    @Reference
    SkuService skuService;// 查询mysql


    @Autowired
    JestClient jestClient;

    @Test
    public void contextLoads() throws IOException {
        put();
    }

    public void put() throws IOException {

        // 查询mysql数据
        List<PmsSkuInfo> pmsSkuInfoList = new ArrayList<>();

        pmsSkuInfoList = skuService.getSkuInfoList(285l);

        // 转化为es的数据结构
        List<PmsSearchSkuInfo> pmsSearchSkuInfos = new ArrayList<>();

        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfoList) {
            PmsSearchSkuInfo pmsSearchSkuInfo = new PmsSearchSkuInfo();

            BeanUtils.copyProperties(pmsSkuInfo,pmsSearchSkuInfo);

            pmsSearchSkuInfo.setId(pmsSkuInfo.getId());

            pmsSearchSkuInfos.add(pmsSearchSkuInfo);

        }

        // 导入es
        for (PmsSearchSkuInfo pmsSearchSkuInfo : pmsSearchSkuInfos) {
            Index put = new Index.Builder(pmsSearchSkuInfo).index("gmall").type("skuInfo").id(pmsSearchSkuInfo.getId()+"").build();
            jestClient.execute(put);
        }

    }

    public void get() throws IOException {

        Search search = new Search.Builder("{\n" +
                "  \"query\": {\n" +
                "    \"bool\": {\n" +
                "      \"filter\": [{\n" +
                "        \"term\":{\n" +
                "          \"skuAttrValueList.valueId\" : \"39\"\n" +
                "        }\n" +
                "      },{\n" +
                "        \"term\":{\n" +
                "          \"skuAttrValueList.valueId\" : \"43\"\n" +
                "        }\n" +
                "      }],\n" +
                "      \"must\": [\n" +
                "        {\n" +
                "          \"match\": {\n" +
                "            \"skuName\": \"小米\"\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  }\n" +
                "}").addIndex("gmall").addType("SkuInfo").build();
        SearchResult execute = jestClient.execute(search);
        System.out.println(execute.getHits(PmsSearchSkuInfo.class).size());
    }

    public void get2() throws IOException {

        SearchSourceBuilder builder = new SearchSourceBuilder();
        //bool
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
            //filter
                //term
            //must
                //match
        TermQueryBuilder termQueryBuilder = new TermQueryBuilder("skuAttrValueList.valueId","39");
        TermQueryBuilder termQueryBuilder1 = new TermQueryBuilder("skuAttrValueList.valueId","43");
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("skuName","华为");
        boolQueryBuilder.filter(termQueryBuilder).filter(termQueryBuilder1).must(matchQueryBuilder);

        builder.query(boolQueryBuilder);
        //from
        //to
        //highlight
        Search search = new Search.Builder(builder.toString()).addIndex("gmall").addType("SkuInfo").build();
        SearchResult execute = jestClient.execute(search);
        System.out.println(execute.getTotal());
    }



}
