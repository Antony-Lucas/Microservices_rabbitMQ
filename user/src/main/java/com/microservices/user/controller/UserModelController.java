package com.microservices.user.controller;

import com.microservices.user.dtos.UserRecordDTO;
import com.microservices.user.models.UserModel;
import com.microservices.user.services.UserModelServices;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserModelController {
    final UserModelServices userModelServices;

    public UserModelController(UserModelServices userModelServices){
        this.userModelServices = userModelServices;
    }
    @GetMapping("/get")
    public ResponseEntity<List<UserModel>> findAll(){
        List<UserModel> list = userModelServices.findAll();
        return ResponseEntity.ok().body(list);
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<Optional<UserModel>> findById(@PathVariable UUID id){
        Optional<UserModel> obj = userModelServices.findById(id);
        return ResponseEntity.ok().body(obj);
    }
    @PostMapping("/create")
    public ResponseEntity<UserModel> saveUser(@RequestBody @Valid UserRecordDTO userRecordDTO){
        var userModel = new UserModel();
        BeanUtils.copyProperties(userRecordDTO, userModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(userModelServices.createUser(userModel));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<UserModel> deleteUser(@PathVariable UUID id){
        userModelServices.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/update/{id}")
    public ResponseEntity<UserModel> updateUser(@PathVariable UUID id, @RequestBody @Valid UserRecordDTO userRecordDTO){
        var userModel = new UserModel();
        BeanUtils.copyProperties(userRecordDTO, userModel);
        UserModel obj = userModelServices.updateUser(id, userModel);
        return ResponseEntity.ok().body(obj);
    }
}
