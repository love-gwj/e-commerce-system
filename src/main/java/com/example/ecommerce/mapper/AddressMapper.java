package com.example.ecommerce.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ecommerce.entity.Address;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddressMapper extends BaseMapper<Address> {
}
