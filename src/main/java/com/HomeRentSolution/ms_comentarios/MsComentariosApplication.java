package com.HomeRentSolution.ms_comentarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsComentariosApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsComentariosApplication.class, args);
	}

}
