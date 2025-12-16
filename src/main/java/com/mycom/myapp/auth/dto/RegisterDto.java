package com.mycom.myapp.auth.dto;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Getter
@Setter
public class RegisterDto {

	@Pattern(
			regexp = "^[a-zA-Z0-9!@#]{8,20}$",
	        message = "아이디는 영문 소문자, 숫자, !@#만 사용 가능하며 8~20자여야 합니다."
	)
    private String username;
	@Pattern(
			regexp = "^[a-zA-Z0-9!@#]{8,20}$",
	        message = "비밀번호는 영문 소문자, 숫자, !@#만 사용 가능하며 8~20자여야 합니다."
	)
	@Size( min=8,max=20,message="비밀번호는 8~20자여야 합니다.")
    private String password;
    private String nickname;
}
