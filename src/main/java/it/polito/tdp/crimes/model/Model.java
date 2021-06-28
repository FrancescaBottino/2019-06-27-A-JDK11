package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private EventsDao dao;
	private Graph<String, DefaultWeightedEdge> grafo;
	private List<String> soluzioneMigliore;
	private Integer pesoMinimo;
	private Integer dimensioneRaggiungibili;
	
	
	public Model() {
		
		dao = new EventsDao();
		
		
		
	}
	
	public List<String> getAllCategorie(){
		
		List<String> categorie = dao.getAllCategorie();
		Collections.sort(categorie);
		
		return categorie;
		
		
	}
	
	public List<Integer> getAllAnni(){
		
		List<Integer> anni = dao.getAllAnni();
		
		Collections.sort(anni);
		
		return anni;
	}
	
	public void creaGrafo(Integer anno, String categoria) {
		
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		
		// vertici
		
		Graphs.addAllVertices(this.grafo, dao.getVertici(anno, categoria));
		
		//archi 
		
		for (Adiacenza a: dao.getAdiacenze(anno, categoria)) {
			
			Graphs.addEdgeWithVertices(this.grafo, a.getV1(), a.getV2(), a.getPeso());
		}
		
		
	}
	
	//punto d: l’elenco di tutti gli archi il cui peso sia pari al peso massimo
	//presente nel grafo. Per ogni arco si visualizzino i due tipi di reato
	//(i due vertici) ed il peso stesso.
	
	
	public List<Adiacenza> getArchiPesoMax(Integer anno, String categoria){
		
		
		Integer pesoMax=0;
		List<Adiacenza> best = new ArrayList<>();
		
		for(DefaultWeightedEdge e: this.grafo.edgeSet()) {
			
			if((int) this.grafo.getEdgeWeight(e) > pesoMax) {
				
				pesoMax = (int) this.grafo.getEdgeWeight(e);
				best.clear();
				best.add(new Adiacenza(this.grafo.getEdgeSource(e), this.grafo.getEdgeTarget(e), pesoMax));
				
			}
			else if ((int) this.grafo.getEdgeWeight(e) == pesoMax){
				
				pesoMax = (int) this.grafo.getEdgeWeight(e);
				best.add(new Adiacenza(this.grafo.getEdgeSource(e), this.grafo.getEdgeTarget(e), pesoMax));
				
				
			}
			
		}
		
		return best;
		
	}

	
	

	public Integer getNVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public Integer getNArchi() {
		return this.grafo.edgeSet().size();
	}
	
	
	public List<String> calcoloPercorso(String inizio, String fine) {
		
		this.soluzioneMigliore = null;
		
		ConnectivityInspector<String, DefaultWeightedEdge> ci = new ConnectivityInspector<>(grafo);
		this.dimensioneRaggiungibili = ci.connectedSetOf(inizio).size()-1;
		
		
		List<String> parziale = new ArrayList<String>();
		parziale.add(inizio);
		
		pesoMinimo = 999999;
		
		cerca(parziale, pesoMinimo, fine);
		
		return soluzioneMigliore;
		
		
	}

	private void cerca(List<String> parziale, Integer pesoMinimo, String fine) {
		
		
		//casi terminali 
		
		int sommaPesi = calcolaPesoTot(parziale);
		
		
		if(parziale.size() == dimensioneRaggiungibili) {
			
			//cerca se l'ultimo è giusto
			
			if(parziale.get(parziale.size()-1).equals(fine)) {
				
				//cerca se è di peso minimo
			
				if(sommaPesi < pesoMinimo) {
				
					pesoMinimo = sommaPesi;
					soluzioneMigliore = new ArrayList<>(parziale);
					
				}
				
			}
			return;	
		}
		
			
		
	
		//genero sottoproblemi
		
		for(String vicino: Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1))) {
			
			if(!parziale.contains(vicino)) {
				parziale.add(vicino);
				cerca(parziale, pesoMinimo, fine);
				parziale.remove(parziale.size()-1);
			}
			
			
		}
				
		
	}
	
		private int calcolaPesoTot(List<String> parziale) {
		
		int peso=0;
		
		//calcolo il peso della soluzione parziale
		
		for(int i = 0; i<parziale.size(); i++) {
			
			
			String s1 = parziale.get(i);
			
			if (s1.equals(parziale.get(parziale.size()-1))) {
				return peso;
			}
			
			String s2 = parziale.get(i+1);
			
			
			if(s1 != null && s2 != null) {
				peso += grafo.getEdgeWeight(grafo.getEdge(s1, s2));
				
			}
			
			
		}
		
		
		return peso;
	}
	
	
	
}
