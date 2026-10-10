package com.nutri_track.backend.controller;

import com.nutri_track.backend.entity.Account;
import com.nutri_track.backend.entity.UserProfile;
import com.nutri_track.backend.repository.AccountRepository;
import com.nutri_track.backend.repository.UserProfileRepository;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/profile")
public class UserProfileController {

    private final UserProfileRepository profileRepository;
    private final AccountRepository accountRepository;

    public UserProfileController(
            UserProfileRepository profileRepository,
            AccountRepository accountRepository) {
        this.profileRepository = profileRepository;
        this.accountRepository = accountRepository;
    }

    public record NutritionData(
            Double targetCalories,
            Double protein,
            Double carbohydrates,
            Double fat
    ) {}

    public record ProfileData(
            Integer age,
            String sex,
            Double height,
            Double weight,
            String dietPreference,
            String goal,
            NutritionData nutrition
    ) {}

    private Long getAccountId(Jwt jwt) {
        try {
            return Long.parseLong(jwt.getSubject());
        } catch (NumberFormatException exception) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Invalid account identity"
            );
        }
    }

    @GetMapping
    @Transactional(readOnly = true)
    public ProfileData getProfile(
            @AuthenticationPrincipal Jwt jwt) {

        Long accountId = getAccountId(jwt);

        UserProfile profile = profileRepository
            .findByAccount_Id(accountId)
            .orElse(null);

        if (profile == null) {
            return new ProfileData(
                null, null, null, null, null, null, null
            );
        }

        NutritionData nutrition = null;

        if (profile.getTargetCalories() != null) {
            nutrition = new NutritionData(
                profile.getTargetCalories(),
                profile.getProtein(),
                profile.getCarbohydrates(),
                profile.getFat()
            );
        }

        return new ProfileData(
            profile.getAge(),
            profile.getSex(),
            profile.getHeight(),
            profile.getWeight(),
            profile.getDietPreference(),
            profile.getGoal(),
            nutrition
        );
    }

    @PutMapping
    @Transactional
    public ProfileData saveProfile(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody ProfileData request) {

        Long accountId = getAccountId(jwt);

        Account account = accountRepository
            .findById(accountId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Account not found"
            ));

        UserProfile profile = profileRepository
            .findByAccount_Id(accountId)
            .orElseGet(() -> new UserProfile(account));

        if (request.age() != null) {
            profile.setAge(request.age());
        }

        if (request.sex() != null) {
            profile.setSex(request.sex());
        }

        if (request.height() != null) {
            profile.setHeight(request.height());
        }

        if (request.weight() != null) {
            profile.setWeight(request.weight());
        }

        if (request.dietPreference() != null) {
            profile.setDietPreference(request.dietPreference());
        }

        if (request.goal() != null) {
            profile.setGoal(request.goal());
        }

        if (request.nutrition() != null) {
            NutritionData nutrition = request.nutrition();

            if (nutrition.targetCalories() != null) {
                profile.setTargetCalories(
                    nutrition.targetCalories()
                );
            }

            if (nutrition.protein() != null) {
                profile.setProtein(nutrition.protein());
            }

            if (nutrition.carbohydrates() != null) {
                profile.setCarbohydrates(
                    nutrition.carbohydrates()
                );
            }

            if (nutrition.fat() != null) {
                profile.setFat(nutrition.fat());
            }
        }

        profileRepository.save(profile);

        NutritionData savedNutrition = null;

        if (profile.getTargetCalories() != null) {
            savedNutrition = new NutritionData(
                profile.getTargetCalories(),
                profile.getProtein(),
                profile.getCarbohydrates(),
                profile.getFat()
            );
        }

        return new ProfileData(
            profile.getAge(),
            profile.getSex(),
            profile.getHeight(),
            profile.getWeight(),
            profile.getDietPreference(),
            profile.getGoal(),
            savedNutrition
        );
    }
}
