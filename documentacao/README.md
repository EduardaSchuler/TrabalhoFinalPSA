# 📚 Documentação do Sistema de Estacionamento

## 📋 Índice da Documentação Acadêmica

Esta pasta contém toda a documentação técnica do **Sistema de Estacionamento** desenvolvido em Spring Boot.

### 📄 **Documentação Textual**

1. **[DOCUMENTACAO_CLASSES.md](DOCUMENTACAO_CLASSES.md)**
   - Documentação completa dos padrões de projeto implementados
   - Análise da arquitetura em camadas
   - Descrição detalhada de cada classe e seus relacionamentos
   - Justificativas técnicas dos padrões MVC, Domain Model e Repository

2. **[DOCUMENTACAO_DATABASE.md](DOCUMENTACAO_DATABASE.md)**
   - Estrutura completa da base relacional
   - Descrição das tabelas TICKET e PAGAMENTO
   - Relacionamentos, constraints e índices
   - Consultas típicas do sistema

### 🎨 **Diagramas Visuais**

3. **[Diagrama de Classes - Sistema de Estacionamento.pdf](Diagrama%20de%20Classes%20-%20Sistema%20de%20Estacionamento.pdf)**
   - Diagrama UML completo das classes do sistema
   - Organização por camadas (Controller, Service, Domain, Repository, DTO)
   - Visualização dos padrões de projeto implementados
   - Relacionamentos entre classes claramente definidos

4. **[Diagrama da Base Relacional - Sistema de Estacionamento.pdf](Diagrama%20da%20Base%20Relacional%20-%20Sistema%20de%20Estacionamento.pdf)**
   - ERD (Entity Relationship Diagram) das tabelas
   - Relacionamento 1:N entre TICKET e PAGAMENTO
   - Tipos de dados, constraints e exemplos
   - Consultas SQL típicas do sistema

---

## 🎯 **Padrões de Projeto Documentados**

### ✅ **MVC (Model-View-Controller)**
- **Controllers**: Gerenciam requisições HTTP e coordenam o fluxo
- **Services**: Contêm a lógica de negócio
- **DTOs**: Transportam dados entre camadas

### ✅ **Domain Model**
- **Entities**: Classes ricas com comportamento e validações
- **Encapsulamento**: Regras de negócio dentro das entidades
- **Métodos de Domínio**: isPeriodoCortesia(), marcarComoPago(), etc.

### ✅ **Repository Pattern**
- **Abstração**: Interfaces JPA escondem detalhes de persistência
- **Consultas Customizadas**: Métodos específicos para necessidades do domínio
- **Separação de Responsabilidades**: Persistência isolada da lógica

### ✅ **Dependency Injection**
- **Inversão de Controle**: Spring gerencia dependências
- **Constructor Injection**: Baixo acoplamento entre classes
- **Testabilidade**: Facilita criação de mocks e testes

### ✅ **Data Transfer Object (DTO)**
- **Transferência de Dados**: Objetos simples para APIs
- **Desacoplamento**: APIs não expõem entidades diretamente
- **Versionamento**: Permite evolução independente

---

## 🏗️ **Arquitetura do Sistema**

```
┌─────────────────────────────────────────────────┐
│                 CAMADA WEB                      │
│          (Controllers + Templates)              │
├─────────────────────────────────────────────────┤
│                CAMADA SERVIÇO                   │
│             (Business Logic)                    │
├─────────────────────────────────────────────────┤
│               CAMADA DOMÍNIO                    │
│            (Entities + Rules)                   │
├─────────────────────────────────────────────────┤
│             CAMADA REPOSITÓRIO                  │
│            (Data Access Layer)                  │
├─────────────────────────────────────────────────┤
│              BASE DE DADOS                      │
│              (H2 Database)                      │
└─────────────────────────────────────────────────┘
```

---

## 📊 **Funcionalidades Implementadas**

### 🚗 **Gestão de Tickets**
- Emissão automática de tickets com código único
- Cálculo de valor baseado no tempo de permanência
- Período de cortesia de 15 minutos
- Validação de saída após pagamento

### 💳 **Sistema de Pagamento**
- Registro de pagamentos por ticket
- Suporte a múltiplos pagamentos por ticket
- Histórico completo de transações
- Validações de valor e integridade

### 📈 **Relatórios Gerenciais**
- Receita por período (dia, mês, customizado)
- Quantidade de tickets pagos
- Análise temporal de uso do estacionamento
- Exportação de dados para análise

### 🌐 **Interface Web**
- Interface para operadores (emissão/pagamento)
- Interface gerencial (relatórios)
- API REST completa para integrações
- Templates responsivos com Bootstrap

---

## 🎓 **Uso Acadêmico**

Esta documentação foi desenvolvida para atender aos requisitos acadêmicos de:

- ✅ **Análise e Projeto de Sistemas**
- ✅ **Padrões de Projeto de Software**
- ✅ **Arquitetura de Software**
- ✅ **Engenharia de Software**
- ✅ **Desenvolvimento Web**
- ✅ **Base de Dados**

---

## 📝 **Como Usar Esta Documentação**

1. **Para Análise de Arquitetura**: Consulte `DOCUMENTACAO_CLASSES.md`
2. **Para Análise de Dados**: Consulte `DOCUMENTACAO_DATABASE.md`
3. **Para Apresentações**: Use os PDFs dos diagramas
4. **Para Implementação**: Combine as informações de todos os documentos

---

**📅 Gerado em:** Dezembro 2024  
**🎯 Projeto:** Sistema de Estacionamento - Spring Boot  
**👨‍💻 Padrões:** MVC + Domain Model + Repository Pattern