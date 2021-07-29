package com.ssafy.api.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.api.request.UserLoginPostReq;
import com.ssafy.api.request.UserRegisterPostReq;
import com.ssafy.api.response.UserLoginPostRes;
import com.ssafy.api.service.ProfileService;
import com.ssafy.api.service.UserService;
import com.ssafy.common.auth.SsafyUserDetails;
import com.ssafy.common.model.response.BaseResponseBody;
import com.ssafy.common.util.JwtTokenUtil;
import com.ssafy.db.entity.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;
import io.swagger.annotations.ApiResponse;

/**
 * 인증 관련 API 요청 처리를 위한 컨트롤러 정의.
 */
<<<<<<< HEAD
@Api(value = "인증 API", tags = {"Auth."})
=======
@Api(value = "�씤利� API", tags = {"Auth"})
>>>>>>> develop
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	UserService userService;
	
	@Autowired
	ProfileService profileService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@PostMapping("/login")
	@ApiOperation(value = "로그인", notes = "<strong>아이디와 패스워드</strong>를 통해 로그인 한다.") 
    @ApiResponses({
        @ApiResponse(code = 200, message = "성공", response = UserLoginPostRes.class),
        @ApiResponse(code = 401, message = "인증 실패", response = BaseResponseBody.class),
        @ApiResponse(code = 404, message = "사용자 없음", response = BaseResponseBody.class),
        @ApiResponse(code = 500, message = "서버 오류", response = BaseResponseBody.class)
    })
	public ResponseEntity<UserLoginPostRes> login(@RequestBody @ApiParam(value="로그인 정보", required = true) UserLoginPostReq loginInfo) {
		String email = loginInfo.getEmail();
		String password = loginInfo.getPassword();
		
		//이메일로 수정
		User user = userService.getUserByEmail(email);
<<<<<<< HEAD
		// 로그인 요청한 유저로부터 입력된 패스워드 와 디비에 저장된 유저의 암호화된 패스워드가 같은지 확인.(유효한 패스워드인지 여부 확인)
=======
		Profile profile = profileService.getProfileByUserId(user.getUserId());
		/*
		 * �깉�눜�븳 �쉶�썝(user status 媛� 1�씪�븣)�씤 寃쎌슦 濡쒓렇�씤 �뿉�윭
		 */
		if(user.getUserStatus() == 1)
			return ResponseEntity.status(401).body(UserLoginPostRes.of(401, "�깉�눜�븳 �쉶�썝�엯�땲�떎.", null));

		// 濡쒓렇�씤 �슂泥��븳 �쑀���濡쒕���꽣 �엯�젰�맂 �뙣�뒪�썙�뱶 ��� �뵒鍮꾩뿉 ����옣�맂 �쑀����쓽 �븫�샇�솕�맂 �뙣�뒪�썙�뱶媛� 媛숈��吏� �솗�씤.(�쑀�슚�븳 �뙣�뒪�썙�뱶�씤吏� �뿬遺� �솗�씤)
>>>>>>> develop
		if(passwordEncoder.matches(password, user.getPassword())) {
			// 유효한 패스워드가 맞는 경우, 로그인 성공으로 응답.(액세스 토큰을 포함하여 응답값 전달)
			return ResponseEntity.ok(UserLoginPostRes.of(200, "Success", JwtTokenUtil.getToken(email)));
		}
		// 유효하지 않는 패스워드인 경우, 로그인 실패로 응답.
		return ResponseEntity.status(401).body(UserLoginPostRes.of(401, "Invalid Password", null));
	}
	
	@PostMapping("/signup")
	@ApiOperation(value = "회원 가입", notes = "<strong>아이디와 패스워드</strong>를 통해 회원가입 한다.") 
    @ApiResponses({
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 401, message = "인증 실패"),
        @ApiResponse(code = 404, message = "사용자 없음"),
        @ApiResponse(code = 500, message = "서버 오류")
    })
	public ResponseEntity<? extends BaseResponseBody> register(
			@RequestBody @ApiParam(value="회원가입 정보", required = true) UserRegisterPostReq registerInfo) {
		
		//임의로 리턴된 User 인스턴스. 현재 코드는 회원 가입 성공 여부만 판단하기 때문에 굳이 Insert 된 유저 정보를 응답하지 않음.
		User user = userService.createUser(registerInfo);
		
		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
	}
	
	/*
	 * 로그아웃 구현
	 * 
	 */
