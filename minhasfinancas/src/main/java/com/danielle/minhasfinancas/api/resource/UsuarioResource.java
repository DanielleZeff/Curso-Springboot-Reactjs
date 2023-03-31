package com.danielle.minhasfinancas.api.resource;




import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danielle.minhasfinancas.api.dto.UsuarioDTO;
import com.danielle.minhasfinancas.exception.ErrorAutenticacao;
import com.danielle.minhasfinancas.exception.RegraNegocioException;
import com.danielle.minhasfinancas.model.entity.Usuario;
import com.danielle.minhasfinancas.service.UsuarioService;


@RestController
@RequestMapping("/api/usuarios")
public class UsuarioResource {
	
	
	private UsuarioService service;
	
	//construtor para injeção de dependêcias
	public UsuarioResource(UsuarioService service) {
		this.service = service;
	}
	
	@PostMapping("/autenticar")
	public ResponseEntity autenticar(@RequestBody UsuarioDTO dto) {
		try {
		Usuario usuarioAutenticado = service.autenticar(dto.getEmail(), dto.getSenha());
		return ResponseEntity.ok(usuarioAutenticado);
		}catch (ErrorAutenticacao e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	//Aqui vai receber um DTO - cria-sse uma classe DTO no pacote de DTO e receber ele aqui como parâmetro
	@PostMapping
	public ResponseEntity salvar(@RequestBody UsuarioDTO dto) {
		
		//transformar o dto em ENTIDADE/USUARIO
		Usuario usuario = Usuario.builder()
				.nome(dto.getNome())
				.email(dto.getEmail())
				.senha(dto.getSenha())
				.build();
		
		try {
			Usuario usuarioSalvo = service.salvarUsuario(usuario);
			return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
		}catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
