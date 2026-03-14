package com.example.ecommerce.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.ecommerce.entity.Address;

import java.util.List;

public interface AddressService extends IService<Address> {
    List<Address> getByUserId(Long userId);
    void saveAddress(Address address);
    void updateAddress(Address address);
    void deleteAddress(Long addressId);
}
