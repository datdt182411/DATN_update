package com.example.test.Service;

import com.example.test.Entity.Role;
import com.example.test.Entity.Users;
import com.example.test.Repository.RoleRepository;
import com.example.test.Repository.UserRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
public class UserServices {

	private UserRepository repo;

	private PasswordEncoder passwordEncoder;
	

	private JavaMailSender mailSender;

	private RoleRepository roleRepository;


	public List<Users> listAll() {
		return repo.findAll();
	}
	
	public void register(Users user, String siteURL, Model model)
			throws UnsupportedEncodingException, MessagingException {
		List<Role> roles= roleRepository.findRoleByName("MEMBER");
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		user.setRoleList(roles);
		String randomCode = RandomString.make(64);
		user.setVerificationCode(randomCode);
		user.setStatus(0);

		repo.save(user);
		
		sendVerificationEmail(user, siteURL);
	}
	
	private void sendVerificationEmail(Users user, String siteURL)
			throws MessagingException, UnsupportedEncodingException {
		String toAddress = user.getEmail();
		String fromAddress = "nongoanh2000@gmail.com";
		String senderName = "Công ty thiết bị vật tư y tế";
		String subject = "Please verify your registration";
		String content = "Dear [[name]],<br>"
				+ "Please click the link below to verify your registration:<br>"
				+ "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
				+ "Thank you,<br>"
				+ "Công ty thiết bị y tế.";
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		helper.setFrom(fromAddress, senderName);
		helper.setTo(toAddress);
		helper.setSubject(subject);
		
		content = content.replace("[[name]]", user.getUsername());
		String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();

		System.out.println(user.getVerificationCode());

		content = content.replace("[[URL]]", verifyURL);

		System.out.println(verifyURL);
		helper.setText(content, true);
		
		mailSender.send(message);
		
		System.out.println("Email has been sent");
	}
	
	public boolean verify(String verificationCode) {
		Users user = repo.findByVerificationCode(verificationCode);
		if (user == null || user.getStatus()==1) {
			return false;
		} else {
			user.setVerificationCode(null);
			user.setStatus(1);
			repo.save(user);
			return true;
		}
		
	}
	
}
