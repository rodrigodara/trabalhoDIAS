package com.navios.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class TripulacaoController {
    @FXML
    private TableView<?> tabelaTripulantes;

    @FXML
    private Button btnAdicionarTripulante;

    @FXML
    private Button btnSearchTripulante;

    @FXML
    private void handleSearchTripulante() {
        System.out.println("Search Tripulante clicado");
    }
   @FXML
    private void handleAdicionarTripulante() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdicionarTripulacaoView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Adicionar Tripulante");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}