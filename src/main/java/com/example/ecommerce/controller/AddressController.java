package com.example.ecommerce.controller;

import com.example.ecommerce.common.Result;
import com.example.ecommerce.entity.Address;
import com.example.ecommerce.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping("/list")
    public Result<List<Address>> list(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        List<Address> addresses = addressService.getByUserId(userId);
        return Result.success(addresses);
    }

    @PostMapping("/add")
    public Result<?> add(@RequestBody Address address, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        address.setUserId(userId);
        addressService.saveAddress(address);
        return Result.success();
    }

    @PutMapping("/update")
    public Result<?> update(@RequestBody Address address) {
        addressService.updateAddress(address);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return Result.success();
    }
}
