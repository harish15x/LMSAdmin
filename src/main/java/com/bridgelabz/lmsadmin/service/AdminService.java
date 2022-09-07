package com.bridgelabz.lmsadmin.service;

import com.bridgelabz.lmsadmin.dto.AdminDTO;
import com.bridgelabz.lmsadmin.exception.AdminNotFoundException;
import com.bridgelabz.lmsadmin.model.AdminModel;
import com.bridgelabz.lmsadmin.repository.AdminRepository;
import com.bridgelabz.lmsadmin.util.Response;
import com.bridgelabz.lmsadmin.util.ResponseClass;
import com.bridgelabz.lmsadmin.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService implements IAdminService {

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    MailService mailService;

    @Autowired
    TokenUtil tokenUtil;

    @Override
    public ResponseClass addAdmin(AdminDTO adminDTO) {
        AdminModel adminModel = new AdminModel(adminDTO);
        adminModel.setRegistereddate(LocalDateTime.now());
        adminRepository.save(adminModel);
        String body = "admin has been added sucessfully " + adminModel.getId();
        String subject = "admin registration completed";
        mailService.send(adminModel.getEmailId(),body, subject);
        return new ResponseClass(200, "Sucessfull", adminModel);
    }

    @Override
    public ResponseClass updateAdmin(String token, AdminDTO adminDTO, long id) {
        Long userID = tokenUtil.decodeToken(token);
        Optional<AdminModel> isAdminPresent = adminRepository.findById(userID);
        if (isAdminPresent.isPresent()){
            Optional<AdminModel> isAdminAvailable = adminRepository.findById(id);
            if (isAdminAvailable.isPresent()){
                isAdminAvailable.get().setFirstName(adminDTO.getFirstName());
                isAdminAvailable.get().setLastName(adminDTO.getLastName());
                isAdminAvailable.get().setMobile(adminDTO.getMobile());
                isAdminAvailable.get().setEmailId(adminDTO.getEmailId());
                isAdminAvailable.get().setProfilePath(adminDTO.getProfilePath());
                isAdminAvailable.get().setStatus(adminDTO.getStatus());
                isAdminAvailable.get().setUpdateddate(LocalDateTime.now());
                adminRepository.save(isAdminAvailable.get());
                String body = "Admin is added " + isAdminAvailable.get().getId();
                String subject = "Admin registration complete";
                mailService.send(isAdminAvailable.get().getEmailId(), subject, body);
                return new ResponseClass(200, "Sucessfull", isAdminAvailable.get());
            } else{
                throw new AdminNotFoundException(400, "Admin not avilable");
            }
        }
        throw new AdminNotFoundException(400, "token is wrong");
    }

    @Override
    public List<AdminModel> getAllAdmin(String token) {
        Long userId = tokenUtil.decodeToken(token);
        Optional<AdminModel> isAdminPresent = adminRepository.findById(userId);
        if (isAdminPresent.isPresent()){
            List<AdminModel> getalladmindata = adminRepository.findAll();
            if (getalladmindata.size() > 0) {
                return getalladmindata;
            } else {
                throw new AdminNotFoundException(400, "Admin not Found");
            }
        }
        throw new AdminNotFoundException(400, "Token is wrong");
    }

    @Override
    public ResponseClass getDeleteAdmin(Long id, String token) {
        Long userId = tokenUtil.decodeToken(token);
        Optional<AdminModel> isAdminPresent = adminRepository.findById(userId);
        if (isAdminPresent.isPresent()) {
            Optional<AdminModel> isAdminAvailable = adminRepository.findById(id);
            if(isAdminAvailable.isPresent()){
                adminRepository.save(isAdminPresent.get());
                return new ResponseClass(200, "Sucessfull", isAdminAvailable.get());
            }else{
                throw new AdminNotFoundException(400, "Admin is not present ");
            }
        }
        throw new AdminNotFoundException(400, " Token does not match ");
    }

    @Override
    public Response login(String email, String password) {
        Optional<AdminModel> isEmailPresent = adminRepository.findByEmailId(email);
        if (isEmailPresent.isPresent()) {
            if (isEmailPresent.get().getPassword().equals(password)){
                String token = tokenUtil.createToken(isEmailPresent.get().getId());
                return new Response("Login is sucessfull", 200, token);
            }
            throw new AdminNotFoundException(200, "password incorrect");
        }
        throw new AdminNotFoundException(400, "Not admi is present");
    }

    @Override
    public ResponseClass changePassword(String token, String password) {
        Long id = tokenUtil.decodeToken(token);
        Optional<AdminModel> isIdPresent = adminRepository.findById(id);
        if(isIdPresent.isPresent()){
            isIdPresent.get().setPassword(password);
            adminRepository.save(isIdPresent.get());
            return new ResponseClass(200, "sucessfull" , isIdPresent.get());
        } else {
            throw new AdminNotFoundException(400, "token does not match");
        }
    }

        @Override
        public ResponseClass resetPassword(String emailId) {
            Optional<AdminModel> isEmailPresent = adminRepository.findByEmailId(emailId);
            if(isEmailPresent.isPresent()){
                String token = tokenUtil.createToken(isEmailPresent.get().getId());
                String url = "http://localhost:8083/lmsadmin/changePassword";
                String subject = "reset password";
                String body = "For reset password click on this link" + url + "use this to reset password" + token;
                mailService.send(isEmailPresent.get().getEmailId(), body, subject);
                return new ResponseClass(200, "successful", isEmailPresent.get());
            }
            throw new AdminNotFoundException(400,"token wrong");
        }

    @Override
    public Boolean validate(String token) {
        Long userId = tokenUtil.decodeToken(token);
        Optional<AdminModel> isUserPresent = adminRepository.findById(userId);
        if (isUserPresent.isPresent()){
            return true;
        }else {
            return false;
        }
    }

}

