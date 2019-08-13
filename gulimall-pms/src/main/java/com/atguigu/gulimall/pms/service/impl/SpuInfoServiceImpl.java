package com.atguigu.gulimall.pms.service.impl;

import com.atguigu.gulimall.commons.bean.Resp;
import com.atguigu.gulimall.commons.to.SkuSaleInfoTo;
import com.atguigu.gulimall.commons.to.SkuStockVo;
import com.atguigu.gulimall.commons.to.es.EsSkuAttributeValue;
import com.atguigu.gulimall.commons.utils.AppUtils;
import com.atguigu.gulimall.pms.dao.*;
import com.atguigu.gulimall.pms.entity.*;
import com.atguigu.gulimall.pms.feign.EsFeignService;
import com.atguigu.gulimall.pms.feign.SmsSkuSalelnfoFeignService;
import com.atguigu.gulimall.pms.feign.WmsFeignService;
import com.atguigu.gulimall.pms.vo.BaseAttrsVO;
import com.atguigu.gulimall.pms.vo.SaleAttrVo;
import com.atguigu.gulimall.pms.vo.SkuVO;
import com.atguigu.gulimall.pms.vo.SpuAllSaveVo;
import com.atguigu.gulimall.commons.to.es.EsSkuVo;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gulimall.commons.bean.PageVo;
import com.atguigu.gulimall.commons.bean.Query;
import com.atguigu.gulimall.commons.bean.QueryCondition;

import com.atguigu.gulimall.pms.service.SpuInfoService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    SpuInfoDao spuInfoDao;

    @Autowired
    SpuInfoDescDao spuInfoDescDao;

    @Autowired
    ProductAttrValueDao spuAttrValueDao;

    @Autowired
    SkuInfoDao skuInfoDao;

    @Autowired
    SkuImagesDao skuImagesDao;

    @Autowired
    AttrDao attrDao;

    @Autowired
    SkuSaleAttrValueDao skuSaleAttrValueDao;

    @Autowired
    SmsSkuSalelnfoFeignService smsSkuSalelnfoFeignService;

    @Autowired
    EsFeignService esFeignService;

    @Autowired
    BrandDao brandDao;

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    WmsFeignService wmsFeignService;

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageVo(page);
    }


    // 根据分类查出分页信息
    @Override
    public PageVo queryPageByCatId(QueryCondition queryCondition, Long catId) {
        // 1 封装查询条件
        QueryWrapper<SpuInfoEntity> queryWrapper = new QueryWrapper<>();
        if (catId != 0) {
            // 查全站
            queryWrapper.eq("catalog_id", catId);
            if (!StringUtils.isEmpty(queryCondition.getKey())) {
                queryWrapper.and(obj -> {
                    obj.like("spu_name", queryCondition.getKey());
                    obj.or().like("id", queryCondition.getKey());
                    return obj;
                });
            }
        }
        // 2 封装翻页条件
        IPage<SpuInfoEntity> page = new Query<SpuInfoEntity>().getPage(queryCondition);

        // 3 去数据库查询
        IPage<SpuInfoEntity> data = this.page(page, queryWrapper);

        //PageVo pageVo = new PageVo(data.getRecords(),data.getTotal(),data.getSize(),data.getCurrent());

        PageVo pageVo = new PageVo(data);

        return pageVo;
    }

    // 大保存
