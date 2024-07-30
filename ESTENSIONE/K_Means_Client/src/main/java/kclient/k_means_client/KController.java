package kclient.k_means_client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import kclient.k_means_client.exception.ServerException;

import java.io.IOException;

public class KController {

    private Stage stage;
    private Scene scene;
    private FXMLLoader fx;
    // TextField apparteneti a IP-Port
    @FXML
    private TextField IpTextField;
    @FXML
    private TextField PortTextField;
    // TextField apparteneti a TabNumCluster
    @FXML
    private TextField TabellaTextField;
    @FXML
    private TextField ClusterTextField;
    // TextField apparteneti a TabNumClusterCaricaDati
    @FXML
    private TextField TabellaCaricaDatiTextField;
    @FXML
    private TextField ClusterCaricaDatiTextField;
    // Label appartiene a IP-Port
    @FXML
    private Label ErrorLabel;
    //Label appartiene a Risultati
    @FXML
    private ScrollPane Risultati;
    @FXML
    private Label DiServizio;

    public void SwitchtoIP_Port(ActionEvent event) throws IOException {
        fx = new FXMLLoader(KController.class.getResource("IP-Port.fxml"));
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        scene= new Scene(fx.load());
        stage.setScene(scene);
        stage.show();
    }
    public void SwitchtoRisultati(ActionEvent event) throws IOException {
        fx = new FXMLLoader(KController.class.getResource("Risultati.fxml"));
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        scene= new Scene(fx.load());
        stage.setScene(scene);
        stage.show();
    }
    public void SwitchTabNumClusterCaricaFile(ActionEvent event) throws IOException {
        fx = new FXMLLoader(KController.class.getResource("TabNumCluster.fxml"));
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        scene= new Scene(fx.load());
        stage.setScene(scene);
        stage.show();
    }
    public void SwitchTabNumClusterCaricaDati(ActionEvent event) throws IOException {
        fx = new FXMLLoader(KController.class.getResource("TabNumClusterCaricaDati.fxml"));
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        scene= new Scene(fx.load());
        stage.setScene(scene);
        stage.show();
    }

    public void SwitchMenu(ActionEvent event) throws IOException {
        try{
        fx = new FXMLLoader(KController.class.getResource("Menu.fxml"));
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        scene= new Scene(fx.load());
        stage.setScene(scene);
        stage.show();
        }catch (IOException e){
            System.out.println("Errore:"+e.getMessage());
        }

    }

    @FXML
    public void ControlloIP_Port(ActionEvent event) {
        try {
            String s = IpTextField.getText();
            Integer i = Integer.parseInt(PortTextField.getText());
            MainTest.createInstance(s,i);
            SwitchMenu(event);
        }catch(IOException e){
            ErrorLabel.setText("ERRORE NEL INSERIMENTO");
        }
    }



    public void learningFromFile(ActionEvent event) throws ServerException, IOException, ClassNotFoundException {
        MainTest.getInstance().getOut().writeObject(3);
        String tableName = TabellaTextField.getText();
        MainTest.getInstance().getOut().writeObject(tableName);
        int numCluster = Integer.parseInt(ClusterTextField.getText());
        MainTest.getInstance().getOut().writeObject(numCluster);
        String result = (String) MainTest.getInstance().getIn().readObject();
        if (result.equals("OK")){
            ContenitoreRisultati.setRisultato((String) MainTest.getInstance().getIn().readObject());
            SwitchtoRisultati(event);
        } else {
            throw new ServerException(result);
        }
    }

    public void storeTableFromDb(ActionEvent event) throws ServerException, IOException, ClassNotFoundException {
        MainTest.getInstance().getOut().writeObject(0);
        String tabName = TabellaCaricaDatiTextField.getText();
        MainTest.getInstance().getOut().writeObject(tabName);
        String result = (String) MainTest.getInstance().getIn().readObject();
        if (!result.equals("OK")) {
            throw new ServerException(result);
        }
        String Stampa = learningFromDbTable(Integer.parseInt(ClusterCaricaDatiTextField.getText()));
        ContenitoreRisultati.setRisultato(Stampa);
        storeClusterInFile();
        SwitchtoRisultati(event);
    }

    public String learningFromDbTable(Integer i) throws ServerException, IOException, ClassNotFoundException {
        MainTest.getInstance().getOut().writeObject(1);
        int k = i;
        MainTest.getInstance().getOut().writeObject(k);
        String result = (String) MainTest.getInstance().getIn().readObject();
        if (result.equals("OK")) {
            System.out.println("\nClustering output:" + MainTest.getInstance().getIn().readObject());
            return (String) MainTest.getInstance().getIn().readObject();
        } else
            throw new ServerException(result);
    }

    private void storeClusterInFile() throws ServerException, IOException, ClassNotFoundException {
        MainTest.getInstance().getOut().writeObject(2);
        String result = (String) MainTest.getInstance().getIn().readObject();
        if (!result.equals("OK"))
            throw new ServerException(result);
    }

    @FXML
    private void MostraRisultati(){
        DiServizio.setText(ContenitoreRisultati.getRisultato());
        Risultati.setContent(DiServizio);}

    @FXML
    private void RitornaMenu(ActionEvent event)throws IOException{SwitchMenu(event);}
}
