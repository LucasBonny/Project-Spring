package br.com.gunthercloud.coursejava.repositories;

import br.com.gunthercloud.coursejava.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
