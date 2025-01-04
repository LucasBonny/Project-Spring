package br.com.gunthercloud.coursejava.repositories;

import br.com.gunthercloud.coursejava.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
