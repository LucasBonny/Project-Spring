package br.com.gunthercloud.coursejava.repositories;

import br.com.gunthercloud.coursejava.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
