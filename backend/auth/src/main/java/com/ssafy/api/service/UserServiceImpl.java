package com.ssafy.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ssafy.api.request.UserRegisterPostReq;
import com.ssafy.db.entity.Profile;
import com.ssafy.db.entity.User;
import com.ssafy.db.repository.ProfileRepository;
import com.ssafy.db.repository.ProfileRepositorySupport;
import com.ssafy.db.repository.UserRepository;
import com.ssafy.db.repository.UserRepositorySupport;

/**
 *	유저 관련 비즈니스 로직 처리를 위한 서비스 구현 정의.
 */
@Service("userService")
public class UserServiceImpl implements UserService {
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ProfileRepository profileRepository;

	@Autowired
	UserRepositorySupport userRepositorySupport;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Override
	public User createUser(UserRegisterPostReq userRegisterInfo) {
		User user = new User();
		/*
		 * userId 암호화 셋팅
		 */
		String userId = ""; 
		user.setUserId(userId);
		user.setEmail(userRegisterInfo.getEmail());
		// 보안을 위해서 유저 패스워드 암호화 하여 디비에 저장.
		user.setPassword(passwordEncoder.encode(userRegisterInfo.getPassword()));
		user.setUsername(userRegisterInfo.getUsername());
		
		Profile profile = new Profile();
		profile.setUserId(userId);
		profile.setNickname(userRegisterInfo.getNickname());
		profile.setPhoneNum(userRegisterInfo.getPhoneNum());
		userRepository.save(user);
		profileRepository.save(profile);
		return user;
	}

	@Override
	public User getUserByEmail(String email) {
		// 디비에 유저 정보 조회 (userId 를 통한 조회).
		User user = userRepositorySupport.findUserByEmail(email).get();
		return user;
	}

	@Override
	public boolean checkEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	@Override
	public boolean checkPw(String email, String password) {
		//return userRepository.
		return true;
	}

	@Override
	public boolean changeStatus(String email, String password) {
		
		return false;
	}
}
