-- Script para popular o banco com dados de teste
-- Execute este script no H2 Console (http://localhost:8080/h2-console)

-- Inserindo tickets de teste com diferentes horários
INSERT INTO ticket (codigo, placa, entrada, pago, saida, valor) VALUES 
('TK001', 'ABC1234', '2025-12-01 08:00:00', true, '2025-12-01 10:30:00', 5.00),
('TK002', 'DEF5678', '2025-12-01 09:15:00', true, '2025-12-01 12:45:00', 14.50),
('TK003', 'GHI9012', '2025-12-01 14:20:00', false, null, 0.00),
('TK004', 'JKL3456', '2025-12-01 07:30:00', true, '2025-12-01 18:00:00', 52.50),
('TK005', 'MNO7890', '2025-12-01 16:45:00', false, null, 0.00),
('TK006', 'PQR1122', '2025-11-30 10:00:00', true, '2025-11-30 11:15:00', 5.00),
('TK007', 'STU3344', '2025-11-30 13:30:00', true, '2025-11-30 16:20:00', 9.50),
('TK008', 'VWX5566', '2025-11-29 08:15:00', true, '2025-11-29 20:30:00', 60.00),
('TK009', 'YZA7788', '2025-11-29 11:45:00', true, '2025-11-29 13:00:00', 5.00),
('TK010', 'BCD9900', '2025-11-28 15:20:00', true, '2025-11-28 17:55:00', 9.50);

-- Inserindo pagamentos correspondentes aos tickets pagos
INSERT INTO pagamento (data_pagamento, ticket_codigo, valor) VALUES
('2025-12-01 10:25:00', 'TK001', 5.00),
('2025-12-01 12:40:00', 'TK002', 14.50),
('2025-12-01 17:55:00', 'TK004', 52.50),
('2025-11-30 11:10:00', 'TK006', 5.00),
('2025-11-30 16:15:00', 'TK007', 9.50),
('2025-11-29 20:25:00', 'TK008', 60.00),
('2025-11-29 12:55:00', 'TK009', 5.00),
('2025-11-28 17:50:00', 'TK010', 9.50);