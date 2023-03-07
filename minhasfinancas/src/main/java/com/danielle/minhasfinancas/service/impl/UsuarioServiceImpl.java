package com.danielle.minhasfinancas.service.impl;



import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.danielle.minhasfinancas.exception.ErrorAutenticacao;
import com.danielle.minhasfinancas.exception.RegraNegocioException;
import com.danielle.minhasfinancas.model.entity.Usuario;
import com.danielle.minhasfinancas.model.repository.UsuarioRepository;
import com.danielle.minhasfinancas.service.UsuarioService;


@Service("UsuarioService")
public class UsuarioServiceImpl implements UsuarioService {
	
	/* O UsuarioServiceImpl não pode acessar a camada da base de dados,
	 * ele vai precisar do repository para fazer as alterações*/ 
	
	private UsuarioRepository repository;

	/*Para que o UsuarioServiceImpl venha funcionar, 
	vou precisar construir ele com uma instância de um UsuarioRepository*/
	@Autowired
	public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		Optional<Usuario> usuario = repository.findByEmail(email);
		
		if(!usuario.isPresent()) {
			throw new ErrorAutenticacao("Usuário não encontrado para o email informado.");
		}
		
		if(!usuario.get().getSenha().equals(senha)) {
			throw new ErrorAutenticacao("Senha inválida.");
		}
		return usuario.get();
	}

	@Override
	@Transactional //Vai abrir uma transação e executar o método de salvar o usuario e depois que salvar, ele vai comitar
	public Usuario salvarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());// garantir que ele não esteja cadastrado na base de dados 
		return repository.save(usuario);
	}

	@Override
	public void validarEmail(String email) {
		boolean existe = repository.existsByEmail(email);
		if(existe) {
			throw new RegraNegocioException("Já existe um usuário cadastrado com esse email.");
		}
		
	}

}
