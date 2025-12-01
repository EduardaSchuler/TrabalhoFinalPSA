# 🚗 Sistema de Estacionamento Pago

Sistema completo para controle de estacionamento pago desenvolvido em **Spring Boot**, implementando arquitetura multicamada com separação de responsabilidades.

## 📋 Funcionalidades Implementadas

### 🚪 Cancela de Entrada
- ✅ Emissão de tickets com código único
- ✅ Registro de placa e data/hora de entrada
- ✅ API REST para integração com dispositivos de cancela

### 🚪 Cancela de Saída  
- ✅ Validação de tickets para liberação
- ✅ Verificação de status de pagamento
- ✅ Liberação automática para período de cortesia

### 💰 Operador do Caixa
- ✅ Cálculo automático de valores baseado em regras de negócio
- ✅ Interface gráfica para operação
- ✅ Processamento de pagamentos

### 📊 Módulo Gerencial
- ✅ Relatórios de faturamento por dia e mês
- ✅ Estatísticas de utilização
- ✅ Interface web para consultas

## 🏗️ Arquitetura e Padrões Implementados

### Arquitetura Multicamada
```
┌─────────────────────────┐
│   Camada Apresentação   │  ← MVC Pattern
│   (Controllers + Views) │
├─────────────────────────┤
│   Camada de Serviço     │  ← Business Logic
│   (Services)            │
├─────────────────────────┤
│   Camada de Domínio     │  ← Domain Model Pattern
│   (Entities)            │
├─────────────────────────┤
│   Camada Persistência   │  ← Repository Pattern
│   (Repositories + JPA)  │
└─────────────────────────┘
```

### Padrões de Projeto Utilizados

#### ✅ **MVC (Model-View-Controller)**
- **Controllers**: `TicketController`, `WebController`
- **Views**: Templates Thymeleaf (`index.html`, `cancela.html`, etc.)
- **Models**: DTOs e Domain Objects

#### ✅ **Domain Model**
- **Classe**: `Ticket.java`
- **Características**:
  - Encapsulamento adequado com validações
  - Métodos de domínio (`isPeriodoCortesia()`, `marcarComoPago()`)
  - Regras de negócio encapsuladas na entidade

#### ✅ **Repository Pattern**
- **Interface**: `TicketRepository`
- **Implementação**: Spring Data JPA
- **Funcionalidades**: Abstração da camada de persistência

## 💾 Regras de Negócio

### Cobrança por Tempo
1. **🆓 15 minutos**: Período de cortesia (gratuito)
2. **⏰ Até 1 hora**: R$ 5,00 (valor fixo)
3. **⏰ Acima de 1 hora**: R$ 5,00 + R$ 4,50 por hora adicional

### Fluxo de Operação
1. **Entrada**: Veículo entra → Sistema gera ticket único
2. **Permanência**: Cliente utiliza o estacionamento
3. **Pagamento**: Cliente paga no caixa (se necessário)
4. **Saída**: Sistema valida ticket → Libera cancela

## 🚀 Como Executar

### Pré-requisitos
- Java 17+
- Maven 3.6+

### Passos para Execução
1. **Clone/Download do projeto**
2. **Navegue até o diretório do projeto**
3. **Execute o comando**:
   ```bash
   mvn spring-boot:run
   ```
4. **Acesse no navegador**: `http://localhost:8080`

### Acessos Disponíveis
- **Sistema Web**: `http://localhost:8080`
- **Console H2**: `http://localhost:8080/h2-console`
  - URL: `jdbc:h2:mem:TrabalhoFinal`
  - User: `trabalhoFinalPSA`
  - Password: `trabalhoFinalPSA`

## 🎨 Frontend - Interface Web

### Tecnologias Utilizadas
- **HTML5**: Estruturação semântica das páginas
- **CSS3**: Estilização moderna com gradientes, sombras e responsividade
- **JavaScript**: Interatividade e integração com APIs REST
- **Thymeleaf**: Template engine para renderização server-side

### Arquitetura do Frontend

#### **Single Page Applications (SPA) Híbrida**
Cada módulo foi desenvolvido como uma interface específica, mas com navegação fluida:

