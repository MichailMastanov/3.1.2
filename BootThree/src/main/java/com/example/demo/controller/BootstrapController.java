package com.example.demo.controller;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@Controller
public class BootstrapController {

    private UserRepository userRepository;

    @Autowired
    public void setUserRepositories(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private RoleRepository roleRepository;

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    @GetMapping("/admin/index")
    public String usersAdminAll(Model model, Principal principal){
        User userName = (User)userRepository.findByUsername(principal.getName());
        model.addAttribute("user", userName);
        model.addAttribute("xxx", userRepository.findAll());
        return "index_admin";
    }

    @GetMapping("/admin/updates/{id}")
        public String updateUsers(@PathVariable("id") Long id, Model model){
        System.out.println(id);
            User user = userRepository.getOne(id);
            model.addAttribute("id", user.getId());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("password", user.getPassword());
            return "editUser";
    }

    @PostMapping("/admin/updatesUserNew")
    public String updateUser(@RequestParam("id") Long id,
                             @RequestParam("username") String username,
                             @RequestParam("password") String password,
                             @RequestParam("roles") String[] roles,
                             Model model) {
        Set<Role> roleSet = new HashSet<>();
        for (String role : roles) {
            roleSet.add(roleRepository.findByName(role.toLowerCase()));
        }
        User user = new User(username, password);
        user.setRole(roleSet);
        user.setId(id);
        userRepository.save(user);
        model.addAttribute("xxx", userRepository.findAll());
        return "redirect:/admin/index";
    }

    @GetMapping("/admin/deleteUser/{id}")
    public String deleteUserId(@PathVariable("id") Long id, Model model){
        System.out.println("Delete");
        User user = userRepository.getOne(id);
        model.addAttribute("id", user.getId());
        model.addAttribute("username", user.getUsername());
        model.addAttribute("password", user.getPassword());
        return "deleteUser";
    }

    @PostMapping("/admin/delUserItem")
    public String deleteUser(@RequestParam Long id){
        userRepository.deleteById(id);
        return "redirect:/admin/index";
    }

    @PostMapping("/admin/createNewUser")
    public String addNewUser(@RequestParam(value = "username") String username,
                             @RequestParam(value = "password") String password,
                             @RequestParam("roles") String[] roles) {
        Set<Role> roleSet = new HashSet<>();
        for (String role : roles) {
            System.out.println(role);
            roleSet.add(roleRepository.findByName(role.toLowerCase()));
        }
        User user = new User(username,password);
        user.setRole(roleSet);
        userRepository.save(user);
        return "redirect:/admin/index";
    }

    @GetMapping("/user")
    public String openUser(Principal principal, Model model){
        User userName = (User)userRepository.findByUsername(principal.getName());
        model.addAttribute("user", userName);
        return "userOne";
    }

}


