package com.microservices.user.services;

import com.microservices.user.models.UserModel;
import com.microservices.user.producer.UserProducer;
import com.microservices.user.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserModelServices {
    final UserRepository userRepository;
    final UserProducer userProducer;

    public UserModelServices(UserRepository userRepository, UserProducer userProducer){
        this.userRepository = userRepository;
        this.userProducer = userProducer;
    }

    public List<UserModel> findAll(){
        return userRepository.findAll();
    }

    public Optional<UserModel> findById(UUID id){
        return userRepository.findById(id);
    }

    @Transactional
    public void deleteUser(UUID id){
        try{
            userRepository.deleteById(id);
        }catch (EntityNotFoundException e){
            throw new EntityNotFoundException("Usuário não encontrado com o Id: " + e);
        }
    }

    @Transactional
    public UserModel updateUser(UUID id, UserModel obj){
        try{
            UserModel existingUserModel = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id: " + id + " Não encontrado"));
            if (obj.getUsername() != null){
                existingUserModel.setUsername(obj.getUsername());
            }else if(obj.getEmail() != null){
                existingUserModel.setEmail(obj.getEmail());
            }

            return userRepository.save(existingUserModel);

        }catch (RuntimeException e){
            throw new EntityNotFoundException(id.toString());
        }
    }

    @Transactional
    public UserModel createUser(UserModel obj){
        obj = userRepository.save(obj);
        userProducer.publishMessageEmail(obj);
        return obj;
    }
}
