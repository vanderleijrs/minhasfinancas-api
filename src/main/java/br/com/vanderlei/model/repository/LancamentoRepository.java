package br.com.vanderlei.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.vanderlei.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento,Long>{

}
