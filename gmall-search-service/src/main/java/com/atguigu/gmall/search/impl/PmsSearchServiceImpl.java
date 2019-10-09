package com.atguigu.gmall.search.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.PmsSearchParam;
import com.atguigu.gmall.bean.PmsSearchSkuInfo;
import com.atguigu.gmall.bean.PmsSkuAttrValue;
import com.atguigu.gmall.service.PmsSearchService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liumw
 * @date 2019/9/29
 * @describe
 */
@Service
public class PmsSearchServiceImpl implements PmsSearchService {

    @Autowired
    private JestClient jestClient;

    @Override
    public List<PmsSearchSkuInfo> searchPmsSkuInfoList(PmsSearchParam searchParam) {

        String queryDsl = getDslStr(searchParam);

        Search search = new Search.Builder(queryDsl).addIndex("gmall").addType("skuInfo").build();
        try {
            SearchResult execute = jestClient.execute(search);
            List<PmsSearchSkuInfo> collect = new ArrayList<>();
            for (SearchResult.Hit<PmsSearchSkuInfo, Void> hit : execute.getHits(PmsSearchSkuInfo.class)) {
                if(hit.highlight != null && hit.highlight.get("skuName") != null && !hit.highlight.get("skuName").isEmpty()){
                    hit.source.setSkuName(hit.highlight.get("skuName").get(0));
                }
                collect.add(hit.source);
            }
            return collect;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getDslStr(PmsSearchParam searchParam) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
        String[] valueIds = searchParam.getValueId();
        if (valueIds != null && valueIds.length > 0){
            for (int i = 0; i < valueIds.length; i++) {
                TermQueryBuilder termQueryBuilder = new TermQueryBuilder("skuAttrValueList.valueId",valueIds[i]);
                queryBuilder.filter(termQueryBuilder);
            }
        }

        if (searchParam.getCatalog3Id() != null){
            TermQueryBuilder termQueryBuilder = new TermQueryBuilder("catalog3Id",searchParam.getCatalog3Id());
            queryBuilder.filter(termQueryBuilder);
        }

        if (StringUtils.isNotBlank(searchParam.getKeyword())){
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("skuName",searchParam.getKeyword());
            MatchQueryBuilder matchQueryBuilder1 = new MatchQueryBuilder("skuDesc",searchParam.getKeyword());
            queryBuilder.must(matchQueryBuilder);
            queryBuilder.must(matchQueryBuilder1);
        }
        //高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<span style='color:red;'>");
        highlightBuilder.field("skuName");
        highlightBuilder.postTags("</span>");
        sourceBuilder.highlight(highlightBuilder);
        sourceBuilder.query(queryBuilder);
        sourceBuilder.sort("id", SortOrder.DESC);
        return sourceBuilder.toString();
    }
}
