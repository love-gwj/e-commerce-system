package com.example.ecommerce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ecommerce.entity.Address;
import com.example.ecommerce.mapper.AddressMapper;
import com.example.ecommerce.service.AddressService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {

    @Override
    public List<Address> getByUserId(Long userId) {
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address::getUserId, userId);
        wrapper.orderByDesc(Address::getIsDefault);
        return list(wrapper);
    }

    @Override
    @Transactional
    public void saveAddress(Address address) {
        if (address.getIsDefault()) {
            clearDefault(address.getUserId());
        }
        save(address);
    }

    private void clearDefault(Long userId) {
        LambdaUpdateWrapper<Address> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Address::getUserId, userId);
        wrapper.set(Address::getIsDefault, false);
        update(wrapper);
    }

    @Override
    @Transactional
    public void updateAddress(Address address) {
        if (address.getIsDefault()) {
            clearDefault(address.getUserId());
        }
        updateById(address);
    }

    @Override
    public void deleteAddress(Long addressId) {
        removeById(addressId);
    }
}
