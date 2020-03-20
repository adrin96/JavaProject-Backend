package com.DTeam.eshop.controllers.employee;

import com.DTeam.eshop.services.OrderService;
import com.DTeam.eshop.entities.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class EmployeeDetailOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/employee/order/detail/{id}")
    public String detail(Model model,@PathVariable(name = "id") long id){
        Order orderList = orderService.get(id);
        model.addAttribute("orderList", orderList);
        return "views/employee/detail";
    }
}