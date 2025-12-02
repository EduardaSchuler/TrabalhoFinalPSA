# 🗄️ Diagrama da Base Relacional - Sistema de Estacionamento

## 📊 Esquema da Base de Dados

### Tabela: **TICKET**
```sql
┌─────────────────────────────────────────────────────────────┐
│                           TICKET                            │
├─────────────────────────────────────────────────────────────┤
│ 🔑 CODIGO          VARCHAR(255)     PRIMARY KEY            │
│ 📅 ENTRADA         TIMESTAMP        NOT NULL               │ 
│ 🚗 PLACA           VARCHAR(255)     NOT NULL               │
│ ✅ PAGO            BOOLEAN          DEFAULT FALSE          │
│ 📅 SAIDA           TIMESTAMP        NULL                   │
│ 💰 VALOR           DECIMAL(19,2)    NULL                   │
└─────────────────────────────────────────────────────────────┘
```

### Tabela: **PAGAMENTO**
```sql
┌─────────────────────────────────────────────────────────────┐
│                         PAGAMENTO                           │
├─────────────────────────────────────────────────────────────┤
│ 🔑 ID              BIGINT          PRIMARY KEY AUTO_INC    │
│ 📅 DATA_PAGAMENTO  TIMESTAMP       NOT NULL               │
│ 🎫 TICKET_CODIGO   VARCHAR(255)    NOT NULL               │
│ 💰 VALOR           DECIMAL(19,2)   NOT NULL               │
└─────────────────────────────────────────────────────────────┘
```

---

## 🔗 Relacionamentos Entre Tabelas

### **Relacionamento Lógico: TICKET ←→ PAGAMENTO**

```
TICKET (1) ────────────── (0..*) PAGAMENTO
   │                              │
   │ CODIGO                       │ TICKET_CODIGO
   └──────────────────────────────┘
```

**Tipo de Relacionamento:** One-to-Many (1:N)
- **Um ticket** pode ter **zero ou vários pagamentos**
- **Um pagamento** pertence a **exatamente um ticket**

**Implementação:**
- A tabela `PAGAMENTO` possui a chave estrangeira `TICKET_CODIGO` que referencia `TICKET.CODIGO`
- A relação é implementada logicamente através do campo `ticketCodigo` na entidade `Pagamento`

---

## 🔧 Constraints e Índices

### **Chaves Primárias**
- `TICKET.CODIGO`: Identificador único do ticket (String)
- `PAGAMENTO.ID`: Identificador sequencial auto-incrementado (Long)

### **Restrições de Integridade**
- `TICKET.ENTRADA`: NOT NULL (todo ticket deve ter horário de entrada)
- `TICKET.PLACA`: NOT NULL (todo ticket deve ter placa do veículo)
- `TICKET.PAGO`: DEFAULT FALSE (tickets iniciam como não pagos)
- `PAGAMENTO.TICKET_CODIGO`: NOT NULL (todo pagamento deve referenciar um ticket)
- `PAGAMENTO.VALOR`: NOT NULL (todo pagamento deve ter valor)
- `PAGAMENTO.DATA_PAGAMENTO`: NOT NULL (todo pagamento deve ter timestamp)

### **Índices Recomendados**
```sql
-- Índice para consultas por placa
CREATE INDEX idx_ticket_placa ON TICKET(PLACA);

-- Índice para consultas por período (relatórios)
CREATE INDEX idx_ticket_saida ON TICKET(SAIDA);
CREATE INDEX idx_ticket_entrada ON TICKET(ENTRADA);

-- Índice para consultas de pagamentos por período
CREATE INDEX idx_pagamento_data ON PAGAMENTO(DATA_PAGAMENTO);

-- Índice para relacionamento com ticket
CREATE INDEX idx_pagamento_ticket_codigo ON PAGAMENTO(TICKET_CODIGO);
```

---

## 📋 Estrutura Detalhada dos Campos