```
Frontend Architecture:
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   index.html    │────│  Página Inicial │────│   Navegação     │
│   (Dashboard)   │    │   + Menu        │    │   entre         │
└─────────────────┘    └─────────────────┘    │   Módulos       │
         │                                     │                 │
         ├─── cancela.html    (Cancelas)      │                 │
         ├─── operador.html   (Caixa)         │                 │
         └─── gerencial.html  (Relatórios)    │                 │
                                              └─────────────────┘
```

### Design System Implementado

#### **Paleta de Cores**
```css
:root {
  --primary-gradient: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  --card-background: #ffffff;
  --text-primary: #2d3748;
  --text-secondary: #718096;
  --success-color: #48bb78;
  --warning-color: #ed8936;
  --error-color: #f56565;
}
```

#### **Componentes Reutilizáveis**
- **Cards**: Design consistente com sombras suaves e bordas arredondadas
- **Botões**: Estados hover, focus e active bem definidos
- **Forms**: Validação visual em tempo real
- **Tabelas**: Responsivas com alternância de cores nas linhas

### Características Técnicas

#### **Responsividade**
```css
/* Mobile First Approach */
.container {
  max-width: 600px;
  width: 90%;
}

@media (max-width: 768px) {
  .grid { grid-template-columns: 1fr; }
  .card { padding: 1.5rem; }
}
```

#### **Interatividade JavaScript**
- **AJAX Calls**: Comunicação assíncrona com backend
- **Form Validation**: Validação client-side antes do envio
- **Dynamic Content**: Atualização de conteúdo sem reload
- **Error Handling**: Tratamento elegante de erros de API

### Módulos Frontend Detalhados

#### **1. Dashboard Principal (index.html)**
- **Funcionalidade**: Portal de entrada com navegação para todos os módulos
- **Design**: Cards com ícones intuitivos e gradientes atraentes
- **UX**: Hover effects e transições suaves

#### **2. Interface de Cancelas (cancela.html)**  
- **Entrada**: Formulário para cadastro de placas com validação
- **Saída**: Interface para verificação de tickets
- **Feedback**: Mensagens de sucesso/erro em tempo real

#### **3. Operador de Caixa (operador.html)**
- **Cálculo**: Interface para consulta de valores
- **Pagamento**: Processamento de transações
- **Validação**: Verificação de códigos antes das operações

#### **4. Módulo Gerencial (gerencial.html)**
- **Dashboards**: Visualização de estatísticas em tempo real
- **Relatórios**: Filtros por data com resultados dinâmicos
- **Charts**: Apresentação visual de dados (implementação com CSS puro)

### Integração Frontend-Backend

#### **Padrão de Comunicação**
```javascript
// Exemplo de integração AJAX
async function calcularValor(codigo) {
    try {
        const response = await fetch(`/tickets/calcular/${codigo}`);
        const data = await response.json();
        
        if (data.sucesso) {
            displayValor(data.valor, data.tempo);
        } else {
            showError(data.mensagem);
        }
    } catch (error) {
        showError('Erro de comunicação com o servidor');
    }
}
```

#### **Tratamento de Estados**
- **Loading**: Indicadores visuais durante requisições
- **Success**: Confirmações com feedback positivo  
- **Error**: Mensagens de erro claras e acionáveis
- **Validation**: Validação em tempo real nos formulários

### User Experience (UX)

#### **Princípios Aplicados**
1. **Simplicidade**: Interface clean sem elementos desnecessários
2. **Consistência**: Padrões visuais mantidos em todas as telas
3. **Feedback**: Resposta imediata para todas as ações do usuário
4. **Acessibilidade**: Contrastes adequados e navegação por teclado

#### **Flow de Navegação**
```
Usuário → Dashboard → Seleciona Módulo → Executa Função → Recebe Feedback → Retorna/Continua
```

## 🌐 Endpoints da API REST

### Cancela de Entrada
```http
POST /tickets/entrada
Content-Type: application/json

{
  "placa": "ABC-1234"
}
```

