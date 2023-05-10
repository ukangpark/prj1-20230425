package com.example.demo.domain;

import java.util.*;

import lombok.*;

@Data
public class Member {

	private String id;
	private String password;
	private String email;
	private String nickName;
	private Date inserted;
	private List<String> authority;
}
