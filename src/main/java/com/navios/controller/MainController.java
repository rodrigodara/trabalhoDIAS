package com.navios.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

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
            mainContent.getChildren().clear();
            mainContent.getChildren().add(view);
        } catch (Exception e) {
            System.out.println("Erro ao carregar: " + fxml);
            e.printStackTrace();
        }
    }
}