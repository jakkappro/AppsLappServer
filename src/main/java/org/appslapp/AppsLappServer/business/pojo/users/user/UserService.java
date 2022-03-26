package org.appslapp.AppsLappServer.business.pojo.users.user;

import net.bytebuddy.utility.RandomString;
import org.appslapp.AppsLappServer.business.pojo.users.entity.EntityService;
import org.appslapp.AppsLappServer.business.pojo.users.labmaster.Labmaster;
import org.appslapp.AppsLappServer.persistance.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Service
public class UserService implements EntityService<User> {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JavaMailSender mailSender;

    public UserService(@Autowired UserRepository repository, @Autowired PasswordEncoder encoder,
                       @Autowired JavaMailSender sender) {
        this.userRepository = repository;
        this.encoder = encoder;
        this.mailSender = sender;
    }

    public long update(User user) {
        return userRepository.save(user).getId();
    }

    public List<String> getStudents() {
        var map = new ArrayList<String>();
        for (var user : userRepository.findAllByAuthorityAndEnabled("PUPIL", true)) {
            map.add(user.getFirstName() + " " + user.getLastName());
        }
        return map;
    }

    public long enable(User user) {
        user.setEnabled(true);
        return userRepository.save(user).getId();
    }

    public Optional<User> findByCode(String code) {
        return userRepository.findByVerificationCode(code);
    }

    public long save(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent() ||
                userRepository.findByEmail(user.getEmail()).isPresent())
            return -2;

        if (!isPasswordValid(user.getPassword()))
            return -1;

        if (user.createGrantedAuthority() == null)
            user.setAuthority("PUPIL");

        user.setPassword(encoder.encode(user.getPassword()));

        user.setEnabled(false);

        user.setVerificationCode(RandomString.make(64));

        var id = userRepository.save(user).getId();

        try {
            sendVerificationEmail(user);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return id;
    }

    public Optional<User> getUserById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByName(String username) {
        return userRepository.findByUsername(username);
    }

    public long resendEmail(String username) {
        var user = getUserByName(username);
        if (user.isEmpty())
            return -1;
        try {
            sendVerificationEmail(user.get());
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return user.get().getId();
    }

    private boolean isPasswordValid(String password) {
        var numberRegex = ".*[0-9]+.*";
        var lowerCaseLettersRegex = ".*[a-z]+.*";
        var upperCaseLettersRegex = ".*[A-Z]+.*";
        var specialCharactersRegex = ".*[^a-zA-Z0-9]+.*";

        return password.matches(numberRegex) && password.matches(lowerCaseLettersRegex) &&
                password.matches(specialCharactersRegex) && password.matches(upperCaseLettersRegex) &&
                password.matches(".{8,16}");
    }

    private void sendVerificationEmail(User user) throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "appslappmanagement@gmail.com";
        String senderName = "AppsLapp";
        String subject = "Potvrdte registraciu";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "AppsLapp.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getUsername());

        String verifyURL = "https://apps-lapp-server.herokuapp.com" + "/api/auth/verify?code=" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);
    }

    public Labmaster createLabmaster(User user, String password) {
        var labmaster = new Labmaster();
        labmaster.setUsername(user.getUsername());
        labmaster.setFirstName(user.getFirstName());
        labmaster.setLastName(user.getLastName());
        labmaster.setEmail(user.getEmail());
        labmaster.setPassword(encoder.encode(password));
        userRepository.delete(user);
        return labmaster;
    }
}