package br.com.gunthercloud.coursejava.repositories;

import br.com.gunthercloud.coursejava.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
