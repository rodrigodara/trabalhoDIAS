package com.navios.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

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
        System.out.println("Adicionar Viagem clicado");
    }
}