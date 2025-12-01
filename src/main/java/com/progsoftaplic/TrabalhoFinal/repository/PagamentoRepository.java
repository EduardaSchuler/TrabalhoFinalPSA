package com.progsoftaplic.TrabalhoFinal.repository;

import com.estac.domain.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;


public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    @Query("SELECT COALESCE(SUM(p.valor),0) FROM Pagamento p WHERE DATE(p.dataPagamento) = :dia")
    Double totalPorDia(@Param("dia") LocalDate dia);

    @Query("SELECT COALESCE(SUM(p.valor),0) FROM Pagamento p WHERE MONTH(p.dataPagamento) = :mes AND YEAR(p.dataPagamento)=:ano")
    Double totalPorMes(@Param("mes") int mes, @Param("ano") int ano);

    @Query("SELECT COUNT(p) FROM Pagamento p WHERE DATE(p.dataPagamento) = :dia")
    Integer ticketsPorDia(@Param("dia") LocalDate dia);

    @Query("SELECT COUNT(p) FROM Pagamento p WHERE MONTH(p.dataPagamento) = :mes AND YEAR(p.dataPagamento)=:ano")
    Integer ticketsPorMes(@Param("mes") int mes, @Param("ano") int ano);
}