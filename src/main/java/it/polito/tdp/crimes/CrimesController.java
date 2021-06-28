/**
 * Sample Skeleton for 'Crimes.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.crimes.model.Adiacenza;
import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class CrimesController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxCategoria"
    private ComboBox<String> boxCategoria; // Value injected by FXMLLoader

    @FXML // fx:id="boxAnno"
    private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="boxArco"
    private ComboBox<Adiacenza> boxArco; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Crea grafo...\n");
    	boxArco.getItems().clear();
    	
    	Integer anno = boxAnno.getValue();
    	String categoria = boxCategoria.getValue();
    	
    	if(anno == null || categoria == null) {
    		txtResult.appendText("Devi inserire anno e categoria");
    		return;
    	}
    	
    	model.creaGrafo(anno, categoria);
    	
    	txtResult.appendText("Grafo creato!\n");
    	txtResult.appendText("Numero vertici: "+model.getNVertici()+"\n");
    	txtResult.appendText("Numero archi: "+model.getNArchi()+"\n");
    	
    	
    	txtResult.appendText("Archi di peso massimo: \n");
    	for(Adiacenza a: model.getArchiPesoMax(anno, categoria)) {
    		
    		txtResult.appendText("Vertice v1: "+a.getV1()+" VS Vertice v2: "+a.getV2()+", peso: "+a.getPeso()+"\n");
    		
    		
    	}
    	
    	boxArco.getItems().addAll(model.getArchiPesoMax(anno, categoria));
    	
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Calcola percorso...\n");
    	
    	String s1 = boxArco.getValue().getV1();
    	String s2 = boxArco.getValue().getV2();
    	
    	List<String> percorso =model.calcoloPercorso(s1, s2);
    	
    	if(percorso== null) {
    		txtResult.appendText("Nessun percorso disponibile");
    		return;
    	}
    	
    	for(String s: percorso) {
    		
    		txtResult.appendText(s+"\n");
    	}
    	
    	
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxCategoria != null : "fx:id=\"boxCategoria\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxArco != null : "fx:id=\"boxArco\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Crimes.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	boxAnno.getItems().addAll(model.getAllAnni());
    	boxCategoria.getItems().addAll(model.getAllCategorie());
    	
    }
}