//	@GetMapping("/logout")
//	@ApiOperation(value = "이메일 중복 확인", notes = "회원가입 중 이메일 중복확인") 
//    @ApiResponses({
//        @ApiResponse(code = 200, message = "성공"),
//        @ApiResponse(code = 401, message = "인증 실패"),
//        @ApiResponse(code = 404, message = "사용자 없음"),
//        @ApiResponse(code = 500, message = "서버 오류")
//    })
//	public void logout(HttpServletRequest request) {
//        UserLoginPostRes.of(200, "success", null);
//    }	
//	
	/*
	 * 이메일 중복 확인
	 * 
	 */
	@GetMapping("/checkEmail/{email}")
	@ApiOperation(value = "이메일 중복 확인", notes = "회원가입 중 이메일 중복확인") 
    @ApiResponses({
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 401, message = "인증 실패"),
        @ApiResponse(code = 404, message = "사용자 없음"),
        @ApiResponse(code = 500, message = "서버 오류")
    })
	public ResponseEntity<Boolean> checkEmail(@PathVariable String email) {
		System.out.println(email);
		return ResponseEntity.ok(userService.checkEmail(email));
	}
	
	/*
	 * 유저네임 중복 확인
	 * 
	 */
	@GetMapping("/checkNickname/{nickname}")
	@ApiOperation(value = "닉네임 중복 확인", notes = "회원가입 중 닉네임 중복확인") 
    @ApiResponses({
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 401, message = "인증 실패"),
        @ApiResponse(code = 404, message = "사용자 없음"),
        @ApiResponse(code = 500, message = "서버 오류")
    })
	public ResponseEntity<Boolean> checkUsername(@PathVariable String nickname) {
		return ResponseEntity.ok(profileService.checkName(nickname));
	}
	
	/*
	 * 비밀번호 수정	=> 이메일 인증 후에.
	 * 
	 */
//	@PutMapping("/password")
//	@ApiOperation(value = "비밀번호 수정", notes = "현재 비밀번호를 수정합니다.") 
//    @ApiResponses({
//        @ApiResponse(code = 200, message = "성공"),
//        @ApiResponse(code = 401, message = "인증 실패"),
//        @ApiResponse(code = 404, message = "사용자 없음"),
//        @ApiResponse(code = 500, message = "서버 오류")
//    })
//	public ResponseEntity<Boolean> modifyPassword(@PathVariable String newPassword){
//		User user = userService.getU
//	}
	
	/*	
	 * 회원삭제	=> profile에서 사용되는 user_status 변경
	 * 
	 */
	@PutMapping("/delete")
	@ApiOperation(value = "회원 탈퇴 처리", notes = "회원의 상태를 탈퇴상태로 변경합니다.") 
    @ApiResponses({
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 401, message = "인증 실패"),
        @ApiResponse(code = 404, message = "사용자 없음"),
        @ApiResponse(code = 500, message = "서버 오류")
    })
	public ResponseEntity<Boolean> changeStatus(@ApiIgnore Authentication authentication, @RequestParam String password){
		SsafyUserDetails userDetails = (SsafyUserDetails) authentication.getDetails();
		String email = userDetails.getEmail();
		String userPw = userDetails.getPassword();
		
//		if(email.equals(password))
//			return ResponseEntity.ok(userService.changeStatus(email, userPw);)
//		else
//			return ResponseEntity.ok(userService.changeStatus(email, userPw);)
//			
		return ResponseEntity.ok(true);
		//내 정보에서 가져온 비밀번호가 인코딩 되어있을 경우
		/*
		 * email.equals(passwordEncoder.encode(password))
		 */
		
//		boolean result = userService.checkPw(password);
//		if(result) {
//			userService.changeStatus(u)
//		}
		//return null;
	}
}
