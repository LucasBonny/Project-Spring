package br.com.gunthercloud.coursejava.resources;

import br.com.gunthercloud.coursejava.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/users")
public class UserResource {

    @GetMapping
    public ResponseEntity<User> findAll(){
        User user = new User(1L, "Lucas", "lucas@lucasbonny","61966997755", "123456");
        return ResponseEntity.ok().body(user);
    }
}
