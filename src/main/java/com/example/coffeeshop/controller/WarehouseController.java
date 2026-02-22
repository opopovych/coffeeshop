package com.example.coffeeshop.controller;

import com.example.coffeeshop.model.dto.ProductReportDto;
import com.example.coffeeshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/warehouse")
@RequiredArgsConstructor
public class WarehouseController {

    private final OrderService orderService;

    @GetMapping("/pick-list")
    public String showPickList(Model model) {
        List<ProductReportDto> items = orderService.getWarehousePickList();
        model.addAttribute("items", items);
        return "admin/warehouse-pick-list";
    }
}