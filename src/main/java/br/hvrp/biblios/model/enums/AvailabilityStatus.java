package br.hvrp.biblios.model.enums;

public enum AvailabilityStatus {

	/** A revista está na caixa e pronta para ser emprestada. */
    AVAILABLE,
    
    /** A revista está com um amigo no momento. */
    BORROWED,
    
    /** * Opcional: A revista está reservada para alguém, 
     * mas ainda não foi retirada. 
     */
    RESERVED,
    
    /** * Opcional: A revista foi perdida ou está danificada 
     * e não pode ser emprestada. 
     */
    UNAVAILABLE
}
