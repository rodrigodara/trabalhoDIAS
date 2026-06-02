package com.navios.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

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
        System.out.println("Adicionar Carga clicado");
    }
}