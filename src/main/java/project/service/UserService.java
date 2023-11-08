package project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.entity.Company;
import project.entity.User;
import project.repository.CompanyRepository;
import project.repository.UserRepository;

import java.util.List;

@Service
@Transactional
public class UserService {
    @Autowired
    UserRepository repo;

    public void save(User user){
        repo.save(user);
    }

    public User get(Long id) {
        return repo.findById(id).get();
    }

    public List<User> listAll() {
        return (List<User>) repo.findAll();
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
