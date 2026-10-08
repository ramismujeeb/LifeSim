package com.lifesim.controller;
import com.lifesim.dto.FinancialProfileDto;
import com.lifesim.model.FinancialProfile;
import com.lifesim.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    @Autowired ProfileService profileService;

    @GetMapping
    public ResponseEntity<?> getProfile(Authentication authentication) {
        FinancialProfile profile = profileService.getProfile(authentication.getName());
        if (profile == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(profile);
    }

    @PostMapping
    public ResponseEntity<?> createProfile(Authentication authentication, @RequestBody FinancialProfileDto dto) {
        return ResponseEntity.ok(profileService.saveOrUpdateProfile(authentication.getName(), dto));
    }

    @PutMapping
    public ResponseEntity<?> updateProfile(Authentication authentication, @RequestBody FinancialProfileDto dto) {
        return ResponseEntity.ok(profileService.saveOrUpdateProfile(authentication.getName(), dto));
    }
}