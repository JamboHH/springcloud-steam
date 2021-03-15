package com.ah.service.Impl;

import com.ah.common.BaseResp;
import com.ah.pojo.Game;
import com.ah.service.GaSearchService;
import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GaSearchServiceImpl implements GaSearchService {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public BaseResp selectKey(Map map) {
        BaseResp baseResp = new BaseResp();
        String key = map.get("key").toString();
        Integer page = (Integer) map.get("page");
        Integer size = (Integer) map.get("size");
        String appraisalSort = null;
        String priceSort = null;
        if (!StringUtils.isEmpty(map.get("appraisalSort"))) {
            appraisalSort = map.get("appraisalSort").toString();
            System.out.println("appraisalSort = " + appraisalSort);
        }
        if (!StringUtils.isEmpty(map.get("priceSort"))) {
            priceSort = map.get("priceSort").toString();
            System.out.println("priceSort = " + priceSort);
        }
        SearchRequest searchRequest = new SearchRequest("game");
        searchRequest.types("doc");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        if (StringUtils.isEmpty(key)) {
            searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        } else {
            searchSourceBuilder.query(QueryBuilders.multiMatchQuery(key, "gname", "galias"));
        }
        //设置高亮字段
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("gname");
        highlightBuilder.field("galias");
        highlightBuilder.preTags("<font style='color:red'>");
        highlightBuilder.postTags("</font>");
        searchSourceBuilder.highlighter(highlightBuilder);
        if (!StringUtils.isEmpty(appraisalSort) && appraisalSort.equals("asc")) {
            searchSourceBuilder.sort("gappraisal", SortOrder.ASC);
        }
        if (!StringUtils.isEmpty(appraisalSort) && appraisalSort.equals("desc")) {
            searchSourceBuilder.sort("gappraisal", SortOrder.DESC);
        }
        if (!StringUtils.isEmpty(priceSort) && priceSort.equals("asc")) {
            searchSourceBuilder.sort("gprice", SortOrder.ASC);
        }
        if (!StringUtils.isEmpty(priceSort) && priceSort.equals("desc")) {
            searchSourceBuilder.sort("gprice", SortOrder.DESC);
        }
        //分页
        searchSourceBuilder.from((page - 1) * size);
        searchSourceBuilder.size(size);
        searchRequest.source(searchSourceBuilder);
        //执行请求
        try {
            SearchResponse search = restHighLevelClient.search(searchRequest);
            //解析结果
            SearchHits hits = search.getHits();
            baseResp.setTotal(hits.getTotalHits());
            SearchHit[] hits1 = hits.getHits();
            List list = new ArrayList<>();
            for (SearchHit hi : hits1) {
                Map<String, HighlightField> highlightFields = hi.getHighlightFields();
                String gamename = null;
                String gamealias = null;
                if (highlightFields != null) {
                    HighlightField gameName = highlightFields.get("gname");
                    HighlightField gameAlias = highlightFields.get("galias");
                    if (gameName != null) {
                        Text[] fragments = gameName.getFragments();
                        StringBuffer stringBuffer = new StringBuffer();
                        if (fragments != null) {
                            for (Text te : fragments) {
                                gamename = stringBuffer.append(te).toString();
                            }
                        }
                    }
                    if (gameAlias != null) {
                        Text[] fragments = gameAlias.getFragments();
                        StringBuffer stringBuffer = new StringBuffer();
                        if (fragments != null) {
                            for (Text te : fragments) {
                                gamealias = stringBuffer.append(te).toString();
                            }
                        }
                    }
                }
                Map<String, Object> sourceAsMap = hi.getSourceAsMap();
                if (gamename != null) {
                    sourceAsMap.put("gaame", gamename);
                }
                if (gamealias != null) {
                    sourceAsMap.put("galias", gamealias);
                }
                Game game = JSONObject.parseObject(JSONObject.toJSON(sourceAsMap).toString(), Game.class);
                list.add(game);
            }
            baseResp.setCode(200);
            baseResp.setData(list);
            baseResp.setMessage("搜索成功");
            return baseResp;
        } catch (IOException e) {
            baseResp.setMessage("搜索失败");
            baseResp.setCode(201);
            return baseResp;
        }
    }
}
