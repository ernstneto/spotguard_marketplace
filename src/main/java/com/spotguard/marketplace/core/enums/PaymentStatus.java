package com.spotguard.marketplace.core.enums;

/**
 * Status de um pagamento no sistema.
 */
public enum PaymentStatus {

    /** Aguardando confirmação do gateway. */
    PENDING,

    /** Pagamento confirmado. */
    COMPLETED,

    /** Pagamento falhou (cartão recusado, saldo insuficiente, etc.). */
    FAILED,

    /** Reembolsado ao locatário. */
    REFUNDED,

    /** Em custódia (escrow) — valor retido até o início da locação. */
    IN_ESCROW
}
