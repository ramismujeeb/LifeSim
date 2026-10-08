package com.lifesim.service;
import com.lifesim.dto.FinancialProfileDto;
import com.lifesim.model.FinancialProfile;
import com.lifesim.model.User;
import com.lifesim.repository.FinancialProfileRepository;
import com.lifesim.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.BeanUtils;

@Service
public class ProfileService {
    @Autowired FinancialProfileRepository profileRepository;
    @Autowired UserRepository userRepository;

    public FinancialProfile getProfile(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        return profileRepository.findByUserId(user.getId()).orElse(null);
    }

    public FinancialProfile saveOrUpdateProfile(String email, FinancialProfileDto dto) {
        User user = userRepository.findByEmail(email).orElseThrow();
        FinancialProfile profile = profileRepository.findByUserId(user.getId()).orElse(new FinancialProfile());
        profile.setUser(user);
        BeanUtils.copyProperties(dto, profile);
        return profileRepository.save(profile);
    }
}