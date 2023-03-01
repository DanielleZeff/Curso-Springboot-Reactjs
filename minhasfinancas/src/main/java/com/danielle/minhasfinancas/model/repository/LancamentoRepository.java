package com.danielle.minhasfinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.danielle.minhasfinancas.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{

}