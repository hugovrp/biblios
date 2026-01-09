package br.hvrp.biblios.listener;

import jakarta.faces.event.PhaseEvent;
import jakarta.faces.event.PhaseId;
import jakarta.faces.event.PhaseListener;

public class AuthListener implements PhaseListener {

	private static final long serialVersionUID = -5107049575279287227L;

	@Override
	public void afterPhase(PhaseEvent event) {
		
	}

	@Override
	public void beforePhase(PhaseEvent event) {
		
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}
}