package com.DTeam.eshop.controllers.customer;

import java.security.Principal;

import javax.validation.Valid;

import com.DTeam.eshop.entities.Address;
import com.DTeam.eshop.entities.Customer;
import com.DTeam.eshop.services.AddressService;
import com.DTeam.eshop.services.CustomerService;
import com.DTeam.eshop.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Klasa obsługująca Profil-Klient
 * @author 
 */
@Controller
public class CustomerProfileController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private UserService userService;

    /**
     * Metoda Wyswietla Profil danego Klienta
     * @param model przechowywanie atrybutów modelu
     * @param principal przechowuje email Klienta
     * @return widok Profilu Klienta
     */
    @GetMapping("/customer/profile")
    public String edit(Model model, Principal principal) {
        String email = principal.getName();
        if (customerService.isCustomerExist(email)) {
            Customer customer = customerService.getByEmail(email);
            model.addAttribute("customer", customer);
            if (customer.getAddress() == null) {
                Address address = new Address();
                model.addAttribute("address", address);
            } else {
                Address address = addressService.getByCustomerId(customer.getCustomerId());
                model.addAttribute("address", address);
            }
            return "views/customer/profile";
        } else {
            Customer customer = new Customer();
            Address address = new Address();
            model.addAttribute("customer", customer);
            model.addAttribute("address", address);
            return "views/customer/profile";
        }
    }

    /**
     * Metoda edytuje Profil Klienta
     * @param customer przechowuje Dane Klienta
     * @param bindingResult walidacja błędów
     * @param address przechowuje adres Klienta
     * @param bindingResult2 walidacja błędów
     * @param principal przechowuje email Klienta
     * @return widok Profilu Klienta
     */
    @PostMapping("/customer/profile")
    public String edit(@Valid Customer customer, BindingResult bindingResult, @Valid Address address,
            BindingResult bindingResult2, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "views/customer/profile";
        }
        if (bindingResult2.hasErrors()) {
            return "views/customer/profile";
        }
        String email = principal.getName();
        customer.setUser(userService.get(email));
        addressService.save(address);
        customer.setAddress(address);
        customerService.save(customer);
        return "redirect:/customer/profile";
    }
}