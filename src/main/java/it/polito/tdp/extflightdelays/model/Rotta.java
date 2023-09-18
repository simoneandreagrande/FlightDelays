package it.polito.tdp.extflightdelays.model;

import java.util.Objects;

public class Rotta {

	
	// questa classe rappresenta gli edges con il loro relativo peso
	
	// creo costruttore, hash, equals, getters, setters
	
	// se nel dao facciamo un metodo per leggere le rotte, usiamo questo metodo per memorizzarle
	// facciamo dunque una lista di rotte
	
	private Airport origin;
	private Airport destination;
	int N;
	public Rotta(Airport origin, Airport destination, int n) {
		super();
		this.origin = origin;
		this.destination = destination;
		N = n;
	}
	public Airport getOrigin() {
		return origin;
	}
	public void setOrigin(Airport origin) {
		this.origin = origin;
	}
	public Airport getDestination() {
		return destination;
	}
	public void setDestination(Airport destination) {
		this.destination = destination;
	}
	public int getN() {
		return N;
	}
	public void setN(int n) {
		N = n;
	}
	@Override
	public int hashCode() {
		return Objects.hash(N, destination, origin);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rotta other = (Rotta) obj;
		return N == other.N && Objects.equals(destination, other.destination) && Objects.equals(origin, other.origin);
	}
	
	
	
	
}
