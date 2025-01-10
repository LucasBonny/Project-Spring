package br.com.gunthercloud.coursejava.services;

import br.com.gunthercloud.coursejava.entities.User;
import br.com.gunthercloud.coursejava.repositories.UserRepository;
import br.com.gunthercloud.coursejava.services.exceptions.DatabaseException;
import br.com.gunthercloud.coursejava.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public List<User> findAll(){
        return repository.findAll();
    }

    public User findById(Long id) {
        Optional<User> user = repository.findById(id);
        return user.orElseThrow(() -> new ResourceNotFoundException(id));
    }
    public User insert(User obj) {
        return repository.save(obj);
    }
    public void delete(Long id) {
        //repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        if(repository.findById(id).isEmpty()) throw new ResourceNotFoundException(id);
        try {
            repository.deleteById(id);
        }
        catch(DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public User update(Long id, User obj) {
        try{
            User entity = repository.getReferenceById(id);
            updateData(entity, obj);
            return repository.save(entity);
        }
        catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    private void updateData(User entity, User obj) {
        entity.setName(obj.getName());
        entity.setEmail(obj.getEmail());
        entity.setPhone(obj.getPhone());
    }
}
