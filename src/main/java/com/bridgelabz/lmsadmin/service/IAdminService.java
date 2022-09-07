package com.bridgelabz.lmsadmin.service;

import com.bridgelabz.lmsadmin.dto.AdminDTO;
import com.bridgelabz.lmsadmin.model.AdminModel;
import com.bridgelabz.lmsadmin.util.Response;
import com.bridgelabz.lmsadmin.util.ResponseClass;

import java.util.List;

public interface IAdminService {

    ResponseClass addAdmin(AdminDTO adminDTO);

    ResponseClass updateAdmin(String token, AdminDTO adminDTO, long id);

    List<AdminModel> getAllAdmin(String token);

    ResponseClass getDeleteAdmin(Long id, String token);

    Response login(String email, String password);

    ResponseClass changePassword(String token, String password);

    ResponseClass resetPassword(String emailId);

    Boolean validate(String token);
}
