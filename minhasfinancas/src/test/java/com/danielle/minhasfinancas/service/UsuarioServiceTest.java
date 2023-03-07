package com.danielle.minhasfinancas.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.danielle.minhasfinancas.exception.ErrorAutenticacao;
import com.danielle.minhasfinancas.exception.RegraNegocioException;
import com.danielle.minhasfinancas.model.entity.Usuario;
import com.danielle.minhasfinancas.model.repository.UsuarioRepository;
import com.danielle.minhasfinancas.service.impl.UsuarioServiceImpl;


@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

	
	//@Autowired - com o teste mockado,
	//não precisa injetar as instâncias oficiais
	
	@SpyBean
	UsuarioServiceImpl service;
	
	//@Autowired - com o teste mockado, 
	//não precisa injetar as instâncias oficiais
	
	@MockBean // essa annotation cria as instâncias 
	//já mockadas sem precisar fazer tudo na mão
	UsuarioRepository repository;
	
	//teste unitário mockado - lembrar de sempre injetar o mock dentro da classe que quiser testar
	/*@Before
	public void setUp() {
		repository = Mockito.spy(UsuarioServiceImpl.class); //SEM a annotation @MockBean
		service = new UsuarioServiceImpl(repository);
	}*/
	
	@Test(expected = Test.None.class)
	public void deveSalvarUmUsuario() {
		//cenario - vai criar um mock no spy e validar o email
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		Usuario usuario = Usuario.builder()
				.id(1l)
				.nome("nome")
				.email("email@email.com")
				.senha("senha")
				.build();
		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		
		//acao
		Usuario usuarioSalvo = service.salvarUsuario(new Usuario());
		
		//verificacao
		Assertions.assertThat(usuarioSalvo).isNotNull();
		Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1l);
		Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("nome");
		Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("email@email.com");
		Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");
		
	}
	
	@Test(expected = RegraNegocioException.class)
	public void naoDeveSalvarUmUsuarioComEmailJaCadastrado() {
		//cenario
		String email = "email@email.com";
		Usuario usuario = Usuario.builder().email(email).build();
		Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email);
	
		//acao
		service.salvarUsuario(usuario);
		
		//verificacao - esperando nuca ter chamado a acao do método usuario
		Mockito.verify(repository, Mockito.never()).save(usuario);
	}
	
	@Test(expected = Test.None.class)
	public void deveAutenticarUmUsuarioComSucesso() {
		//cenario
		String email = "email@email.com"; 
		String senha = "senha";
		
		Usuario usuario = Usuario.builder()
				.email(email).senha(senha).id(1l).build();
		
		//simular a chamada do findByEmail do meu repository
		Mockito.when(repository.findByEmail(email))
		.thenReturn(Optional.of(usuario));
		
		//ação
		Usuario result = service.autenticar(email, senha);
		
		//verificação
		Assertions.assertThat(result).isNotNull();
	}
	
	@Test
	public void deveLancarErroQuandoEncontrarUsuarioCadastradoComOEmailInformado() {
		//cenario
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
	
		//acao
	   Throwable exception =    Assertions.catchThrowable(() -> service.autenticar("email@email.com", "senha"));
	
	   //verificação
	   Assertions.assertThat(exception).isInstanceOf(ErrorAutenticacao.class).hasMessage("Usuário não encontrado para o email informado.");
	}
	
	@Test
	public void deveLancarErroQuandoSenhaNaoBater() {
		//cenario
		String senha = "senha";
		Usuario usuario = Usuario.builder().email("email@email.com").senha(senha).build();
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
		
		//acao
	    Throwable exception = Assertions.catchThrowable(() -> service.autenticar("email@email.com", "123")) ;
		Assertions.assertThat(exception).isInstanceOf(ErrorAutenticacao.class).hasMessage("Senha inválida.");
	}
	
	@Test(expected = Test.None.class) //expected = Test.None.class espera que minha classe não lance nenhuma exceção
	public void deveValidarEmail() {
		
		//cenario
		
		/*test com mock
		UsuarioRepository usuarioRepositoryMock =	Mockito.mock(UsuarioRepository.class);*/
		
		//repository.deleteAll();
		
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
		
		//ação
		service.validarEmail("email@email.com");
		
		//não precisa da verificação, pois a verificação já é a ação de validar 
	}
	
	@Test(expected = RegraNegocioException.class ) // Aqui já pede para lançar a exeçãao se caso tiver erro - exception criada na classe UsuarioService 
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
		//cenario
		//Usuario usuario = Usuario.builder().nome("usuario").email("email@email.com").build();
		//repository.save(usuario);
		
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
		
		//ação
		service.validarEmail("email@email.com");
		
	}
	
}
