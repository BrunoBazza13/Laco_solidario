package com.lacossolidario.doacao;

import java.io.UnsupportedEncodingException;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class DoacaoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DoacaoApplication.class, args);
	}

	
	@PostMapping("jwt")
	public String decodeToken(String token) throws UnsupportedEncodingException {
		//header.playload.signingkey
		System.out.println(token);
		
		String payload = token.split("\\.")[1];
		
		return new String(Base64.decodeBase64(payload), "UTF-8");
	}
	
}
