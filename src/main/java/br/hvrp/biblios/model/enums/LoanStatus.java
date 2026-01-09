package br.hvrp.biblios.model.enums;

public enum LoanStatus {
	ACTIVE,     // Empréstimo em andamento (dentro do prazo)
    OVERDUE,    // Prazo de 7 dias expirado e ainda não devolvido
    RETURNED,   // Revista já devolvida
    CANCELLED   // Caso o empréstimo seja anulado por algum motivo
}
