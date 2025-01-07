package br.com.gunthercloud.coursejava.repositories;

import br.com.gunthercloud.coursejava.entities.OrderItem;
import br.com.gunthercloud.coursejava.entities.pk.OrderItemPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK> {

}
