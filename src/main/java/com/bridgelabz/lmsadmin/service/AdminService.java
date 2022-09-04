package com.bridgelabz.lmsadmin.service;

import com.bridgelabz.lmsadmin.dto.AdminDTO;
import com.bridgelabz.lmsadmin.exception.AdminNotFoundException;
import com.bridgelabz.lmsadmin.model.AdminModel;
import com.bridgelabz.lmsadmin.repository.AdminRepository;
import com.bridgelabz.lmsadmin.util.Response;
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
    public AdminModel addAdmin(AdminDTO adminDTO) {
        AdminModel adminModel = new AdminModel(adminDTO);
        adminModel.setRegistereddate(LocalDateTime.now());
        adminRepository.save(adminModel);
        String body = "admin has been added sucessfully " + adminModel.getId();
        String subject = "admin registration completed";
        mailService.send(adminModel.getEmailId(),body, subject);
        return adminModel;
    }

    @Override
    public AdminModel updateAdmin(String token, AdminDTO adminDTO, long id) {
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
                adminRepository.save(isAdminPresent.get());
                String body = "Admin is added " + isAdminPresent.get().getId();
                String subject = "Admin registration complete";
                mailService.send(isAdminPresent.get().getEmailId(), subject, body);
                return isAdminAvailable.get();
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
        throw new AdminNotFoundException(400, "Token is wromg");
    }

    @Override
    public AdminModel getDeleteAdmin(Long id, String token) {
        Long userId = tokenUtil.decodeToken(token);
        Optional<AdminModel> isAdminPresent = adminRepository.findById(userId);
        if (isAdminPresent.isPresent()) {
            Optional<AdminModel> isAdminPresents = adminRepository.findById(id);
            if(isAdminPresents.isPresent()){
                adminRepository.save(isAdminPresent.get());
                return isAdminPresent.get();
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
    public AdminModel changePassword(String token, String password) {
        Long id = tokenUtil.decodeToken(token);
        Optional<AdminModel> isIdPresent = adminRepository.findById(id);
        if(isIdPresent.isPresent()){
            isIdPresent.get().setPassword(password);
            adminRepository.save(isIdPresent.get());
            return isIdPresent.get();
        } else {
            throw new AdminNotFoundException(400, "token does not match");
        }
    }

        @Override
        public AdminModel resetPassword(String emailId) {
            Optional<AdminModel> isEmailPresent = adminRepository.findByEmailId(emailId);
            if(isEmailPresent.isPresent()){
                String token = tokenUtil.createToken(isEmailPresent.get().getId());
                String url = "http://localhost:8085/lmsproject/changePassword";
                String subject = "reset password";
                String body = "For reset password click on this link" + url + "use this to reset password" + token;
                mailService.send(isEmailPresent.get().getEmailId(), body, subject);
                return isEmailPresent.get();
            }
            throw new AdminNotFoundException(400,"token wrong");
        }

}

