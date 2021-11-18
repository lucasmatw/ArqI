package ar.edu.mercadogratis.app.service;

import ar.edu.mercadogratis.app.dao.UserRepository;
import ar.edu.mercadogratis.app.model.User;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@RequiredArgsConstructor
@Service("userService")
public class UserService {

    private final UserRepository userRepository;
    private final IEmailService emailService;
    private final PasswordGeneratorService passwordGeneratorService;
    private final MoneyAccountService moneyAccountService;

    @Transactional
    public User getUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Transactional
    public User getUserForMail(String mail) {
        return userRepository.searchUserByEmail(mail).orElse(null);
    }

    @Transactional
    public Long addUser(User user) {
        String generatedPwd = passwordGeneratorService.generateRandom();
        sendRegistrationEmail(user, generatedPwd);
        user.setPassword(generatedPwd);

        User savedUser = userRepository.save(user);
        moneyAccountService.registerAccount(savedUser);

        return savedUser.getId();
    }

    private void sendRegistrationEmail(User user, String generatedPwd) {
        emailService.send(user.getEmail(), "Bienvenido a MercadoGratis", "Tu password es: " + generatedPwd);
    }

    @Transactional
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void forgetPassword(String mail) {
        User user = this.getUserForMail(mail);
        if (user != null) {
            emailService.send(user.getEmail(), "Bienvenido a MercadoGratis", "Tu password es: " + user.getPassword());
        }
    }

    @Transactional
    public Long changePassword(JSONObject userWithNewPassword) throws RuntimeException, JSONException {
        Long idUser = null;

        User user = this.getUserForMail(userWithNewPassword.getString("email"));

        if (user != null && user.getPassword().equals(userWithNewPassword.getString("password"))) {
            idUser = user.getId();

            user.setPassword(userWithNewPassword.getString("newpassword"));
            userRepository.save(user);

        } else {
            throw new RuntimeException("Error User o Password Ingresada");
        }
        return idUser;
    }
}
