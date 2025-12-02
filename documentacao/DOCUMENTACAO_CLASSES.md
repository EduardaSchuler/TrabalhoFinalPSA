# 📐 Documentação dos Diagramas - Sistema de Estacionamento

## 🏗️ Diagrama de Classes do Sistema

### Organização por Pacotes (Arquitetura em Camadas)

#### 📦 **com.progsoftaplic.TrabalhoFinal.controller** (Camada de Apresentação)
```
┌─────────────────────────────────────┐
│            <<Controller>>           │
│           TicketController          │
├─────────────────────────────────────┤
│ - ticketService: TicketService      │
├─────────────────────────────────────┤
│ + criarTicket(req: TicketCreateRequestDTO): ResponseEntity<TicketResponseDTO> │
│ + buscarTicket(codigo: String): ResponseEntity<TicketResponseDTO>              │
│ + calcularValor(codigo: String): ResponseEntity<TicketValorResponseDTO>        │
│ + liberarTicketComPagamento(codigo: String): ResponseEntity<PagamentoResponseDTO> │
│ + validarSaida(codigo: String): ResponseEntity<ValidarSaidaResponseDTO>        │
│ - toDTO(ticket: Ticket): TicketResponseDTO                                     │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│            <<Controller>>           │
│            WebController            │
├─────────────────────────────────────┤
│ + index(): String                   │
│ + operador(): String               │
│ + gerencial(): String              │
│ + cancela(): String                │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│            <<Controller>>           │
│          CancelaController          │
├─────────────────────────────────────┤
│ - ticketService: TicketService      │
├─────────────────────────────────────┤
│ + emitirTicket(req: TicketCreateRequestDTO): ResponseEntity<TicketResponseDTO>  │
│ + validarSaida(codigo: String): ResponseEntity<ValidarSaidaResponseDTO>         │
│ - toDTO(ticket: Ticket): TicketResponseDTO                                     │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│            <<Controller>>           │
│         RelatorioController         │
├─────────────────────────────────────┤
│ - ticketService: TicketService      │
├─────────────────────────────────────┤
│ + receitaPorPeriodo(inicio: LocalDateTime, fim: LocalDateTime): ResponseEntity<ReportResponseDTO> │
│ + receitaPorDia(data: LocalDate): ResponseEntity<ReportResponseDTO>                              │
│ + receitaPorMes(ano: int, mes: int): ResponseEntity<ReportResponseDTO>                           │
└─────────────────────────────────────┘
```

#### 📦 **com.progsoftaplic.TrabalhoFinal.service** (Camada de Serviço)
```
┌─────────────────────────────────────┐
│            <<Service>>              │
│           TicketService             │
├─────────────────────────────────────┤
│ - ticketRepository: TicketRepository│
│ - pagamentoRepository: PagamentoRepository │
├─────────────────────────────────────┤
│ + criarTicket(placa: String): Ticket                                          │
│ + calcularValor(codigo: String): BigDecimal                                   │
│ + validarSaida(codigo: String): boolean                                       │
│ + pagarTicket(codigo: String): boolean                                        │
│ + totalRecebido(inicio: LocalDateTime, fim: LocalDateTime): BigDecimal        │
│ + contarTicketsPagos(inicio: LocalDateTime, fim: LocalDateTime): Integer      │
│ + buscarPorCodigo(codigo: String): Optional<Ticket>                          │
│ + pagarETrazerPagamento(codigo: String): Pagamento                           │
└─────────────────────────────────────┘
```

#### 📦 **com.progsoftaplic.TrabalhoFinal.domain** (Camada de Domínio)
```
┌─────────────────────────────────────┐
│            <<Entity>>               │
│              Ticket                 │
├─────────────────────────────────────┤
│ - codigo: String                    │
│ - placa: String                     │
│ - entrada: LocalDateTime            │
│ - saida: LocalDateTime              │
│ - pago: boolean                     │
│ - valor: BigDecimal                 │
├─────────────────────────────────────┤
│ + Ticket()                          │
│ + Ticket(codigo: String, placa: String, entrada: LocalDateTime) │
│ + getCodigo(): String               │
│ + getPlaca(): String               │
│ + getEntrada(): LocalDateTime       │
│ + getSaida(): LocalDateTime         │
│ + isPago(): boolean                 │
│ + getValor(): BigDecimal           │
│ + setCodigo(codigo: String): void   │
│ + setPlaca(placa: String): void     │
│ + setEntrada(entrada: LocalDateTime): void │
│ + setSaida(saida: LocalDateTime): void     │
│ + setPago(pago: boolean): void             │
│ + setValor(valor: BigDecimal): void        │
│ + isVencido(): boolean                     │
│ + isPeriodoCortesia(): boolean             │
│ + marcarComoPago(valorPago: BigDecimal): void │
│ + registrarSaida(): void                   │
│ + equals(obj: Object): boolean             │
│ + hashCode(): int                          │
│ + toString(): String                       │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│            <<Entity>>               │
│             Pagamento               │
├─────────────────────────────────────┤
│ - id: Long                          │
│ - ticketCodigo: String              │
│ - valor: BigDecimal                 │
│ - dataPagamento: LocalDateTime      │
├─────────────────────────────────────┤
│ + Pagamento()                       │
│ + Pagamento(ticketCodigo: String, valor: BigDecimal) │
│ + getId(): Long                     │
│ + getTicketCodigo(): String         │
│ + getValor(): BigDecimal           │
│ + getDataPagamento(): LocalDateTime │
└─────────────────────────────────────┘
```

