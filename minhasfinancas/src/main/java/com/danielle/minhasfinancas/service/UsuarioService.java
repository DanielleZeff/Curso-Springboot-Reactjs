package com.danielle.minhasfinancas.service;

import com.danielle.minhasfinancas.model.entity.Usuario;


//Vai definir os métodos para trabalhar com Usuario e entidades
public interface UsuarioService {

	/* Vai atender o caso de uso do login - Ao receber o email, 
	ele vai na base de dados e se existir o usuario cadastrado com esse email, 
	ele vai ver a senha e se aa senha estiver batendo, 
	ele vai retornar a instância do usuario autenticado*/
	Usuario autenticar(String email, String senha);
	
	
	/* Vai receber como parâmetro o usuario 
	 * que ainda não foi salvo na base de dados sem o id e 
	 * quando ele retornar o usuario persistido, ele já vai ter o seu id*/
	Usuario salvarUsuario(Usuario usuario);
	
	
	/* O email só pode estar cadastrado uma vez, então se ao salvar o email e 
	 * esse já estiver cadastrado na base de dados, 
	 * ele vai retornar uma mensagem de erro*/
	void validarEmail(String email);
	
}
