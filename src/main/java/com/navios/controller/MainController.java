package com.navios.controller;

import com.navios.DB.NavioDAO;
import com.navios.DB.ViagemDAO;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class MainController {

    @FXML private Button btnNavios;
    @FXML private Button btnViagens;
    @FXML private Button btnCargas;
    @FXML private Button btnTripulacao;
    @FXML private Button btnInicio;

    @FXML private AnchorPane mainContent;
    @FXML private VBox navButtons;
    @FXML private VBox sidebar;
    @FXML private VBox dashboardView;
    @FXML private Label lblNomeApp;

    @FXML private Label lblTotalNavios;
    @FXML private Label lblTotalViagens;
    @FXML private Label lblTotalCargas;
    @FXML private Label lblTotalTripulacao;



    private boolean expanded = true;
    private static final double SIDEBAR_EXPANDED = 160.0;
    private static final double SIDEBAR_COLLAPSED = 50.0;

     @FXML
    public void initialize() {
        carregarDashboard();
    }

    

    private void carregarDashboard() {
        NavioDAO dao = new NavioDAO();
        ViagemDAO viagemDAO = new ViagemDAO();
        lblTotalNavios.setText(String.valueOf(dao.listarNavios().size()));
        lblTotalViagens.setText(String.valueOf(new ViagemDAO().listarViagens().size()));
    }

    @FXML
        private void handleToggleMenu() {
            expanded = !expanded;
            navButtons.setVisible(expanded);
            navButtons.setManaged(expanded);
            lblNomeApp.setVisible(expanded);
            lblNomeApp.setManaged(expanded);
            sidebar.setPrefWidth(expanded ? SIDEBAR_EXPANDED : SIDEBAR_COLLAPSED);
        }
    @FXML
    private void handleInicio() {
        // ✅ repõe o dashboard sem carregar nenhum FXML
        mainContent.getChildren().setAll(dashboardView);
        AnchorPane.setTopAnchor(dashboardView, 0.0);
        AnchorPane.setBottomAnchor(dashboardView, 0.0);
        AnchorPane.setLeftAnchor(dashboardView, 0.0);
        AnchorPane.setRightAnchor(dashboardView, 0.0);
        carregarDashboard();
    }

    @FXML
    private void handleNavios() {
        carregarView("/fxml/NavioView.fxml");
    }

    @FXML
    private void handleViagens() {
        carregarView("/fxml/ViagemView.fxml");
    }

    @FXML
    private void handleCargas() {
        carregarView("/fxml/CargasView.fxml");
    }

    @FXML
    private void handleTripulacao() {
        carregarView("/fxml/TripulanteView.fxml");
    }

    private void carregarView(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent view = loader.load();
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);
            mainContent.getChildren().setAll(view);
        } catch (Exception e) {
            System.out.println("Erro ao carregar: " + fxml);
            e.printStackTrace();
        }
    }
}