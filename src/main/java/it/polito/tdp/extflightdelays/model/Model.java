package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
	
	
	
	// crep una Mappa, per lavorare più e più volte
	// con gli aeroporti, associo ad ogni id dell'aeroporto tutte le informazioni
	// sull'aeroporto stesso
	
	private Graph<Airport, DefaultWeightedEdge> grafo;
	private ExtFlightDelaysDAO dao;
	private Map<Integer, Airport> idMap;
	
	
	// prima di tutto inizializzo il modello e inserisco i dati nella mappa
	public Model() {
	
		this.grafo = new SimpleWeightedGraph (DefaultWeightedEdge.class);
		this.dao = new ExtFlightDelaysDAO();
		this.idMap = new HashMap<Integer, Airport>();
		
		// dobbiamo chiedere al dao di prendere tutti gli airport e metterli nella mappa
		
		this.dao.loadAllAirports(idMap);
	}
	
	
	// creo il grafo
	
	public void creaGrafo(int nAirlines) {
		
		// reset grafo
		this.grafo = new SimpleWeightedGraph (DefaultWeightedEdge.class);
		// essendo che gli edge sono non orientati, pesati, viaggiano anche in direzioni opposte
		
		Graphs.addAllVertices(grafo, this.dao.getVertici(nAirlines, idMap));
		
		List<Rotta> edges = this.dao.getRotte(idMap);
		
		for (Rotta e : edges) {
			Airport origin = e.getOrigin();
			Airport destination = e.getDestination();
			int N = e.getN();
			
			
			// controllo se gli aerporti origin e desitnation siano tra i vertici
			
			if (grafo.vertexSet().contains(origin)
					&& grafo.vertexSet().contains(destination)) {
				
				// controllo se l'edge esiste oppure no
				DefaultWeightedEdge edge = this.grafo.getEdge(origin, destination);
				if ( edge != null) {
					double weight = this.grafo.getEdgeWeight(edge);
					weight += N;
					this.grafo.setEdgeWeight(origin, destination, weight);
					
				} else {
					this.grafo.addEdge(origin, destination);
					this.grafo.setEdgeWeight(origin, destination, N);
				}
			}
			
			
			
		}
		
		
		System.out.println("Grafo creato.");
		System.out.println("Ci sono " + this.grafo.vertexSet().size() + " vertici.");
		System.out.println("Ci sono " + this.grafo.edgeSet().size() + " edges.");
		
		
		
	}
	
	/**
	 * Metodo getter che restituisce i vertici del grafo. Serve per popolare le tendine 
	 * nell'interfaccia grafica, una volta che il grafo é stato creato
	 * @return
	 */
	public List<Airport> getVertici(){
		List<Airport> vertici = new ArrayList<>(this.grafo.vertexSet());
		Collections.sort(vertici);
		return vertici;
	}
	
	
	
	
	/**
	 * Metodo per verificare se due aeroporti sono connessi nel grafo, e quindi se esiste un percorso tra i due
	 * @param origin
	 * @param destination
	 * @return
	 */
	
	public boolean esistePercorso(Airport origin, Airport destination) {
		ConnectivityInspector<Airport, DefaultWeightedEdge> inspect = new ConnectivityInspector<Airport, DefaultWeightedEdge>(this.grafo);
		Set<Airport> componenteConnessaOrigine = inspect.connectedSetOf(origin);
		return componenteConnessaOrigine.contains(destination);
	}
	
	
	
	
	/**
	 * Metodo che calcola il percorso tra due aeroporti. Se il percorso non viene trovato, 
	 * il metodo restituisce null.
	 * @param origin
	 * @param destination
	 * @return
	 */
	public List<Airport> trovaPercorso(Airport origin, Airport destination){
		List<Airport> percorso = new ArrayList<>();
	 	BreadthFirstIterator<Airport,DefaultWeightedEdge> it = new BreadthFirstIterator<>(this.grafo, origin);
	 	Boolean trovato = false;
	 	
	 	//visito il grafo fino alla fine o fino a che non trovo la destinazione
	 	while(it.hasNext() & !trovato) {
	 		Airport visitato = it.next();
	 		if(visitato.equals(destination))
	 			trovato = true;
	 	}
	 
	 
	 	/* se ho trovato la destinazione, costruisco il percorso risalendo l'albero di visita in senso
	 	 * opposto, ovvero partendo dalla destinazione fino ad arrivare all'origine, ed aggiiungo gli aeroporti
	 	 * ad ogni step IN TESTA alla lista
	 	 * se non ho trovato la destinazione, restituisco null.
	 	 */
	 	if(trovato) {
	 		percorso.add(destination);
	 		Airport step = it.getParent(destination);
	 		while (!step.equals(origin)) {
	 			percorso.add(0,step);
	 			step = it.getParent(step);
	 		}
		 
		 percorso.add(0,origin);
		 return percorso;
	 	} else {
	 		return null;
	 	}
		
	}
	
	
	
}



















