package br.com.vanderlei.model.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.vanderlei.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento,Long>{
	@Query(value="select sum(l.valor) from lancamento l join.usuario u"
			+" where u.id = :idUsuario and l.tipo =:tipo group by u")
	BigDecimal obterSaldoPorTipoLancamentoEUsuario(@Param("isUsuario")Long idusuario,@Param("tipo")String tipo);
}
