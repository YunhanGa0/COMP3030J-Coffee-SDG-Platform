package com.coffee_backend.service;

import cn.hutool.core.bean.BeanUtil;
import com.coffee_backend.dto.*;
import com.coffee_backend.entity.Farm;
import com.coffee_backend.entity.User;
import com.coffee_backend.repo.FarmRepository;
import com.coffee_backend.repo.UserRepository;
import com.coffee_backend.util.UserContext;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FarmService {

    @Autowired
    private FarmRepository farmRepository;

    @Autowired
    private UserRepository userRepository;


    public PageQueryResponse list(PageQueryRequest pageQueryRequest) {
        PageRequest pageRequest = PageRequest.of(pageQueryRequest.getPage(), pageQueryRequest.getSize());

        Specification<Farm> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(pageQueryRequest.getCountry())) {
                predicates.add(cb.equal(root.get("country"), pageQueryRequest.getCountry()));
            }

            if (pageQueryRequest.getCertified() != null) {
                predicates.add(cb.equal(root.get("isCertificated"), pageQueryRequest.getCertified()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Farm> all = farmRepository.findAll(spec, pageRequest);

        List<FarmResponse> farmResponseStream = all.getContent().stream().map(farm -> {
            FarmResponse farmResponse = BeanUtil.copyProperties(farm, FarmResponse.class);
            farmResponse.setUserId(farm.getUser().getId());

            if (farm.getIsCertificated() == null){
                farmResponse.setIsCertificated(false);
            }

            return farmResponse;
        }).collect(Collectors.toList());

        return PageQueryResponse.builder()
                .content(farmResponseStream)
                .totalPages(all.getTotalPages())
                .totalElements(all.getTotalElements())
                .pageNumber(all.getNumber())
                .pageSize(all.getSize())
                .first(all.isFirst())
                .last(all.isLast())
                .empty(all.isEmpty())
                .build();
    }

    public ApiResponse getById(Long id) {
        Optional<Farm> farm = farmRepository.findById(id);

        if (farm.isPresent()){
            FarmFullResponse farmResponse = BeanUtil.copyProperties(farm.get(), FarmFullResponse.class);

            UserDTO userDTO = new UserDTO();
            userDTO.setId(farm.get().getUser().getId());
            userDTO.setUsername(farm.get().getUser().getUsername());

            farmResponse.setUser(userDTO);

            if (farm.get().getIsCertificated() == null){
                farmResponse.setIsCertificated(false);
            }


            return ApiResponse.success(farmResponse);
        }else{
            return ApiResponse.error(404, "Farm not found");
        }
    }

    public ApiResponse saveProfile(SaveProfileRequest saveProfileRequest) {
        String result = validateFarmProfile(saveProfileRequest);

        if (result != null){
            return ApiResponse.error(404, result);
        }

        // DB needs to save a User object
        Long userId = UserContext.getUser().getId();
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()){
            return ApiResponse.error(404, "User not found");
        }

        Farm farm = new Farm();
        farm.setUser(user.get());
        BeanUtils.copyProperties(saveProfileRequest, farm);

        Farm saved = farmRepository.save(farm);

        SaveProfileResponse response = SaveProfileResponse.builder()
                .userId(userId)
                .id(saved.getId())
                .build();

        return ApiResponse.success(response);
    }

    public String validateFarmProfile(SaveProfileRequest request) {
        if (request.getFarmName() == null || request.getFarmName().trim().isEmpty()) {
            return "Farm name should not be null or empty";
        }

        if (request.getCountry() == null || request.getCountry().trim().isEmpty()) {
            return "Country should not be null or empty";
        }

        if (request.getLocation() == null || request.getLocation().trim().isEmpty()) {
            return "Location should not be null or empty";
        }

        if (request.getSize() == null || request.getSize() <= 0) {
            return "Size should be a positive number";
        }

        if (request.getEstablishedYear() == null || request.getEstablishedYear() < 1000 || request.getEstablishedYear() > 9999) {
            return "Established year should be a valid year between 1000 and 9999";
        }

        if (request.getDescription() == null || request.getDescription().trim().isEmpty()) {
            return "Description should not be null or empty";
        }

        if (request.getImageUrl() == null || request.getImageUrl().trim().isEmpty()) {
            return "Image URL should not be null or empty";
        }

        if (request.getElevation() == null || request.getElevation() < 0) {
            return "Elevation should be a non-negative number";
        }

        if (request.getSoilType() == null) {
            return "Soil type should not be null";
        }

        return null;
    }

    public ApiResponse updateProfile(SaveProfileRequest saveProfileRequest) {
        String result = validateFarmProfile(saveProfileRequest);

        if (result != null){
            return ApiResponse.error(404, result);
        }

        // DB needs to save a User object
        Long userId = UserContext.getUser().getId();
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()){
            return ApiResponse.error(404, "User not found");
        }

        Optional<Farm> oldOpt = farmRepository.findByUserId(userId);

        if (oldOpt.isEmpty()){
            return ApiResponse.error(404, "Farm not found");
        }

        Farm oldFarm = oldOpt.get();
        BeanUtils.copyProperties(saveProfileRequest, oldFarm);

        Farm newFarm = farmRepository.save(oldFarm);
        UpdateProfileResponse response = new UpdateProfileResponse();

        BeanUtils.copyProperties(newFarm, response);
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        userDTO.setUsername(user.get().getUsername());

        response.setUser(userDTO);

        return ApiResponse.success(response);
    }

    public ApiResponse queryStatus() {
        Long userId = UserContext.getUser().getId();

        Optional<Farm> oldOpt = farmRepository.findByUserId(userId);

        FarmStatusResponse response = new FarmStatusResponse();
        boolean result = oldOpt.isPresent();
        response.setHasCompletedProfile(result);

        return ApiResponse.success(response);
    }
}
