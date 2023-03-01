package com.danielle.minhasfinancas.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.danielle.minhasfinancas.exception.RegraNegocioException;
import com.danielle.minhasfinancas.model.entity.Usuario;
import com.danielle.minhasfinancas.model.repository.UsuarioRepository;
import com.danielle.minhasfinancas.service.UsuarioService;


@Service("UsuarioService")
public class UsuarioServiceImpl implements UsuarioService {
	
	/* O UsuarioServiceImpl não pode acessar a camada da base de dados,
	 * ele vai precisar do repository para fazer aas alterações*/
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Usuario salvarUsuario(Usuario usuario) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validarEmail(String email) {
		boolean existe = repository.existsByEmail(email);
		if(existe) {
			throw new RegraNegocioException("Já existe um usuáario cadastrado com esse email.");
		}
		
	}

}
