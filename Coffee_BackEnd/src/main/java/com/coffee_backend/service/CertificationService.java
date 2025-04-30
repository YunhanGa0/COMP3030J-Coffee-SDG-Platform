package com.coffee_backend.service;

import cn.hutool.core.bean.BeanUtil;
import com.coffee_backend.dto.*;
import com.coffee_backend.entity.CertificationApplication;
import com.coffee_backend.entity.Farm;
import com.coffee_backend.entity.FinancialApplication;
import com.coffee_backend.enumType.ApplicationStatus;
import com.coffee_backend.enumType.CertificationStatus;
import com.coffee_backend.repo.CertificationRepository;
import com.coffee_backend.repo.FarmRepository;
import com.coffee_backend.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.AbstractCachingViewResolver;

import java.util.List;
import java.util.Optional;

@Service
public class CertificationService {

    @Autowired
    private FarmRepository farmRepository;

    @Autowired
    private CertificationRepository certificationRepository;

    public ApiResponse applicationCertification(CertificationApplicationRequest request) {
        if (request == null) {
            return ApiResponse.error(400, "Request is null");
        }

        // Check if desc is null or empty
        if (request.getDescription() == null || request.getDescription().isEmpty()) {
            return ApiResponse.error(400, "Description is required");
        }

        Optional<Farm> optionalFarm = farmRepository.findByUserId(UserContext.getUser().getId());

        CertificationApplication certificationApplication = new CertificationApplication();
        certificationApplication.setDescription(request.getDescription());
        certificationApplication.setFarm(optionalFarm.get());

        CertificationApplication saved = certificationRepository.save(certificationApplication);

        return ApiResponse.success();
    }

    public ApiResponse queryApplicationCertification() {
        Long userId = UserContext.getUser().getId();
        Optional<Farm> optionalFarm = farmRepository.findByUserId(userId);
        Farm farm = optionalFarm.get();
        Optional<CertificationApplication> optional = certificationRepository.findByFarmId(farm.getId());
        if (optional.isEmpty()){
            return ApiResponse.error(404, "You not apply it!");
        }

        CertificationApplicationResponse response = BeanUtil.copyProperties(optional.get(), CertificationApplicationResponse.class);
        response.setFarmId(farm.getId());
        response.setAdminFeedback((response.getAdminFeedback()) == null ? "" : response.getAdminFeedback());
        return ApiResponse.success(response);
    }

    public ApiResponse queryCertificationApplication(CertificationStatus status) {
        List<CertificationApplication> applications;

        if (status == null) {
            applications = certificationRepository.findAll();
        } else {
            applications = certificationRepository.findByStatus(status);
        }
        List<CertificationApplicationResponse> responses = applications.stream().map(app -> {
            CertificationApplicationResponse response = new CertificationApplicationResponse();
            response.setId(app.getId());
            response.setFarmId(app.getFarm().getId());
            response.setDescription(app.getDescription());
            response.setStatus(app.getStatus());
            response.setAdminFeedback(app.getAdminFeedback() == null ? "" : app.getAdminFeedback());
            response.setSubmitTime(app.getSubmitTime());
            return response;
        }).toList();

        return ApiResponse.success(responses);
    }

    public ApiResponse reviewCertificationApplication(Long id, CertificationReviewRequest request) {
        Optional<CertificationApplication> optional = certificationRepository.findById(id);
        if (optional.isEmpty()){
            return ApiResponse.error(404, "There is not has application with id: " + id);
        }

        CertificationApplication old = optional.get();
        old.setStatus(request.getStatus());
        old.setAdminFeedback(request.getAdminFeedback());
        CertificationApplication saved = certificationRepository.save(old);
        CertificationApplicationResponse response = CertificationApplicationResponse.builder()
                .adminFeedback(saved.getAdminFeedback() == null ? "" : saved.getAdminFeedback())
                .status(saved.getStatus())
                .build();
        return ApiResponse.success(response);
    }
}
