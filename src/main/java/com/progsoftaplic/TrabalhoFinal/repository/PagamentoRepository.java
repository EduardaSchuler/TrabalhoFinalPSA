package com.progsoftaplic.TrabalhoFinal.repository;

import com.progsoftaplic.TrabalhoFinal.domain.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
    List<Pagamento> findByDataPagamentoBetween(LocalDateTime inicio, LocalDateTime fim);
}
