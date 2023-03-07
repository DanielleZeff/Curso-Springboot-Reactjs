package com.danielle.minhasfinancas.model.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.danielle.minhasfinancas.model.entity.Usuario;


//@SpringBootTest - ele sobe a aplicação inteira para fazer o teste, então não é interessante
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest //cria uma instância de banco de dados na memória e ao finalizar o teste, ela deleta 
@AutoConfigureTestDatabase(replace = Replace.NONE) //Para não subscrever as configurações do banco de dados
public class UsuarioRepositoryTest {

	@Autowired
	UsuarioRepository repository;
	
	@Autowired
	TestEntityManager entityManager; // para fazer a manipulação do cenário
	
	
	@Test
	public void deveVerificarAExistenciaDeUmEmail() {
		
		//cenario - criar o usuario e salvar na base de dados
		Usuario usuario = criarUsuario();
		entityManager.persist(usuario);
		
		// ação/excecução - verrificar se existe um usuario na base de dados
		boolean result = repository.existsByEmail("usuario@email.com");
		
		//verificação - verificar se o resultado é verdadeiro,
		//caso venha falso o teste falhou
		Assertions.assertThat(result).isTrue();
		
	}
	
	@Test
	public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComOMesmoEmail() {
		//cenario
		//repository.deleteAll(); Com o DataJpaTest não precisa mais deletar tudo, pois essa anotação já vai fazer 
		
		//ação
		boolean result = repository.existsByEmail("usuario@email.com");
		
		//verificação - essa deve vir falsa
		Assertions.assertThat(result).isFalse();
		
		
	}
	
	@Test
	public void devePersistirUmUsuarioNaBaseDeDados() {
		//cenário
		Usuario usuario = criarUsuario();
		
		//ação
	    Usuario usuarioSalvo = repository.save(usuario);
	    
	    //verificação
	    Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
		
	}
	
	@Test
	public void deveBuscarUmUsuarioPorEmail() {
		//cenário
		Usuario usuario = criarUsuario();
		entityManager.persist(usuario);
		
		//Verificação
		Optional<Usuario> result = repository.findByEmail("usuario@email.com");
		
		Assertions.assertThat(result.isPresent()).isTrue();
	}
	@Test
	public void deveRetornarVazioAoBuscarUmUsuarioPorEmailQuandoNaoExistirNaBase() {
		//cenário
		
		
		//Verificação
		Optional<Usuario> result = repository.findByEmail("usuario@email.com");
		
		Assertions.assertThat(result.isPresent()).isFalse();
	}
	//static para não precisar criar uma instância
	public static Usuario criarUsuario() {
		return Usuario.builder().nome("usuario")
				.email("usuario@email.com")
				.senha("senha").build();
	}
}
