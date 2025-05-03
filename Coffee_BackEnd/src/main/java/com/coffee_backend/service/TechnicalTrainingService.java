package com.coffee_backend.service;

import cn.hutool.core.bean.BeanUtil;
import com.coffee_backend.dto.*;
import com.coffee_backend.entity.*;
import com.coffee_backend.enumType.TrainingStatus;
import com.coffee_backend.enumType.TrainingType;
import com.coffee_backend.repo.TechnicalTrainingRepository;
import com.coffee_backend.repo.TrainingApplicationRepository;
import com.coffee_backend.repo.UserRepository;
import com.coffee_backend.util.UserContext;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TechnicalTrainingService {

    @Autowired
    private TechnicalTrainingRepository technicalTrainingRepository;

    @Autowired
    private TrainingApplicationRepository trainingApplicationRepository;

    @Autowired
    private UserRepository userRepository;


    public ApiResponse saveTraining(TechTrainingRequest request) {
        ApiResponse checkedTraining = checkTraining(request);
        if (checkedTraining != null){
            return checkedTraining;
        }

        TechnicalTraining technicalTraining = BeanUtil.copyProperties(request, TechnicalTraining.class);
        technicalTraining.setType(TrainingType.valueOf(request.getType()));
        technicalTraining.setStatus(TrainingStatus.UPCOMING);
        technicalTraining.setCurrentParticipants(0);

        TechnicalTraining saved = technicalTrainingRepository.save(technicalTraining);

        TechTrainingResponse response = BeanUtil.copyProperties(saved, TechTrainingResponse.class);

        return ApiResponse.success(response);
    }

    private ApiResponse checkTraining(TechTrainingRequest request){
        if (request.getTitle() == null || request.getTitle().isEmpty()) {
            return ApiResponse.error(400, "Title cannot be null or empty");
        }

        if (request.getDescription() == null || request.getDescription().isEmpty()) {
            return ApiResponse.error(400, "Description cannot be null or empty");
        }

        if (request.getStartDate() == null) {
            return ApiResponse.error(400, "Start date cannot be null");
        }

        if (request.getEndDate() == null) {
            return ApiResponse.error(400, "End date cannot be null");
        }

        if (request.getMaxParticipants() == null || request.getMaxParticipants() <= 0) {
            return ApiResponse.error(400, "Max participants must be a positive number");
        }

        if (request.getType() == null || request.getType().isEmpty()) {
            return ApiResponse.error(400, "Training type cannot be null or empty");
        }

        if (request.getLocation() == null || request.getLocation().isEmpty()) {
            return ApiResponse.error(400, "Location cannot be null or empty");
        }

        if (request.getImageUrl() == null || request.getImageUrl().isEmpty()) {
            return ApiResponse.error(400, "Image URL cannot be null or empty");
        }

        return null;
    }

    public ApiResponse updateTrainingStatus(Long id, UpdateTrainingStatusRequest status) {
        Optional<TechnicalTraining> optional = technicalTrainingRepository.findById(id);
        if (optional.isEmpty()){
            return ApiResponse.error(400, "Training not found");
        }

        TechnicalTraining training = optional.get();

        TrainingStatus newStatus = status.getStatus();
        TrainingStatus oldStatus = training.getStatus();

        if (oldStatus == TrainingStatus.COMPLETED || oldStatus == TrainingStatus.CANCELLED) {
            return ApiResponse.error(400, "Cannot modify training in COMPLETED or CANCELLED state");
        }
        training.setStatus(newStatus);

        TechnicalTraining saved = technicalTrainingRepository.save(training);
        TechTrainingResponse response = BeanUtil.copyProperties(saved, TechTrainingResponse.class);

        return ApiResponse.success(response);
    }

    public ApiResponse deleteTechTraining(Long id) {
        Optional<TechnicalTraining> optional = technicalTrainingRepository.findById(id);
        if (optional.isEmpty()){
            return ApiResponse.error(400, "Training not found");
        }

        TechnicalTraining old = optional.get();

        List<TrainingApplication> applications = trainingApplicationRepository.findByTrainingId(id);

        if (!applications.isEmpty()){
            return ApiResponse.error(400, "There are some applications in it, can not delete!");
        }

        technicalTrainingRepository.deleteById(id);

        return ApiResponse.success();
    }

    public ApiResponse getTechTrainingFarmers(Long id) {
        Optional<TechnicalTraining> optional = technicalTrainingRepository.findById(id);
        if (optional.isEmpty()){
            return ApiResponse.error(400, "Training not found");
        }

        TechnicalTraining old = optional.get();

        List<TrainingApplication> applications = trainingApplicationRepository.findByTrainingId(id);

        List<ApplicationQueryResponse> responses = applications.stream().map(application -> ApplicationQueryResponse.builder()
                .applicationId(application.getId())
                .farmerId(application.getFarmer().getId())
                .farmerName(application.getFarmer().getUsername())
                .applicationTime(application.getApplicationTime())
                .build()).toList();

        return ApiResponse.success(responses);
    }


    public ApiResponse getTrainingList(PageQueryRequest request) {
        int page = Optional.ofNullable(request.getPage()).orElse(0);
        int size = Optional.ofNullable(request.getSize()).orElse(10);

        PageRequest pageRequest = PageRequest.of(page, size);

        Specification<TechnicalTraining> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getType() != null && !request.getType().isEmpty()) {

                TrainingType trainingType = TrainingType.valueOf(request.getType());

                predicates.add(cb.equal(root.get("type"), trainingType));
            }

            if (request.getStatus() != null && !request.getStatus().isEmpty()) {

                TrainingStatus trainingStatus = TrainingStatus.valueOf(request.getStatus());

                predicates.add(cb.equal(root.get("status"), trainingStatus));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<TechnicalTraining> trainingPage = technicalTrainingRepository.findAll(spec, pageRequest);

        List<TechTrainingBasicResponse> pageContent = trainingPage.getContent().stream().map(training -> TechTrainingBasicResponse.builder()
                .id(training.getId())
                .title(training.getTitle())
                .status(training.getStatus())
                .startDate(training.getStartDate())
                .location(training.getLocation())
                .imageUrl(training.getImageUrl())
                .build()).toList();

        return ApiResponse.success(
                PageQueryResponse.builder()
                        .content(pageContent)
                        .totalPages(trainingPage.getTotalPages())
                        .totalElements(trainingPage.getTotalElements())
                        .pageNumber(trainingPage.getNumber())
                        .pageSize(trainingPage.getSize())
                        .first(trainingPage.isFirst())
                        .last(trainingPage.isLast())
                        .empty(trainingPage.isEmpty())
                        .build());
    }

    public ApiResponse getTrainingInfo(Long id) {
        Optional<TechnicalTraining> optional = technicalTrainingRepository.findById(id);
        if (optional.isEmpty()){
            return ApiResponse.error(400, "Training not found");
        }

        TechnicalTraining old = optional.get();

        TechTrainingResponse response = BeanUtil.copyProperties(old, TechTrainingResponse.class);

        return ApiResponse.success(response);
    }

    public ApiResponse applyTraining(Long id) {
        // DB needs to save a User object
        Long userId = UserContext.getUser().getId();
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()){
            return ApiResponse.error(404, "User not found");
        }

        Optional<TechnicalTraining> optional = technicalTrainingRepository.findById(id);
        if (optional.isEmpty()){
            return ApiResponse.error(400, "Training not found");
        }


        TechnicalTraining training = optional.get();

        if ((training.getStatus() != TrainingStatus.UPCOMING) && (training.getStatus() != TrainingStatus.ONGOING)){
            return ApiResponse.error(400, "Training can not join");
        }

        TrainingApplication application = new TrainingApplication();
        application.setTraining(training);
        application.setFarmer(user.get());

        TrainingApplication saved = trainingApplicationRepository.save(application);

        ApplicationQueryResponse response = BeanUtil.copyProperties(saved, ApplicationQueryResponse.class);
        response.setApplicationId(saved.getId());
        response.setFarmerId(userId);
        response.setFarmerName(user.get().getUsername());

        return ApiResponse.success(response);
    }
}