#### 📦 **com.progsoftaplic.TrabalhoFinal.repository** (Camada de Persistência)
```
┌─────────────────────────────────────┐
│          <<Repository>>             │
│          TicketRepository           │
├─────────────────────────────────────┤
│ extends JpaRepository<Ticket, String>│
├─────────────────────────────────────┤
│ + findByPagoTrueAndSaidaBetween(inicio: LocalDateTime, fim: LocalDateTime): List<Ticket> │
│ + findByPagoTrueAndSaidaBetweenOrderBySaidaAsc(inicio: LocalDateTime, fim: LocalDateTime): List<Ticket> │
│ + findByPlaca(placa: String): List<Ticket>                                              │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│          <<Repository>>             │
│        PagamentoRepository          │
├─────────────────────────────────────┤
│ extends JpaRepository<Pagamento, Long>│
├─────────────────────────────────────┤
│ + findByDataPagamentoBetween(inicio: LocalDateTime, fim: LocalDateTime): List<Pagamento> │
└─────────────────────────────────────┘
```

#### 📦 **com.progsoftaplic.TrabalhoFinal.service.dto** (Transfer Objects)
```
┌─────────────────────────────────────┐
│              <<DTO>>                │
│         TicketResponseDTO           │
├─────────────────────────────────────┤
│ - codigo: String                    │
│ - placa: String                     │
│ - entrada: LocalDateTime            │
│ - saida: LocalDateTime              │
│ - pago: boolean                     │
│ - valor: BigDecimal                 │
├─────────────────────────────────────┤
│ + getters() and setters()           │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│              <<DTO>>                │
│      TicketCreateRequestDTO         │
├─────────────────────────────────────┤
│ - placa: String                     │
├─────────────────────────────────────┤
│ + getPlaca(): String               │
│ + setPlaca(placa: String): void     │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│              <<DTO>>                │
│       TicketValorResponseDTO        │
├─────────────────────────────────────┤
│ - codigo: String                    │
│ - valor: BigDecimal                 │
├─────────────────────────────────────┤
│ + getters() and setters()           │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│              <<DTO>>                │
│      ValidarSaidaResponseDTO        │
├─────────────────────────────────────┤
│ - codigo: String                    │
│ - liberado: boolean                 │
│ - motivo: String                    │
├─────────────────────────────────────┤
│ + getters() and setters()           │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│              <<DTO>>                │
│        ReportResponseDTO            │
├─────────────────────────────────────┤
│ - totalRecebido: BigDecimal         │
│ - quantidadeTicketsPagos: Integer   │
│ - inicio: LocalDateTime             │
│ - fim: LocalDateTime                │
├─────────────────────────────────────┤
│ + getters() and setters()           │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│              <<DTO>>                │
│       PagamentoResponseDTO          │
├─────────────────────────────────────┤
│ - id: Long                          │
│ - ticketCodigo: String              │
│ - valor: BigDecimal                 │
│ - dataPagamento: LocalDateTime      │
├─────────────────────────────────────┤
│ + getters() and setters()           │
└─────────────────────────────────────┘
```

### 🔄 Relacionamentos Entre Classes

```
TicketController -----> TicketService
CancelaController ----> TicketService
RelatorioController --> TicketService
WebController (não possui dependências diretas)

TicketService --------> TicketRepository
TicketService --------> PagamentoRepository
TicketService --------> Ticket (Domain Model)
TicketService --------> Pagamento (Domain Model)

TicketRepository -----> Ticket (Entity)
PagamentoRepository --> Pagamento (Entity)

Ticket 1 ---------> 0..* Pagamento (relacionamento lógico por ticketCodigo)
```

---

## 🗂️ Padrões de Projeto Implementados

### ✅ **1. MVC (Model-View-Controller)**

**Onde foi aplicado:** Camada de Apresentação

**Implementação:**
- **Controllers**: `TicketController`, `WebController`, `CancelaController`, `RelatorioController`
- **Views**: Templates Thymeleaf (index.html, cancela.html, operador.html, gerencial.html)
- **Models**: DTOs e Domain Objects (Ticket, Pagamento)

**Justificativa:** Separação clara entre lógica de apresentação, controle de fluxo e dados, facilitando manutenibilidade e testabilidade.