### Cancela de Saída
```http
POST /tickets/saida/{codigo}
```

### Cálculo de Valor
```http
GET /tickets/calcular/{codigo}
```

### Processar Pagamento
```http
POST /tickets/pagar/{codigo}
```

### Relatórios
```http
GET /tickets/relatorio/dia?data=2025-12-01
GET /tickets/relatorio/mes?ano=2025&mes=12
```

## 📁 Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/progsoftaplic/TrabalhoFinal/
│   │   ├── controller/           # Camada Apresentação (MVC)
│   │   │   ├── TicketController.java
│   │   │   ├── WebController.java
│   │   │   └── GlobalExceptionHandler.java
│   │   ├── service/              # Camada Serviço
│   │   │   ├── TicketService.java
│   │   │   └── dto/
│   │   │       └── TicketRequestDTO.java
│   │   ├── domain/               # Camada Domínio (Domain Model)
│   │   │   └── Ticket.java
│   │   ├── repository/           # Camada Persistência (Repository)
│   │   │   └── TicketRepository.java
│   │   └── TrabalhoFinalApplication.java
│   └── resources/
│       ├── templates/            # Views (MVC)
│       │   ├── index.html
│       │   ├── cancela.html
│       │   ├── operador.html
│       │   └── gerencial.html
│       ├── application.properties
│       └── data.sql              # Script de população do BD
```

## 💾 Banco de Dados

### Tecnologia
- **SGBD**: H2 Database (em memória)
- **ORM**: Spring Data JPA + Hibernate

### Schema da Tabela `ticket`
```sql
CREATE TABLE ticket (
    codigo VARCHAR(50) PRIMARY KEY,
    placa VARCHAR(10) NOT NULL,
    entrada TIMESTAMP NOT NULL,
    saida TIMESTAMP,
    pago BOOLEAN NOT NULL DEFAULT FALSE,
    valor DECIMAL(10,2) NOT NULL DEFAULT 0.00
);
```

### Dados de Teste
O arquivo `data.sql` contém dados pré-populados para demonstração:
- 8 tickets de exemplo
- Diferentes status (pagos/não pagos)
- Horários variados para teste de relatórios

## 🧪 Testando o Sistema

### Cenário 1: Entrada e Saída Rápida (Cortesia)
1. Acesse `/cancela`
2. Emita um ticket com placa `TEST-001`
3. Imediatamente teste a saída com o código gerado
4. ✅ **Resultado**: Saída liberada (cortesia)

### Cenário 2: Pagamento no Caixa
1. Emita um ticket e aguarde mais de 15 minutos
2. Acesse `/operador`
3. Calcule o valor usando o código
4. Processe o pagamento
5. Teste a saída em `/cancela`

### Cenário 3: Relatórios Gerenciais
1. Acesse `/gerencial`
2. Visualize estatísticas de hoje
3. Gere relatórios por dia/mês específicos

## 📊 Validação dos Requisitos

### ✅ Requisitos Funcionais Atendidos
- [x] Emissão de tickets na entrada
- [x] Validação de tickets na saída  
- [x] Cálculo de valores por regras definidas
- [x] Interface para operador de caixa
- [x] Módulo gerencial com relatórios
- [x] Serviços web para integração com cancelas

### ✅ Requisitos Técnicos Atendidos
- [x] Arquitetura multicamada
- [x] Padrão MVC na apresentação
- [x] Padrão Domain Model no domínio
- [x] Padrão Repository na persistência
- [x] Persistência em banco relacional
- [x] Mapeamento objeto-relacional (JPA)
- [x] Tratamento de exceções entre camadas
- [x] Base de dados populada com script

## 👥 Equipe de Desenvolvimento

**Trabalho Final - Programação de Software Aplicado**
**Autores: Larissa Oliveira e Maria Eduarda Schüler**

---

## 📝 Observações Técnicas

- Sistema desenvolvido seguindo princípios SOLID
- Arquitetura preparada para escalabilidade
- Código documentado e bem estruturado
- Interface responsiva e intuitiva
- Validações robustas em todas as camadas
- Logs detalhados para debug e monitoramento
