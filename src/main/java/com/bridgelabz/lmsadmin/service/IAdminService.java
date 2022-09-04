package com.bridgelabz.lmsadmin.service;

import com.bridgelabz.lmsadmin.dto.AdminDTO;
import com.bridgelabz.lmsadmin.model.AdminModel;
import com.bridgelabz.lmsadmin.util.Response;

import java.util.List;

public interface IAdminService {

    AdminModel addAdmin(AdminDTO adminDTO);

    AdminModel updateAdmin(String token, AdminDTO adminDTO, long id);

    List<AdminModel> getAllAdmin(String token);

    AdminModel getDeleteAdmin(Long id, String token);

    Response login(String email, String password);

    AdminModel changePassword(String token, String password);

    AdminModel resetPassword(String emailId);
}