//  @Transactional(rollbackFor = {Exception.class})
    @GlobalTransactional
    @Override
    public void spuBigSaveAll(SpuAllSaveVo spuInfo) {

        // 获取代理对象
        SpuInfoService proxy = (SpuInfoService) AopContext.currentProxy();

        // 1.1 保存spu基本信息,返回id
        Long spuId = this.saveSpuBaseInfo(spuInfo);
        // 1.2 保存spu的所有图片信息
        proxy.saveSpuInfoImages(spuId, spuInfo.getSpuImages());

        // 2 保存spu的基本属性信息
        List<BaseAttrsVO> baseAttrs = spuInfo.getBaseAttrs();
        proxy.saveSpuBaseAttrs(spuId, baseAttrs);

        // 3 保存sku以及sku的营销相关信息
        proxy.saveSkuinfos(spuId, spuInfo.getSkus());



    }

    // 保存spu基本信息
    @Transactional // 开启事务
    @Override
    public Long saveSpuBaseInfo(SpuAllSaveVo spuInfo) {
        //  保存spu的基本信息
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();

        BeanUtils.copyProperties(spuInfo, spuInfoEntity);
        // 创建时间
        spuInfoEntity.setCreateTime(new Date());
        // 更新时间
        spuInfoEntity.setUodateTime(new Date());
        // 保存到数据库
        spuInfoDao.insert(spuInfoEntity);

        return spuInfoEntity.getId();
    }

    // 保存spu的图片信息
    @Transactional // 开启事务
    @Override
    public void saveSpuInfoImages(Long spuId, String[] spuImages) {
        StringBuffer urls = new StringBuffer();
        for (String image : spuImages) {
            // 多个图片地址中间都,号分割
            urls.append(image).append(",");
        }
        SpuInfoDescEntity entity = new SpuInfoDescEntity();
        entity.setSpuId(spuId);
        entity.setDecript(urls.toString());
        spuInfoDescDao.insertInfo(entity);
    }

    // 保存spu的基本属性信息
    @Transactional // 开启事务
    @Override
    public void saveSpuBaseAttrs(Long spuId, List<BaseAttrsVO> baseAttrs) {
        List<ProductAttrValueEntity> allSave = new ArrayList<>();
        if (baseAttrs != null && baseAttrs.size() > 0) {


            for (BaseAttrsVO baseAttr : baseAttrs) {

                ProductAttrValueEntity entity = new ProductAttrValueEntity();
                entity.setAttrId(baseAttr.getAttrId());
                entity.setAttrName(baseAttr.getAttrName());
                String[] selected = baseAttr.getValueSelected();
                entity.setAttrValue(AppUtils.arrarToStringWitSeperator(selected, ","));
                entity.setSpuId(spuId);
                entity.setQuickShow(1);
                entity.setAttrSort(0);

                allSave.add(entity);
            }
        }
        spuAttrValueDao.insertBatch(allSave);

    }

    // 保存sku以及sku的营销相关信息
    @Transactional // 开启事务
    @Override
    public void saveSkuinfos(Long spuId, List<SkuVO> skus) {
        // 0 查出这个sku的信息
        SpuInfoEntity spuInfo = this.getById(spuId);

        List<SkuSaleInfoTo> tos = new ArrayList<>();
        // 1 保存sku的info信息
        for (SkuVO skuVO : skus) {
            SkuInfoEntity skuInfoEntity = new SkuInfoEntity();

            String[] images = skuVO.getImages();

            skuInfoEntity.setBrandId(spuInfo.getBrandId());
            skuInfoEntity.setCatalogId(spuInfo.getCatalogId());
            skuInfoEntity.setPrice(skuVO.getPrice());
            skuInfoEntity.setSkuCode(UUID.randomUUID().toString().substring(0, 5));

            if (skuVO.getImages() != null && skuVO.getImages().length > 0) {

                skuInfoEntity.setSkuDefaultImg(skuVO.getImages()[0]);
            }

            skuInfoEntity.setSkuDesc(skuVO.getSkuDesc());
            skuInfoEntity.setSkuName(skuVO.getSkuName());
            skuInfoEntity.setSkuSubtitle(skuVO.getSkuTitle());
            skuInfoEntity.setSpuId(spuId);
            skuInfoEntity.setWeight(skuVO.getWeight());

            skuInfoDao.insert(skuInfoEntity);




            // 2 保存sku的所有图片
            Long skuId = skuInfoEntity.getSkuId();
            for (int i = 0; i < images.length; i++) {
                SkuImagesEntity imagesEntity = new SkuImagesEntity();
                imagesEntity.setSkuId(skuId);
                imagesEntity.setDefaultImg(i == 0 ? 1 : 0);
                imagesEntity.setImgUrl(images[i]);
                imagesEntity.setImgSort(0);
                skuImagesDao.insert(imagesEntity);
            }

            // 3 当前sku的所有销售属性组合保存
            List<SaleAttrVo> saleAttrs = skuVO.getSaleAttrs();
            for (SaleAttrVo attrVo : saleAttrs) {
                // 查出当前属性的信息
                SkuSaleAttrValueEntity entity = new SkuSaleAttrValueEntity();
                // 查出这个属性的的真正信息
                AttrEntity attrEntity = attrDao.selectById(attrVo.getAttrId());
                entity.setSkuId(skuId);
                entity.setAttrId(attrVo.getAttrId());
                entity.setAttrName(attrEntity.getAttrName());
                entity.setAttrValue(attrVo.getAttrValue());
                entity.setAttrSort(0);

                skuSaleAttrValueDao.insert(entity);
            }

            // 以下需要由sms完成,保存每一个sku的相关优惠数据
            SkuSaleInfoTo info = new SkuSaleInfoTo();
            BeanUtils.copyProperties(skuVO,info);
            info.setSkuId(skuId);
            tos.add(info);

        }
            // 发给sms,让他处理,我们不管
            smsSkuSalelnfoFeignService.saveSkuSaleInfos(tos);

    }
    // 商品上下架功能
    @Override
    public void updateSpuStatus(Long spuId, Integer status) {


        if (status == 1){
            // 上架
            spuUp(spuId,status);
        }else{
            spuDwon(spuId,status);
        }


    }
    // 商品上架
    private void spuUp(Long spuId, Integer status){

        // 查出我们下面要使用的基本信息
        SpuInfoEntity spuInfoEntity = spuInfoDao.selectById(spuId);
        // 品牌名
        BrandEntity brandEntity = brandDao.selectById(spuInfoEntity.getBrandId());
        // 所属分类的名字
        CategoryEntity categoryEntity = categoryDao.selectById(spuInfoEntity.getCatalogId());

        // 上架: 将商品需要检索的信息放到es中 下架: 把商品从es中删除
        List<EsSkuVo> skuVos = new ArrayList<>();

        // 查出当前需要上架的spu的所有sku信息
        List<SkuInfoEntity> skus = skuInfoDao.selectList(new QueryWrapper<SkuInfoEntity>().eq("spu_Id", spuId));

        // 查出这个spu对应的sku所有的库存信息
        List<Long> skuIds = new ArrayList<>();
        skus.forEach(skuInfoEntity -> {
            skuIds.add(skuInfoEntity.getSkuId());
        });
        // 远程检索到所有的sku的库存信息
        Resp<List<SkuStockVo>> infos = wmsFeignService.skuWareInfo(skuIds);
        List<SkuStockVo> skuStockVos = infos.getData();

        // 查出当前spu所有可以供检索的属性
        List<ProductAttrValueEntity> spu_id = spuAttrValueDao.selectList(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id", spuId));
        // 过滤出可以被检索的
        List<Long> attrIds = new ArrayList<>();
        spu_id.forEach(item ->{
            attrIds.add(item.getAttrId());

        });
        List<AttrEntity> list = attrDao.selectList(new QueryWrapper<AttrEntity>().in("attr_id", attrIds).eq("search_type", 1));

        // 在spuid过滤出list的所有数据
       //  List<ProductAttrValueEntity> productAttrValueEntities = new ArrayList<>();
        List<EsSkuAttributeValue> esSkuAttributeValues = new ArrayList<>();
        list.forEach((item)->{
            //当前能被检索的属性
            Long attrId = item.getAttrId();
            //拿到真正的值
            spu_id.forEach((s)->{
                if(item.getAttrId() == s.getAttrId()){
                    //s
                    EsSkuAttributeValue value = new EsSkuAttributeValue();
                    value.setId(s.getId());
                    value.setName(s.getAttrName());
                    value.setProductAttributeId(s.getAttrId());
                    value.setSpuId(spuId);
                    value.setValue(s.getAttrValue());
                    esSkuAttributeValues.add(value);
                }
            });
        });




        if (skus != null && skus.size() >0) {

            // 构造所有需要保存在es中的sku信息
            skus.forEach(skuInfoEntity -> {
                EsSkuVo skuVo = skuInfoToEsSkuVo(skuInfoEntity,spuInfoEntity,brandEntity,categoryEntity,skuStockVos, esSkuAttributeValues);
                skuVos.add(skuVo);
            });



            // 远程调用search服务,将商品上架
            Resp<Object> resp = esFeignService.spuUp(skuVos);
            if (resp.getCode() == 0){
                // 调用成功
                System.out.println("调用成功");
                // 修改本地数据库
                SpuInfoEntity entity = new SpuInfoEntity();
                entity.setId(spuId);
                entity.setPublishStatus(1);
                entity.setUodateTime(new Date());
                spuInfoDao.updateById(entity);
            }
        }

    }

    // 商品下架
    private void spuDwon(Long spuId, Integer status){

    }

    // 将SkuInfoEntity加工成EsSkuVo
    private EsSkuVo skuInfoToEsSkuVo(SkuInfoEntity sku, SpuInfoEntity spuInfoEntity, BrandEntity brandEntity, CategoryEntity categoryEntity, List<SkuStockVo> skuStockVos, List<EsSkuAttributeValue> productAttrValueEntities){

        EsSkuVo vo = new EsSkuVo();
        vo.setId(sku.getSkuId());
        vo.setBrandId(sku.getBrandId());


        if (brandEntity != null){
            vo.setBrandName(brandEntity.getName());
        }
        // 标题
        vo.setName(sku.getSkuTitle());
        // sku的图片
        vo.setPic(sku.getSkuDefaultImg());

        // sku的价格
        vo.setPrice(sku.getPrice());
        // 所属分类的id
        vo.setProductCategoryId(sku.getCatalogId());

        if (categoryEntity != null) {
            // 所属分类的名字
          vo.setProductCategoryName(categoryEntity.getName());
        }
        vo.setSale(0);
        vo.setStock(0);

        // 保存自己的库存
        skuStockVos.forEach((item) ->{
            if (item.getSkuId() == sku.getSkuId()){
                vo.setStock(item.getStock());
            }
        });

        vo.setAttrValueList(productAttrValueEntities);

        return vo;
    }
}