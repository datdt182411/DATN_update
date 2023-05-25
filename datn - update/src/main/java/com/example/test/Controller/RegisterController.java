package com.example.test.Controller;

import com.example.test.Entity.Users;
import com.example.test.Repository.RoleRepository;
import com.example.test.Repository.UserRepository;
import com.example.test.Service.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Controller
public class RegisterController {
    @Autowired
    private UserServices userServices;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public RegisterController(RoleRepository roleRepository,
                              UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String getRegister(Model model, @ModelAttribute("user") Users users) {
        model.addAttribute("user", new Users());
        return "User/register";
    }

    @PostMapping("/register")
    public String register(Model model, @ModelAttribute("user") Users users,
                           HttpServletRequest request)
                           throws UnsupportedEncodingException, MessagingException {
        try {
            if(userRepository.getUsersByUsername(users.getUsername())!=null){
                model.addAttribute("mess", "Tài khoản đã tồn tại, vui lòng nhập username khác! ");
                return "User/register";
            }
                userServices.register(users, getSiteURL(request),model);
                model.addAttribute("sucess", "Thêm tài khoản Thành Công, Bạn có thể đăng nhập ngay bây giờ! ");
        }catch (Exception ex){
            model.addAttribute("mess", "Thêm tài khoản lỗi! ");
        }
        return "User/register_success";
    }
//Test///////////////////////////////////
    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    @GetMapping("/verify")
    public String verifyUser(@Param("code") String code) {
        if (userServices.verify(code)) {
            return "User/verify_success";
        } else {
            return "User/verify_fail";
        }
    }
}
