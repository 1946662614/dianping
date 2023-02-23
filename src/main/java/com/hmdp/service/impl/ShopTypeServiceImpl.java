package com.hmdp.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.hmdp.dto.Result;
import com.hmdp.entity.ShopType;
import com.hmdp.mapper.ShopTypeMapper;
import com.hmdp.service.IShopTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import static com.hmdp.utils.RedisConstants.CACHE_SHOP_TYPE_KEY;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    
    /**
     * 将商户信息存入redis
     * @return
     */
    @Override
    public Result getShopTypeByList() {
        // 1.从redis查询商铺类型
        String shop_typeJson = stringRedisTemplate.opsForValue().get(CACHE_SHOP_TYPE_KEY);
        List<ShopType> shopTypes = JSONUtil.toList(shop_typeJson, ShopType.class);
        // 2.判断是否存在
        if (CollectionUtil.isNotEmpty(shopTypes)) {
            // 3.存在，则直接返回
            return Result.ok(shopTypes);
        }
        // 4.不存在，根据id查询数据库
        List<ShopType> list = query().orderByAsc("sort").list();
        if (CollectionUtil.isEmpty(list)) {
            // 5.数据库不存在，返回错误
            return Result.fail("商户类型不存在！");
        }
        // 6.存在，存入redis
        stringRedisTemplate.opsForValue().set(CACHE_SHOP_TYPE_KEY,JSONUtil.toJsonStr(list));
        // 7.返回
        return Result.ok(list);
    }
}
