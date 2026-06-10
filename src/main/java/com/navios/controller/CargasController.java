package com.navios.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class CargasController {

    @FXML
    private TableView<?> tabelaCargas;

    @FXML
    private Button btnAdicionarCarga;

    @FXML
    private Button btnSearchCarga;

    @FXML
    private void handleSearchCarga() {
        System.out.println("Search Carga clicado");
    }
   @FXML
    private void handleAdicionarCarga() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdicionarCargasView.fxml"));
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