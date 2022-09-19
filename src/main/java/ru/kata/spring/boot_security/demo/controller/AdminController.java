package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController (UserService userService){
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/")
    public String userList(Model model) {
        model.addAttribute("usersList", userService.getAllUsers());
        model.addAttribute("rolesList", userService.getAllRoles());
        model.addAttribute("newUser", new User());
        model.addAttribute("authorisedUser", (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal());
        return "adminPage";
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public String addUser(@ModelAttribute("newUser") User user) {
        userService.saveUser(user);
        return "redirect:/admin/";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/edit")
    public String editUser(@PathVariable("id") Long id, Model model) {
        User userToEdit = userService.getUserById(id);
        String roleUser = (userToEdit.getStringRoles().contains("USER") ? "on" : null);
        String roleAdmin = (userToEdit.getStringRoles().contains("ADMIN") ? "on" : null);

        model.addAttribute("userToEdit", userService.getUserById(id));
        model.addAttribute("roleUser", roleUser);
        model.addAttribute("roleAdmin", roleAdmin);
        return "adminController/edit";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public String update(@ModelAttribute("user") User user) {
        userService.updateUser(user);
        return "redirect:/admin/";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/";
    }

}
