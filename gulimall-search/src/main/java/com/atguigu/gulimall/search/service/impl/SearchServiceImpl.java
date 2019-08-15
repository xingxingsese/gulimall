package com.atguigu.gulimall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.gulimall.commons.bean.Constant;
import com.atguigu.gulimall.commons.to.es.EsSkuVo;
import com.atguigu.gulimall.search.service.SearchService;
import com.atguigu.gulimall.search.vo.SearchParam;
import com.atguigu.gulimall.search.vo.SearchResponse;
import com.atguigu.gulimall.search.vo.SearchResponseAttrVo;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.ChildrenAggregation;
import io.searchbox.core.search.aggregation.MetricAggregation;
import io.searchbox.core.search.aggregation.TermsAggregation;
import org.apache.lucene.search.join.ScoreMode;
import org.apache.tomcat.util.bcel.Const;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.support.ValueType;
import org.elasticsearch.search.aggregations.support.ValuesSourceType;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author heyijieyou
 * @date 2019-08-13 23:13
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    JestClient jestClient;

    // 去es检索商品
    @Override
    public SearchResponse search(SearchParam params) {
        // 0 根据前端传来的条件生成一个dsl语句
        String query = builDSL(params); // DSL语句


        // 1 创建一个检索动作
        Search search = new Search.Builder(query)
                .addIndex(Constant.ES_SPU_INDEX)
                .addType(Constant.ES_SPU_TYPE)
                .build();

        try {
            // 2 执行检索获取到检索的数据,返回的这个就是kibana里面搜索出来的整个json大对象
            SearchResult searchResult = jestClient.execute(search);

            // 3 将检索出来的searchResult提取前端需要的数据模型,组装交给前端
            SearchResponse searchResponse = buildResult(searchResult);

            // 4 封装前端传来的页码,每页大小
            searchResponse.setPageNum(params.getPageNum());
            searchResponse.setPageSize(params.getPageSize());

            return searchResponse;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private SearchResponse buildResult(SearchResult searchResult) {
        SearchResponse response = new SearchResponse();

        // 1 从返回的searchResult抽取所有查询到的商品数据
        // 1.1 获取所有查询到的记录
        List<SearchResult.Hit<EsSkuVo, Void>> hits = searchResult.getHits(EsSkuVo.class);
        List<EsSkuVo> esSkuVos = new ArrayList<>();
        // 1.2 遍历查询到的结果
        hits.forEach(hit -> {
            EsSkuVo source = hit.source;
            Map<String, List<String>> highlight = hit.highlight;
            // 将商品的名字重新改为高亮的名字
            source.setName(highlight.get("name").get(0));
            esSkuVos.add(source);
        });
        response.setProducts(esSkuVos);

        // 2 封装分页的总记录数
        response.setTotal(searchResult.getTotal());

        // 所有的聚合结果
        MetricAggregation aggregations = searchResult.getAggregations();

        // 3 设置当前查到的结果所设计到的所有属性关系
        ChildrenAggregation attr_agg = aggregations.getChildrenAggregation("attr_agg");
        TermsAggregation attrId_agg = attr_agg.getTermsAggregation("attrId_agg");
        List<SearchResponseAttrVo> attrs = new ArrayList<>();

        // 获取到attrid_agg的buck就能知道有多少个attrid
        attrId_agg.getBuckets().forEach(bucket -> {
            // 属性的id
            Long attrId = Long.parseLong(bucket.getKey());
            TermsAggregation attrName_agg = bucket.getTermsAggregation("attrName_agg");
            TermsAggregation attrValue_agg = bucket.getTermsAggregation("attrValue_agg");

            // 属性名
            String attrName = attrName_agg.getBuckets().get(0).getKey();

            // 属性的值
            List<String> value = new ArrayList<>();
            attrValue_agg.getBuckets().forEach(attrValueBucket -> {
                String key = attrValueBucket.getKey();
                value.add(key);
            });

            // 构造一个属性对象
            SearchResponseAttrVo attrVo = new SearchResponseAttrVo();
            attrVo.setName(attrName);
            attrVo.setProductAttributeId(attrId);
            attrVo.setValue(value);
            attrs.add(attrVo);

        });

        response.setAttrs(attrs);

        // 4 设置当前查到的结果所涉及到的所有品牌关系
        SearchResponseAttrVo brand = new SearchResponseAttrVo();
        TermsAggregation brandId_agg = aggregations.getTermsAggregation("brandId_agg");

        // 遍历所有的品牌信息
        List<String> brands = new ArrayList<>();
        brandId_agg.getBuckets().forEach(item -> {
            String brandId = item.getKey();
            TermsAggregation brandName_agg = item.getTermsAggregation("brandName_agg");
            String brandName = brandName_agg.getBuckets().get(0).getKey();

            Map<String, Object> b = new HashMap<>();
            b.put("id", brandId);
            b.put("name", brandName);
            String s = JSON.toJSONString(b);
            brands.add(s);
        });
        brand.setName("品牌");
        // 所有的品牌对象详情作为json放在值里面
        brand.setValue(brands);
        response.setBrand(brand);

        // 5 设置当前查到的结果所涉及到的所有分类关系
        SearchResponseAttrVo catelog = new SearchResponseAttrVo();
        List<String> catelogs = new ArrayList<>();
        TermsAggregation catelog_agg = aggregations.getTermsAggregation("catelog_agg");
        catelog_agg.getBuckets().forEach(item -> {
            String catelogId = item.getKey();
            String catelogName = item.getTermsAggregation("catelogName_agg").getBuckets().get(0).getKey();

            Map<String, Object> b = new HashMap<>();
            b.put("id", catelogId);
            b.put("name", catelogName);
            String s = JSON.toJSONString(b);
            catelogs.add(s);
        });
        catelog.setName("分类");
        catelog.setValue(catelogs);
        response.setCatelog(catelog);

        return response;
    }

    private String builDSL(SearchParam params) {
        // 1 先获取一个SearchSourceBuilder, 辅助我们得到dsl语句
        SearchSourceBuilder builder = new SearchSourceBuilder();

        // 2 查询过滤
        BoolQueryBuilder bool = new BoolQueryBuilder();
        // 2.1 构造match条件
        if (!StringUtils.isEmpty(params.getKeyword())) {
            MatchQueryBuilder match = new MatchQueryBuilder("name", params.getKeyword());
            bool.must(match);
        }
        builder.query(bool);

        // 2.2 构造filter过滤条件
        if (params.getBrand() != null && params.getBrand().length > 0) {
            // 2.2.1 按照品牌过滤
            TermsQueryBuilder brand = new TermsQueryBuilder("brandId", params.getBrand());
            bool.filter(brand);
        }
        if (params.getCatelog3() != null && params.getCatelog3().length > 0) {
            // 2.2.2 按照分类id过滤
            TermsQueryBuilder category = new TermsQueryBuilder("productCategoryId", params.getCatelog3());
            bool.filter(category);
        }
        if (params.getPriceFrom() != null || params.getPriceTo() != null) {
            // 2.2.3 按照价格区间过滤
            RangeQueryBuilder price = new RangeQueryBuilder("price");
            if (params.getPriceFrom() != null) {
                price.gte(params.getPriceFrom()); // 大于等于
            }
            if (params.getPriceTo() != null) {
                price.lte(params.getPriceTo()); // 小于等于
            }
            bool.filter(price);
        }
        // 2.2.4 遍历所有属性的组合关系生成相应的过滤条件
        if (params.getProps() != null && params.getProps().length > 0) {
            // 按照前端传递的属性id value的对应关系进行检索
            for (String prop : params.getProps()) {
                // 遍历每一个属性 prop;
                String[] split = prop.split(":");
                if (split != null && split.length == 2) {
                    String attrId = split[0];
                    String[] attrValues = split[1].split("-");
                    // nested里面的quert条件
                    BoolQueryBuilder qb = new BoolQueryBuilder();
                    // 属性id
                    TermQueryBuilder tqbAttrId = new TermQueryBuilder("attrValueList.productAttributeId", attrId);
                    qb.must(tqbAttrId);
                    // 属性值
                    TermsQueryBuilder termsAttrValues = new TermsQueryBuilder("attrValueList.value", attrValues);
                    qb.must(termsAttrValues);

                    NestedQueryBuilder nested = new NestedQueryBuilder("attrValueList", qb, ScoreMode.None);
                    bool.filter(nested);
                }
            }
        }


        // 3 分页 页码-1 * 每页大小
        builder.from((params.getPageNum() - 1) * params.getPageSize());
        builder.size(params.getPageSize());

        // 4 高亮
        if (!StringUtils.isEmpty(params.getKeyword())) {
            // 前端传递了按关键字的查询条件
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.preTags("<b style='color:red'>")
                    .postTags("</b>")
                    .field("name");

            builder.highlighter(highlightBuilder);
        }

        // 5 排序 0 综合排序 1 销量 2 价格
        String order = params.getOrder();
        if (!StringUtils.isEmpty(order)) {
            String[] split = order.split(":"); // 分割条件
            if (split != null && split.length == 2) {
                // 解析排序规则
                SortOrder sortOrder = split[1].equalsIgnoreCase("asc") ? SortOrder.ASC : SortOrder.DESC;
                // 验证传递的参数
                if (split[0].equals("0")) {
                    builder.sort("_score", sortOrder);
                }
                if (split[0].equals("1")) {
                    builder.sort("sale", sortOrder);
                }
                if (split[0].equals("2")) {
                    builder.sort("price", sortOrder);
                }
            }
        }


        // 6 聚合
        // 属性嵌套大聚合
        NestedAggregationBuilder attraggg = new NestedAggregationBuilder("attr_agg", "attrValueList");// 聚合取个名字,和聚合路径

        // 嵌套聚合里面的子聚合
        TermsAggregationBuilder attrId_agg = new TermsAggregationBuilder("attrId_agg", ValueType.LONG);
        attrId_agg.field("attrValueList.productAttributeId");


        // 子聚合里面的子聚合
        TermsAggregationBuilder attrName_agg = new TermsAggregationBuilder("attrName_agg", ValueType.STRING);
        TermsAggregationBuilder attrValue_agg = new TermsAggregationBuilder("attrValue_agg", ValueType.STRING);

        attrName_agg.field("attrValueList.name");
        attrValue_agg.field("attrValueList.value");

        attrId_agg.subAggregation(attrName_agg);
        attrId_agg.subAggregation(attrValue_agg);

        attraggg.subAggregation(attrId_agg);
        builder.aggregation(attraggg);

        // 品牌嵌套大聚合
        // 品牌id聚合
        TermsAggregationBuilder brandAggg = new TermsAggregationBuilder("brandId_agg", ValueType.LONG);
        brandAggg.field("brandId");

        // 品牌name子聚合
        TermsAggregationBuilder brandNameAgg = new TermsAggregationBuilder("brandName_agg", ValueType.STRING);
        brandNameAgg.field("brandName");

        brandAggg.subAggregation(brandNameAgg);
        builder.aggregation(brandAggg);

        // 分类嵌套大聚合
        // 按照cid大聚合
        TermsAggregationBuilder categoryAggg = new TermsAggregationBuilder("catelog_agg", ValueType.LONG);
        categoryAggg.field("productCategoryId");

        // 按照catename子聚合
        TermsAggregationBuilder categoryNameAggg = new TermsAggregationBuilder("catelogName_agg", ValueType.STRING);
        categoryNameAggg.field("productCategoryName");

        categoryAggg.subAggregation(categoryNameAggg);
        builder.aggregation(categoryAggg);


        return builder.toString();
    }
}
