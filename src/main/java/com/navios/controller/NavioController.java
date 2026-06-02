package com.navios.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

public class NavioController {
    @FXML
    private TableView<?> tabelaNavios;

    @FXML
    private Button btnAdicionarNavio;

    @FXML
    private Button btnSearchNavio;

    @FXML
    private void handleSearchNavio() {
        System.out.println("Search Navio clicado");
    }
    @FXML
    private void handleAdicionarNavio() {
        System.out.println("Adicionar Navio clicado");
    }
}