package com.navios.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class ViagemController {
    @FXML
    private TableView<?> tabelaViagens;

    @FXML
    private Button btnAdicionarViagem;

    @FXML
    private Button btnSearchViagem;

    @FXML
    private void handleSearchViagem() {
        System.out.println("Search Viagem clicado");
    }

        @FXML
    private void handleAdicionarViagem() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdicionarViagemView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Adicionar Viagem");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}