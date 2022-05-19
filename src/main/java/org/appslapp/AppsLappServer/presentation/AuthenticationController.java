package org.appslapp.AppsLappServer.presentation;

import org.appslapp.AppsLappServer.business.pojo.users.admin.AdminService;
import org.appslapp.AppsLappServer.business.pojo.users.labmaster.Labmaster;
import org.appslapp.AppsLappServer.business.pojo.users.labmaster.LabmasterService;
import org.appslapp.AppsLappServer.business.pojo.users.user.User;
import org.appslapp.AppsLappServer.business.pojo.users.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/auth/")
public class AuthenticationController {
    private final UserService userService;
    private final AdminService adminService;
    private final LabmasterService labmasterService;

    @Autowired
    public AuthenticationController(UserService userService, AdminService adminService,
                                    LabmasterService labmasterService) {
        this.userService = userService;
        this.adminService = adminService;
        this.labmasterService = labmasterService;
    }

    @PostMapping("register")
    public long register(@Valid @RequestBody User user) {
        return userService.save(user);
    }

    @GetMapping("login")
    public long login() {
        return 0;
    }

    @PostMapping("resendEmail/{username}")
    public long resendEmail(@PathVariable String username) {
        return userService.resendEmail(username);
    }

    @GetMapping("verify")
    public ResponseEntity<Void> verifyEmail(@RequestParam String code) {
        return ResponseEntity.status(HttpStatus.OK).location(
                URI.create("https://appslappapp.vercel.app/emailV?email=" + userService.verifyUser(code))).build();
    }

    @PostMapping("promoteToLabmaster")
    public long promoteToLabmaster(@RequestParam String username, @RequestParam String password) {
        return labmasterService.save(userService.createLabmaster(username, password));
    }

    //Only for test purposes
    @PostMapping("createAdmins")
    public void tem() {
        /*var admin = new Admin();
        admin.setUsername("admin");
        admin.setPassword("Heslo123_");
        admin.setFirstName("Martinko");
        admin.setLastName("Klingacik");
        admin.setEnabled(true);
        admin.setEmail("huhuhu@gmail.com");
        admin.setAuthority("ADMIN");
        adminService.save(admin);

        var user = new User();
        user.setUsername("user");
        user.setPassword("Heslo123_");
        user.setFirstName("ferko");
        user.setLastName("jozko");
        user.setEnabled(true);
        user.setEmail("emailovaadresa@gmail.com");
        user.setAuthority("PUPIL");
        userService.save(user);*/

        var labmaster =  new Labmaster();
        labmaster.setUsername("ratatui");
        labmaster.setPassword("Heslo123_");
        labmaster.setFirstName("jozko");
        labmaster.setLastName("ferko");
        labmaster.setEnabled(true);
        labmaster.setEmail("tratata@gmail.com");
        labmaster.setAuthority("LABMASTER");
        labmasterService.save(labmaster);
    }
}
