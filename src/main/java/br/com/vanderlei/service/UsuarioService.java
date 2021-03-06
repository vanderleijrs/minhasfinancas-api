package br.com.vanderlei.service;

import java.util.Optional;

import br.com.vanderlei.model.entity.Usuario;

public interface UsuarioService {
	Usuario autenticar(String email, String senha);
	Usuario salvarUsuario(Usuario usuario);
	void validarEmail(String email);
	Optional<Usuario>  obterPorId(Long id);
}