**Código Exemplo:**
```java
@RestController
@RequestMapping("/tickets")
public class TicketController {
    // Controller gerencia requisições HTTP
    
    @PostMapping
    public ResponseEntity<TicketResponseDTO> criarTicket(@RequestBody TicketCreateRequestDTO req) {
        // Processa entrada, chama service, retorna view
        Ticket ticket = ticketService.criarTicket(req.getPlaca());
        return ResponseEntity.ok(toDTO(ticket));
    }
}
```

### ✅ **2. Domain Model**

**Onde foi aplicado:** Camada de Domínio

**Implementação:** Classe `Ticket.java` e `Pagamento.java`

**Características Implementadas:**
- **Encapsulamento**: Atributos privados com getters/setters validados
- **Métodos de Domínio**: `isPeriodoCortesia()`, `marcarComoPago()`, `registrarSaida()`
- **Validações de Negócio**: Regras implementadas dentro das entidades
- **Comportamento Rico**: Objetos não são apenas estruturas de dados

**Justificativa:** Centralização das regras de negócio nas entidades de domínio, mantendo a lógica próxima aos dados que ela manipula.

**Código Exemplo:**
```java
@Entity
public class Ticket {
    // Métodos de domínio encapsulam regras de negócio
    
    public boolean isPeriodoCortesia() {
        if (entrada == null) return false;
        return LocalDateTime.now().isBefore(entrada.plusMinutes(15));
    }
    
    public void marcarComoPago(BigDecimal valorPago) {
        if (valorPago == null || valorPago.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor de pagamento inválido");
        }
        this.valor = valorPago;
        this.pago = true;
    }
}
```

### ✅ **3. Repository Pattern**

**Onde foi aplicado:** Camada de Persistência

**Implementação:** `TicketRepository` e `PagamentoRepository`

**Características:**
- **Abstração da Persistência**: Interfaces escondem detalhes do JPA
- **Consultas Específicas**: Métodos customizados para necessidades do domínio
- **Separação de Responsabilidades**: Persistência isolada da lógica de negócio

**Justificativa:** Isola a camada de domínio dos detalhes de persistência, permitindo mudanças na tecnologia de BD sem afetar a lógica de negócio.

**Código Exemplo:**
```java
@Repository
public interface TicketRepository extends JpaRepository<Ticket, String> {
    // Abstrai consultas específicas do domínio
    List<Ticket> findByPagoTrueAndSaidaBetween(LocalDateTime inicio, LocalDateTime fim);
    List<Ticket> findByPlaca(String placa);
}
```

### ✅ **4. Dependency Injection (DI)**

**Onde foi aplicado:** Em todas as camadas

**Implementação:** Constructor Injection via Spring Framework

**Características:**
- **Inversão de Controle**: Spring gerencia dependências
- **Baixo Acoplamento**: Classes dependem de abstrações
- **Facilita Testes**: Permite mock das dependências

**Código Exemplo:**
```java
@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final PagamentoRepository pagamentoRepository;

    // Constructor Injection
    public TicketService(TicketRepository ticketRepository, 
                        PagamentoRepository pagamentoRepository) {
        this.ticketRepository = ticketRepository;
        this.pagamentoRepository = pagamentoRepository;
    }
}
```

### ✅ **5. DTO (Data Transfer Object)**

**Onde foi aplicado:** Comunicação entre camadas

**Implementação:** Pacote `service.dto`

**Características:**
- **Transferência de Dados**: Objetos simples para transporte
- **Desacoplamento**: APIs não expõem entidades diretamente
- **Versionamento**: Permite evolução independente de APIs

**Código Exemplo:**
```java
public class TicketResponseDTO {
    private String codigo;
    private String placa;
    private LocalDateTime entrada;
    // ... outros atributos
    
    // Apenas getters/setters, sem lógica de negócio
}
```

---

## 🔄 Fluxo de Dados Entre Camadas

```
Browser/Cliente
       ↓ HTTP Request
Controller (MVC - Controller)
       ↓ chama
Service (Business Logic)
       ↓ utiliza
Repository (Repository Pattern)
       ↓ persiste
Database (H2)
       ↑ retorna dados
Repository
       ↑ Domain Objects
Service
       ↑ DTOs
Controller
       ↑ JSON/HTML
Browser/Cliente
```

---

## 🎯 Benefícios da Arquitetura Implementada

### **Separação de Responsabilidades**
- Cada camada tem uma responsabilidade específica e bem definida
- Facilita manutenção e evolução do código

### **Testabilidade**
- Dependency Injection permite fácil criação de testes unitários
- Camadas podem ser testadas independentemente

### **Flexibilidade**
- Repository Pattern permite mudança de BD sem afetar business logic
- MVC permite diferentes tipos de clientes (web, mobile, API)

### **Reutilização**
- Services podem ser utilizados por diferentes controllers
- Domain Model encapsula regras que são reutilizadas

### **Manutenibilidade**
- Código organizado e com responsabilidades claras
- Padrões conhecidos facilitam onboarding de novos desenvolvedores