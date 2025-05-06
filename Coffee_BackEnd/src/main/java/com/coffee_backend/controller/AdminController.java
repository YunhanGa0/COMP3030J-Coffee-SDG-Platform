package com.coffee_backend.controller;

import com.coffee_backend.dto.*;
import com.coffee_backend.enumType.ApplicationStatus;
import com.coffee_backend.enumType.CertificationStatus;
import com.coffee_backend.enumType.TrainingStatus;
import com.coffee_backend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private TechnicalTrainingService technicalTrainingService;

    @Autowired
    private FinancialService financialService;

    @Autowired
    private CertificationService certificationService;

    // 管理员创建农庄账户
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/createFarmer")
    public ApiResponse saveFarmer(@RequestBody SaveFarmerRequest request){
        return adminService.saveFarmer(request);
    }

    // 管理员获取所有农庄账户
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/farmers")
    public ApiResponse getFarmers() {
        return adminService.getAllFarmers();
    }

    // 管理员创建技术培训
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/trainings")
    public ApiResponse saveTechTraining(@RequestBody TechTrainingRequest request){
        return technicalTrainingService.saveTraining(request);
    }

    // 改变技术培训状态
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/trainings/update/{id}")
    public ApiResponse updateTrainingStatus(@PathVariable Long id, @RequestBody UpdateTrainingStatusRequest status){
        return technicalTrainingService.updateTrainingStatus(id, status);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("/trainings/{id}")
    public ApiResponse deleteTechTraining(@PathVariable Long id){
        return technicalTrainingService.deleteTechTraining(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/trainings/{id}/applications")
    public ApiResponse getTechTrainingFarmers(@PathVariable Long id){
        return technicalTrainingService.getTechTrainingFarmers(id);
    }

    // 管理员创建财务支持项目
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/financial-supports")
    public ApiResponse createFinancialSupport(@RequestBody FinancialSupportRequest request){
        return financialService.createFinancialSupport(request);
    }

    // 管理员查询财务支持项目
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/financial-applications")
    public ApiResponse queryFinancialSupport(@RequestParam(name = "status", required = false) ApplicationStatus status){
        return financialService.queryFinancialSupport(status);
    }

    // 管理员审核财务申请
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/financial-applications/review/{id}")
    public ApiResponse reviewFinancialApplication(@PathVariable Long id, @RequestBody FinancialReviewRequest request){
        return financialService.reviewFinancialApplication(id, request);
    }

    // 管理员查询认证申请
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/certification/applications")
    public ApiResponse queryCertificationApplication(@RequestParam(name = "status", required = false) CertificationStatus status){
        return certificationService.queryCertificationApplication(status);
    }

    // 管理员审核认证申请
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/certification/applications/review/{id}")
    public ApiResponse reviewCertificationApplication(@PathVariable Long id, @RequestBody CertificationReviewRequest request){
        return certificationService.reviewCertificationApplication(id, request);
    }

    // 管理员查询CUSTOMER数量
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/queryCustomer")
    public ApiResponse queryCustomer(){
        return adminService.queryCustomer();
    }


}
