package br.com.vanderlei.api.resourse;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.vanderlei.api.dto.UsuarioDTO;
import br.com.vanderlei.exception.ErroAutenticacao;
import br.com.vanderlei.exception.RegraNegocioException;
import br.com.vanderlei.model.entity.Usuario;
import br.com.vanderlei.service.LancamentoService;
import br.com.vanderlei.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioResourse {
	private UsuarioService service;
	private final LancamentoService lancamentoService;
	public UsuarioResourse(UsuarioService service) {
		this.lancamentoService = null;
		this.service = service;
	}
	
	@PostMapping("/autenticar")
	public ResponseEntity autenticar(@RequestBody UsuarioDTO dto){
		try{
			Usuario usuarioAutenticado = service.autenticar(dto.getEmail(),dto.getSenha());
			return ResponseEntity.ok(usuarioAutenticado);
		}catch(ErroAutenticacao e){
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping
	public ResponseEntity salvar(@RequestBody UsuarioDTO dto){
		
		Usuario usuario = new Usuario();
		usuario.setNome(dto.getNome());
		usuario.setEmail(dto.getEmail());
		usuario.setSenha(dto.getSenha());
		try {
			Usuario usuarioSalvo = service.salvarUsuario(usuario);
			return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
		}catch(RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	@GetMapping("{id}/saldo")
	public ResponseEntity obterSaldo(@PathVariable("id")Long id) {
		Optional<Usuario> usuario = service.obterPorId(id);
		if(!usuario.isPresent()) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		BigDecimal saldo=lancamentoService.obterSaldoPorUsuario(id);
		return ResponseEntity.ok(saldo);
	}
}
