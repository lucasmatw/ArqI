package ar.edu.mercadogratis.app.controller;

import ar.edu.mercadogratis.app.model.User;
import ar.edu.mercadogratis.app.service.UserService;
import org.apache.commons.mail.EmailException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/user/register", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<Long> addUser(@RequestBody User user) {
        User userLogin = userService.getUserForMail(user.getEmail());
		Long userId;
        if (userLogin == null) {
			userId = userService.addUser(user);
        } else {
			userId = userLogin.getId();
		}
        return ResponseEntity.ok(userId);
    }

    @RequestMapping(value = "/user/login", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity login(@RequestBody User user) {
        User userLogin = userService.getUserForMail(user.getEmail());
        if (userLogin != null && user.getPassword().equals(userLogin.getPassword())) {
            return ResponseEntity.ok().body(userLogin.getId());
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
    }

    @RequestMapping(value = "/user/forgetPassword", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity forgetPassword(@RequestBody User user) {
        try {
            userService.forgetPassword(user.getEmail());
            return ResponseEntity.ok().body(null);

        } catch (EmailException e) {

        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);

    }

    @RequestMapping(value = "/user/changePassword", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity changePassword(@RequestBody String stringJson) {
        Long idUser;
        try {
            JSONObject userWithNewPassword = new JSONObject(stringJson);
            idUser = userService.changePassword(userWithNewPassword);
        } catch (RuntimeException | JSONException err) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
        }
        return ResponseEntity.ok().body(idUser);
    }
}