### **Tabela TICKET**

| Campo    | Tipo          | Tamanho | Nulo | Default | Descrição                           |
|----------|---------------|---------|------|---------|-------------------------------------|
| CODIGO   | VARCHAR       | 255     | NO   | -       | Código único gerado para o ticket  |
| ENTRADA  | TIMESTAMP     | -       | NO   | -       | Data/hora de entrada no estacionamento |
| PLACA    | VARCHAR       | 255     | NO   | -       | Placa do veículo                   |
| PAGO     | BOOLEAN       | -       | NO   | FALSE   | Status de pagamento do ticket      |
| SAIDA    | TIMESTAMP     | -       | YES  | NULL    | Data/hora de saída (quando aplicável) |
| VALOR    | DECIMAL       | 19,2    | YES  | NULL    | Valor cobrado pelo estacionamento  |

### **Tabela PAGAMENTO**

| Campo         | Tipo          | Tamanho | Nulo | Default | Descrição                          |
|---------------|---------------|---------|------|---------|------------------------------------|
| ID            | BIGINT        | -       | NO   | AUTO    | Identificador único do pagamento   |
| DATA_PAGAMENTO| TIMESTAMP     | -       | NO   | -       | Data/hora do pagamento            |
| TICKET_CODIGO | VARCHAR       | 255     | NO   | -       | Código do ticket pago             |
| VALOR         | DECIMAL       | 19,2    | NO   | -       | Valor pago                        |

---

## 💾 Scripts de Criação das Tabelas

### **DDL - Data Definition Language**

```sql
-- Criação da tabela TICKET
CREATE TABLE TICKET (
    CODIGO VARCHAR(255) PRIMARY KEY,
    ENTRADA TIMESTAMP NOT NULL,
    PLACA VARCHAR(255) NOT NULL,
    PAGO BOOLEAN DEFAULT FALSE NOT NULL,
    SAIDA TIMESTAMP,
    VALOR DECIMAL(19,2)
);

-- Criação da tabela PAGAMENTO  
CREATE TABLE PAGAMENTO (
    ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    DATA_PAGAMENTO TIMESTAMP NOT NULL,
    TICKET_CODIGO VARCHAR(255) NOT NULL,
    VALOR DECIMAL(19,2) NOT NULL
);

-- Índices para performance
CREATE INDEX idx_ticket_placa ON TICKET(PLACA);
CREATE INDEX idx_ticket_saida ON TICKET(SAIDA);
CREATE INDEX idx_ticket_entrada ON TICKET(ENTRADA);
CREATE INDEX idx_pagamento_data ON PAGAMENTO(DATA_PAGAMENTO);
CREATE INDEX idx_pagamento_ticket_codigo ON PAGAMENTO(TICKET_CODIGO);
```

---

## 📊 Exemplo de Dados

### **Dados da Tabela TICKET**
```
┌──────────┬─────────────────────┬──────────┬──────┬─────────────────────┬──────────┐
│ CODIGO   │ ENTRADA             │ PLACA    │ PAGO │ SAIDA               │ VALOR    │
├──────────┼─────────────────────┼──────────┼──────┼─────────────────────┼──────────┤
│ TKT001   │ 2024-01-15 08:30:00 │ ABC-1234 │ true │ 2024-01-15 10:45:00 │ 15.00    │
│ TKT002   │ 2024-01-15 09:15:00 │ DEF-5678 │ false│ NULL                │ NULL     │
│ TKT003   │ 2024-01-15 10:20:00 │ GHI-9012 │ true │ 2024-01-15 12:30:00 │ 10.00    │
└──────────┴─────────────────────┴──────────┴──────┴─────────────────────┴──────────┘
```

