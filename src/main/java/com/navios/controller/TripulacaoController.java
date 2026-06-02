package com.navios.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

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
        System.out.println("Adicionar Tripulante clicado");
    }
}