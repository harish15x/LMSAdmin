package com.bridgelabz.lmsadmin.controller;

import com.bridgelabz.lmsadmin.dto.AdminDTO;
import com.bridgelabz.lmsadmin.model.AdminModel;
import com.bridgelabz.lmsadmin.service.IAdminService;
import com.bridgelabz.lmsadmin.util.Response;
import com.bridgelabz.lmsadmin.util.ResponseClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    IAdminService adminService;

    @PostMapping("/addadmin")
    public ResponseEntity<ResponseClass> addAdmin(@Valid @RequestBody AdminDTO adminDTO){
        ResponseClass responseClass = adminService.addAdmin(adminDTO);
        return new ResponseEntity<>(responseClass, HttpStatus.OK);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<ResponseClass> updateAdmin(@RequestHeader String token, @Valid @RequestParam AdminDTO adminDTO, @PathVariable long id){
        ResponseClass responseClass = adminService.updateAdmin(token, adminDTO, id);
        return new ResponseEntity<>(responseClass, HttpStatus.OK);
    }

    @GetMapping("getadmindata")
    public ResponseEntity <List<?>> getAllAdmin(@RequestHeader String token){
        List<AdminModel> responseClass = adminService.getAllAdmin(token);
        return  new ResponseEntity<>(responseClass, HttpStatus.OK);
    }

    @DeleteMapping("deleteadmin/{id}")
    public ResponseEntity <ResponseClass>deleteAdmin(@PathVariable Long id, @RequestHeader String token){
        ResponseClass responseClass = adminService.getDeleteAdmin(id, token);
        return new ResponseEntity<>(responseClass, HttpStatus.OK);
    }

    @PostMapping("login")
    public ResponseEntity<Response> login(@RequestParam String email, @RequestParam String password){
        Response response = adminService.login(email, password);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("changepassword")
    public ResponseEntity<ResponseClass> changePassword(@RequestHeader String token, @RequestParam String password){
        ResponseClass responseClass = adminService.changePassword(token, password);
        return new ResponseEntity<>(responseClass, HttpStatus.OK);
    }

    @PutMapping("resetpassword")
    public ResponseEntity<ResponseClass> resetPassword(@RequestParam String emailId){
        ResponseClass responseClass =adminService.resetPassword(emailId);
        return new ResponseEntity<>(responseClass, HttpStatus.OK);

    }

    @GetMapping("/string/{token}")
    public Boolean validate(@PathVariable String token){
        return adminService.validate(token);
    }









}
