-- Tickets
INSERT INTO TICKET (codigo, placa, entrada, saida, pago, valor) VALUES
('ABC123', 'AAA-1111', '2025-12-01T08:00:00', '2025-12-01T09:30:00', TRUE, 9.50),
('DEF456', 'BBB-2222', '2025-12-01T07:45:00', '2025-12-01T08:10:00', TRUE, 0.00),
('GHI789', 'CCC-3333', '2025-12-01T06:30:00', '2025-12-01T08:00:00', TRUE, 9.50),
('JKL012', 'DDD-4444', '2025-12-01T09:00:00', NULL, FALSE, 0.00);

-- Pagamentos (ligados por ticket_codigo)
INSERT INTO PAGAMENTO (ticket_codigo, valor, data_pagamento) VALUES
('ABC123', 9.50, '2025-12-01T09:30:00'),
('DEF456', 0.00, '2025-12-01T08:10:00'),
('GHI789', 9.50, '2025-12-01T08:00:00');