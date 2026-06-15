package com.navios.controller;

import java.io.IOException;
import java.util.List;

import com.navios.DB.CargasDAO;
import com.navios.model.Carga;

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

public class CargasController {

    @FXML private TableView<Carga> tabelaCargas;
    @FXML private TableColumn<Carga, String> colDesignacao;
    @FXML private TableColumn<Carga, Integer> colTipo;
    @FXML private TableColumn<Carga, Float> colVolume;
    @FXML private TableColumn<Carga, Float> colPeso;
    @FXML private TableColumn<Carga, Integer> colPortoCarga;
    @FXML private TableColumn<Carga, Integer> colPortoDescarga;
    @FXML private TextField txtPesquisa;

    private List<Carga> todasCargas;

    @FXML
    private void initialize() {
        colDesignacao.setCellValueFactory(new PropertyValueFactory<>("designacao"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colVolume.setCellValueFactory(new PropertyValueFactory<>("volume"));
        colPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));
        colPortoCarga.setCellValueFactory(new PropertyValueFactory<>("portoCarga"));
        colPortoDescarga.setCellValueFactory(new PropertyValueFactory<>("portoDescarga"));

        carregarCargas();
    }

    private void carregarCargas() {
        todasCargas = new CargasDAO().listarCargas();
        tabelaCargas.setItems(FXCollections.observableArrayList(todasCargas));
    }

    @FXML
    private void handleSearchCarga() {
        String pesquisa = txtPesquisa.getText().toLowerCase();

        if (pesquisa.isEmpty()) {
            tabelaCargas.setItems(FXCollections.observableArrayList(todasCargas));
            return;
        }

        List<Carga> filtradas = todasCargas.stream()
            .filter(c -> c.getDesignacao().toLowerCase().contains(pesquisa))
            .toList();

        tabelaCargas.setItems(FXCollections.observableArrayList(filtradas));
    }

    @FXML
    private void handleAdicionarCarga() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdicionarCargasView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Adicionar Carga");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            carregarCargas(); // atualiza a tabela depois de adicionar

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}