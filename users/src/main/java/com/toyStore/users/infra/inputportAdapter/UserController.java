package com.toyStore.users.infra.inputportAdapter;

import com.toyStore.users.domain.User;
import com.toyStore.users.infra.dto.UserDTO;
import com.toyStore.users.infra.dto.UserUpdatingDTO;
import com.toyStore.users.infra.inputport.IUserInputport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private IUserInputport userInputport;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        try {
            UserDTO userDTO = userInputport.getUserById(id);

            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error getting user", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers(@RequestParam int page, @RequestParam int size) {
        try {
            List<UserDTO> usersDTO = userInputport.getAllUsers(page, size);

            return new ResponseEntity<>(usersDTO, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error getting users", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<UserDTO> userCreate(@RequestBody User user) {
        try {
            UserDTO userDTO = userInputport.createUser(user);

            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error creating user", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserDTO> userUpdate(@PathVariable Long id, @RequestBody UserUpdatingDTO user) {
        try {
            UserDTO userDTO = userInputport.updateUser(id, user);

            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error editing user", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try {
            String msj = userInputport.deleteUser(id);

            return new ResponseEntity<>(msj, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error deleting user", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
