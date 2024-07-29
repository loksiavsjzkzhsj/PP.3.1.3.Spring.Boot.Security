package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public String showAllUsers(ModelMap model) {
        model.addAttribute("usersList", userService.findAll());
        model.addAttribute("rolesList", roleService.findAll());
        return "admin/admin_page";
    }

    @GetMapping("/addNewUser")
    public String addNewUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("rolesList", roleService.findAll());

        return "admin/new_user_form";
    }

    @PostMapping("/addNewUser/save")
    public String saveUser(@ModelAttribute("user") User user) {
        userService.create(user);

        return "redirect:/admin";
    }

    @GetMapping("/updateInfo/{id}/form")
    public String changeUser(@PathVariable("id") Long userId, Model model) {
        model.addAttribute("user", userService.findById(userId));
        model.addAttribute("rolesList", roleService.findAll());
        return "admin/update_user";
    }

    @PatchMapping("/{id}")
    public String updateUser(@ModelAttribute("user") User user, @PathVariable("id") Long id) {
        userService.updateUser(user.getId(), user);
        return "redirect:/admin";
    }

    @GetMapping("/deleteUser{id}")
    public String deleteUser(@RequestParam("id") Long userId) {
        userService.delete(userId);
        return "redirect:/admin";
    }

    @GetMapping("/info")
    public String info(Principal principal, ModelMap model) {
        UserDetails user = userService.loadUserByUsername(principal.getName());
        model.addAttribute("username", user);
        return "user/user_info";
    }

}
