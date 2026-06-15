package com.navios.controller;

import java.io.IOException;
import java.util.List;

import com.navios.DB.TripulanteDAO;
import com.navios.model.Tripulante;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class TripulanteController {

    @FXML private TableView<Tripulante> tabelaTripulantes;
    @FXML private TableColumn<Tripulante, String> colNome;
    @FXML private TableColumn<Tripulante, String> colSobrenome;
    @FXML private TableColumn<Tripulante, String> colEstadoDisponibilidade;
    @FXML private TableColumn<Tripulante, String> colNacionalidade;
    @FXML private TableColumn<Tripulante, String> colDataNascimento;
    @FXML private TextField txtPesquisa;

    private List<Tripulante> todosTripulantes;

    @FXML
    private void initialize() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colSobrenome.setCellValueFactory(new PropertyValueFactory<>("sobrenome"));
        colEstadoDisponibilidade.setCellValueFactory(new PropertyValueFactory<>("estadoDisponibilidade"));
        colNacionalidade.setCellValueFactory(new PropertyValueFactory<>("nacionalidade"));
        colDataNascimento.setCellValueFactory(new PropertyValueFactory<>("dataNascimento"));

        carregarTripulantes();
    }

    private void carregarTripulantes() {
        todosTripulantes = new TripulanteDAO().listarTripulantes();
        tabelaTripulantes.setItems(FXCollections.observableArrayList(todosTripulantes));
    }

    @FXML
    private void handleSearchTripulante() {
        String pesquisa = txtPesquisa.getText().toLowerCase();

        if (pesquisa.isEmpty()) {
            tabelaTripulantes.setItems(FXCollections.observableArrayList(todosTripulantes));
            return;
        }

        List<Tripulante> filtrados = todosTripulantes.stream()
            .filter(t -> t.getNome().toLowerCase().contains(pesquisa)
                      || t.getSobrenome().toLowerCase().contains(pesquisa))
            .toList();

        tabelaTripulantes.setItems(FXCollections.observableArrayList(filtrados));
    }

    @FXML
    private void handleAdicionarTripulante() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdicionarTripulanteView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Adicionar Tripulante");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            carregarTripulantes();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}