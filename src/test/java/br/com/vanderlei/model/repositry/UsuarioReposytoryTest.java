package br.com.vanderlei.model.repositry;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.vanderlei.model.entity.Usuario;
import br.com.vanderlei.model.repository.UsuarioRepository;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioReposytoryTest {
	@Autowired
	UsuarioRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	public static Usuario criarUsuario() {
		Usuario usuario = new Usuario();
		usuario.setNome("usuario");
		usuario.setEmail("usuario@email.com");
		usuario.setSenha("senha");
		return usuario;
	}
	@Test
	public void deveVerificarExistenciaDeUmEmail() {
		//cenário
	   Usuario usuario= criarUsuario();
		entityManager.persist(usuario);
		
		//ação
		boolean result = repository.existsByEmail("usuario@email.com");
		
		//verificação
		Assertions.assertThat(result).isTrue();
	}
	@Test
	public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComEmail() {
		//cenário
		
		//ação
		boolean result = repository.existsByEmail("usuario@email.com");
		//verificação
		Assertions.assertThat(result).isFalse();
		
	}
	
	@Test
	public void devePersistirUmUsuarionaBaseDedados() {
		//cenario
		 Usuario usuario= criarUsuario();
		//ação
		Usuario usuarioSalvo = repository.save(usuario);
		//verificação
		Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
		
	}
	@Test
	public void deveBuscarUmUsuarioPorEmail() {
		//cenário
		 Usuario usuario= criarUsuario();
		entityManager.persist(usuario);
		//ação
		Optional<Usuario>result = repository.findByEmail("usuario@email.com");
		//verificação
		Assertions.assertThat(result.isPresent()).isTrue();
	}
	
	
}
