package com.coffee_backend.service;

import cn.hutool.core.bean.BeanUtil;
import com.coffee_backend.dto.*;
import com.coffee_backend.entity.CertificationApplication;
import com.coffee_backend.entity.FinancialApplication;
import com.coffee_backend.entity.FinancialSupport;
import com.coffee_backend.entity.User;
import com.coffee_backend.enumType.ApplicationStatus;
import com.coffee_backend.enumType.UserRole;
import com.coffee_backend.repo.FinancialApplicationRepository;
import com.coffee_backend.repo.FinancialSupportRepository;
import com.coffee_backend.repo.UserRepository;
import com.coffee_backend.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class FinancialService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FinancialSupportRepository financialSupportRepository;

    @Autowired
    private FinancialApplicationRepository financialApplicationRepository;

    public ApiResponse createFinancialSupport(FinancialSupportRequest request) {

        if (UserContext.getUser().getRole() != UserRole.ADMIN){
            return ApiResponse.error(403, "You can not do this");
        }

        // Validate the request fields
        if (request == null) {
            return ApiResponse.error(400, "Request is null");
        }

        // Check if title is null or empty
        if (request.getTitle() == null || request.getTitle().isEmpty()) {
            return ApiResponse.error(400, "Title is required");
        }

        // Check if budget is null or zero
        if (request.getBudget() == null || request.getBudget().compareTo(BigDecimal.ZERO) <= 0) {
            return ApiResponse.error(400, "Budget must be a positive value");
        }

        // Check if description is null or empty
        if (request.getDescription() == null || request.getDescription().isEmpty()) {
            return ApiResponse.error(400, "Description is required");
        }

        FinancialSupport financialSupport = BeanUtil.copyProperties(request, FinancialSupport.class);

        FinancialSupport saved = financialSupportRepository.save(financialSupport);

        return ApiResponse.created(saved);
    }

    public ApiResponse listSupports() {
        return ApiResponse.success(financialSupportRepository.findAll());
    }

    public ApiResponse farmApplyFinancial(FinancialApplicationRequest request) {
        Long financialSupportId = request.getFinancialSupportId();
        Optional<FinancialSupport> optional = financialSupportRepository.findById(financialSupportId);
        if (optional.isEmpty()){
            return ApiResponse.error(400, "Support not found");
        }

        Optional<FinancialApplication> repeat = financialApplicationRepository.findByFarmerId(UserContext.getUser().getId());
        if (repeat.isPresent()){
            return ApiResponse.error(400, "Support can not be applied, because you already applied it!");
        }

        FinancialSupport financialSupport = optional.get();

        // just ignore the warning
        User user = userRepository.findById(UserContext.getUser().getId()).get();

        FinancialApplication financialApplication = new FinancialApplication();
        financialApplication.setFinancialSupport(financialSupport);
        financialApplication.setFarmer(user);
        financialApplication.setPurpose(request.getPurpose());

        FinancialApplication saved = financialApplicationRepository.save(financialApplication);

        return ApiResponse.created(null);
    }

    public ApiResponse farmQueryFinancial() {
        Optional<FinancialApplication> optional = financialApplicationRepository.findByFarmerId(UserContext.getUser().getId());
        if (optional.isEmpty()){
            return ApiResponse.error(404, "You not application it");
        }

        FinancialApplicationResponse response = BeanUtil.copyProperties(optional.get(), FinancialApplicationResponse.class);

        return ApiResponse.success(response);
    }

    public ApiResponse queryAllFinancialSupport() {
        List<FinancialApplication> optional = financialApplicationRepository.findByStatus(ApplicationStatus.PENDING);
        List<FinancialApplicationResponse> responses = optional.stream().map(financialApplication -> BeanUtil.copyProperties(financialApplication, FinancialApplicationResponse.class)).toList();
        return ApiResponse.success(responses);
    }


    public ApiResponse reviewFinancialSupport(Long id) {
        Optional<FinancialApplication> optional = financialApplicationRepository.findById(id);
        if (optional.isEmpty()){
            return ApiResponse.error(404, "There is not has application with id: " + id);
        }

        FinancialApplication old = optional.get();
        old.setStatus(ApplicationStatus.APPROVED);
        FinancialApplication saved = financialApplicationRepository.save(old);

        return ApiResponse.success(BeanUtil.copyProperties(saved, FinancialApplicationResponse.class));
    }

}