### **Dados da Tabela PAGAMENTO**
```
┌────┬─────────────────────┬──────────────┬────────┐
│ ID │ DATA_PAGAMENTO      │ TICKET_CODIGO│ VALOR  │
├────┼─────────────────────┼──────────────┼────────┤
│ 1  │ 2024-01-15 10:40:00 │ TKT001       │ 15.00  │
│ 2  │ 2024-01-15 12:25:00 │ TKT003       │ 10.00  │
└────┴─────────────────────┴──────────────┴────────┘
```

---

## 🎯 Consultas Típicas do Sistema

### **1. Buscar Ticket por Código**
```sql
SELECT * FROM TICKET WHERE CODIGO = 'TKT001';
```

### **2. Consultar Tickets Pagos por Período**
```sql
SELECT t.*, p.DATA_PAGAMENTO, p.VALOR as VALOR_PAGO
FROM TICKET t 
JOIN PAGAMENTO p ON t.CODIGO = p.TICKET_CODIGO
WHERE p.DATA_PAGAMENTO BETWEEN '2024-01-01' AND '2024-01-31'
ORDER BY p.DATA_PAGAMENTO ASC;
```

### **3. Relatório de Receita por Período**
```sql
SELECT 
    COUNT(*) as TOTAL_TICKETS_PAGOS,
    SUM(VALOR) as RECEITA_TOTAL,
    AVG(VALOR) as VALOR_MEDIO
FROM PAGAMENTO 
WHERE DATA_PAGAMENTO BETWEEN '2024-01-01' AND '2024-01-31';
```

### **4. Tickets em Aberto (não pagos)**
```sql
SELECT CODIGO, PLACA, ENTRADA, 
       TIMESTAMPDIFF(MINUTE, ENTRADA, NOW()) as MINUTOS_ESTACIONADO
FROM TICKET 
WHERE PAGO = FALSE
ORDER BY ENTRADA ASC;
```

### **5. Histórico de Tickets por Placa**
```sql
SELECT CODIGO, ENTRADA, SAIDA, PAGO, VALOR
FROM TICKET 
WHERE PLACA = 'ABC-1234'
ORDER BY ENTRADA DESC;
```

---

## 🔄 Ciclo de Vida dos Dados

### **Fluxo Normal de um Ticket:**

1. **Entrada do Veículo:**
   ```sql
   INSERT INTO TICKET (CODIGO, ENTRADA, PLACA, PAGO) 
   VALUES ('TKT001', NOW(), 'ABC-1234', FALSE);
   ```

2. **Cálculo do Valor:**
   ```sql
   UPDATE TICKET SET VALOR = 15.00 WHERE CODIGO = 'TKT001';
   ```

3. **Pagamento:**
   ```sql
   -- Marcar ticket como pago
   UPDATE TICKET SET PAGO = TRUE WHERE CODIGO = 'TKT001';
   
   -- Registrar pagamento
   INSERT INTO PAGAMENTO (TICKET_CODIGO, VALOR, DATA_PAGAMENTO)
   VALUES ('TKT001', 15.00, NOW());
   ```

4. **Saída do Veículo:**
   ```sql
   UPDATE TICKET SET SAIDA = NOW() WHERE CODIGO = 'TKT001';
   ```

---

## 🛡️ Considerações de Segurança e Performance

### **Integridade Referencial**
- A relação entre `TICKET` e `PAGAMENTO` é mantida através do campo `TICKET_CODIGO`
- Embora não haja foreign key declarada no H2, a aplicação garante a integridade

### **Performance**
- Índices criados nos campos mais consultados (PLACA, SAIDA, DATA_PAGAMENTO)
- Consultas otimizadas para relatórios por período
- Uso de TIMESTAMP para consultas eficientes por data/hora

### **Escalabilidade**
- Estrutura preparada para grandes volumes de tickets
- Possibilidade de particionamento por data no futuro
- Campos DECIMAL para precisão monetária

### **Backup e Arquivamento**
- Dados históricos podem ser arquivados periodicamente
- Estrutura permite fácil migração para outros SGBDs