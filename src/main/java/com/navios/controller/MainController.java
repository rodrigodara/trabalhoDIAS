package com.navios.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class MainController {

    @FXML
    private Button btnNavios;

    @FXML
    private Button btnViagens;

    @FXML
    private Button btnCargas;

    @FXML
    private Button btnTripulacao;

    @FXML
    private AnchorPane mainContent;

    @FXML
    private VBox navButtons;

    @FXML
    private VBox sidebar;

    private boolean expanded = true;
    private static final double SIDEBAR_EXPANDED = 160.0;
    private static final double SIDEBAR_COLLAPSED = 50.0;

    @FXML
    private void handleToggleMenu() {
        expanded = !expanded;
        navButtons.setVisible(expanded);
        navButtons.setManaged(expanded);
        sidebar.setPrefWidth(expanded ? SIDEBAR_EXPANDED : SIDEBAR_COLLAPSED);
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
        carregarView("/fxml/CargaView.fxml");
    }

    @FXML
    private void handleTripulacao() {
        carregarView("/fxml/TripulacaoView.fxml");
    }

    private void carregarView(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent view = loader.load();
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);
            mainContent.getChildren().clear();
            mainContent.getChildren().add(view);
        } catch (Exception e) {
            System.out.println("Erro ao carregar: " + fxml);
            e.printStackTrace();
        }
    }
}
