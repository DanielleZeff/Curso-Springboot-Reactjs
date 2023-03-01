package com.danielle.minhasfinancas.model.repository;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.danielle.minhasfinancas.model.entity.Usuario;


@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioRepositoryTest {

	@Autowired
	UsuarioRepository repository;
	
	@Test
	public void deveVerificarAExistenciaDeUmEmail() {
		
		//cenario - criar o usuario e salvar na base de dados
		Usuario usuario = Usuario.builder().nome("usuario").email("usuario@email.com").build();
		repository.save(usuario);
		
		// ação/excecução - verrificar se existe um usuario na base de dados
		boolean result = repository.existsByEmail("usuario@email.com");
		
		//verificação - verificar se o resultado é verdadeiro,
		//caso venha falso o teste falhou
		Assertions.assertThat(result).isTrue();
	}
}
