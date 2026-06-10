package com.navios.controller;

import java.io.IOException;
import java.util.List;

import com.navios.DB.NavioDAO;
import com.navios.model.Navio;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;


public class NavioController {

    @FXML private TableView<Navio> tabelaNavios;
    @FXML private TableColumn<Navio, Integer> colId;
    @FXML private TableColumn<Navio, String> colNome;
    @FXML private TableColumn<Navio, String> colIMO;
    @FXML private TableColumn<Navio, String> colBandeira;
    @FXML private TableColumn<Navio, Integer> colAno;
    @FXML private TableColumn<Navio, String> colEstado;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idNavio"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colIMO.setCellValueFactory(new PropertyValueFactory<>("identificadorIMO"));
        colBandeira.setCellValueFactory(new PropertyValueFactory<>("bandeira"));
        colAno.setCellValueFactory(new PropertyValueFactory<>("anoFabrico"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estadoOperacional"));

        carregarNavios();
    }

    private void carregarNavios() {
        NavioDAO dao = new NavioDAO();
        List<Navio> navios = dao.listarNavios();
        tabelaNavios.getItems().setAll(navios);
    }

    @FXML
    private void handleSearchNavio() {
        System.out.println("Search Navio clicado");
    }

    @FXML
   private void handleAdicionarNavio() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdicionarNavioView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Adicionar Navio");
            stage.setScene(new Scene(root));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.showAndWait(); // ← espera fechar antes de continuar
            carregarNavios();    // ← atualiza a tabela automaticamente
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}