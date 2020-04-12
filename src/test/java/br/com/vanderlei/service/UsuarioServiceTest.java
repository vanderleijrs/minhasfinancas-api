package br.com.vanderlei.service;


import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import br.com.vanderlei.exception.ErroAutenticacao;
import br.com.vanderlei.exception.RegraNegocioException;
import br.com.vanderlei.model.entity.Usuario;
import br.com.vanderlei.model.repository.UsuarioRepository;
import br.com.vanderlei.service.impl.UsuarioServiceImpl;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {
	@SpyBean
	UsuarioServiceImpl service;
	
	@MockBean
	UsuarioRepository repository;
	@Test
	public Usuario criaUsuario(String email, String senha) {
		
		Usuario usuario = new  Usuario();
		usuario.setSenha(senha);
		usuario.setEmail(email);
		return usuario;
	}
	
	/*@BeforeEach
	public void setUp() {
		Mockito.spy(UsuarioServiceImpl.class);
		service = new UsuarioServiceImpl(repository);
	}*/
	@Test
	public void deveSalvarUmUsuario() {
		//cenário
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		String email ="email@email.com";
		String senha ="senha";
		String nome = "nome";
		Long id =  1l;
		Usuario usuario = new Usuario();
		usuario.setId(id);
		usuario.setEmail(email);
		usuario.setSenha(senha);
		usuario.setNome(nome);
		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		//ação
		Usuario usuarioSalvo = service.salvarUsuario(new Usuario());
		//Verificação
		Assertions.assertThat(usuarioSalvo).isNotNull();
		Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1l);
		Assertions.assertThat(usuarioSalvo.getNome().equals(nome));
		Assertions.assertThat(usuarioSalvo.getEmail().equals(email));
		Assertions.assertThat(usuarioSalvo.getSenha().equals(senha));
	}
	@Test 
	public void naoDeveSalvarUmUsuarioComEmailJaCadastrado() {
		//cenario
		String email ="email@email.com";
		
		Usuario usuario = new Usuario();
		usuario.setEmail(email);
		Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email);		
		//ação
		org.junit.jupiter.api.Assertions.assertThrows(RegraNegocioException.class,(() ->service.salvarUsuario(usuario)));
		
		//verificação
		Mockito.verify(repository,Mockito.never()).save(usuario);
		
	}
	@Test
	public void deveAutenticarUmUsuarioComSucesso() {
		//cenario
		String email ="email@email.com";
		String senha ="senha";
		Usuario usuario = criaUsuario(email, senha);
		Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));
		//ação
		Usuario result =service.autenticar(email, senha);
		//verificação
		Assertions.assertThat(result).isNotNull();
	}
	
	@Test
	public void deveLancarErroQuandoNãoEncontarUsuarioCadastradoComEmailInformado() {
		//cenário
			Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		//ação
			Throwable exception = Assertions.catchThrowable(()->service.autenticar("email@email.com", "senha"));
			Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Usuario não encontrado para o e-mail informado.");		
	}
	
	@Test
	public void deveLancarErroQuandoSenhaNaoBater() {
		//cenário
		String email ="email@email.com";
		String senha ="senha";
		Usuario usuario = criaUsuario(email,senha);
		
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
		//ação
		Throwable exception = Assertions.catchThrowable(()->service.autenticar(email, "123"));
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Senha Inválida");
	}
	@Test
	public void deveValidarEmail() {
		//cenário
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
		
		//ação
		service.validarEmail("email@email.com");
	
	}
	@Test
	public void deveLancarErroValidarEmailQuandoExistirEmailCadastrado() {
		//cenário
		/*Usuario usuario = new  Usuario();
		usuario.setNome("usuario");
		usuario.setEmail("email@email.com");
		repository.save(usuario);*/
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
		//ação
		//service.validarEmail("email@email.com");
		Throwable exception = Assertions.catchThrowable(()->service.validarEmail(Mockito.anyString()));
		//Verificação
		
		Assertions.assertThat(exception).isInstanceOf(RegraNegocioException.class).hasMessage("Já existe um usuário cadastrado com este e-mail");
	}
}
