package com.navios.controller;

import java.io.IOException;
import java.util.List;

import com.navios.DB.ViagemDAO;
import com.navios.model.Viagem;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ViagemController {

    @FXML private TableView<Viagem> tabelaViagens;
    @FXML private TableColumn<Viagem, String> colNomeViagem;
    @FXML private TableColumn<Viagem, Integer> colOrigem;
    @FXML private TableColumn<Viagem, Integer> colDestino;
    @FXML private TableColumn<Viagem, String> colObservacoes;
    @FXML private TextField txtPesquisa;
    @FXML private Button btnAdicionarViagem;
    @FXML private Button btnSearchViagem;

    private List<Viagem> todasViagens;

    @FXML
    private void initialize() {
        // Liga cada coluna ao atributo correspondente do modelo Viagem
        colNomeViagem.setCellValueFactory(new PropertyValueFactory<>("estadoViagem"));
        colOrigem.setCellValueFactory(new PropertyValueFactory<>("portoOrigem"));
        colDestino.setCellValueFactory(new PropertyValueFactory<>("portoDestino"));
        colObservacoes.setCellValueFactory(new PropertyValueFactory<>("dataPartida"));

        carregarViagens();
    }

    private void carregarViagens() {
        todasViagens = new ViagemDAO().listarViagens();
        tabelaViagens.setItems(FXCollections.observableArrayList(todasViagens));
    }

    @FXML
    private void handleSearchViagem() {
        String pesquisa = txtPesquisa.getText().toLowerCase();

        if (pesquisa.isEmpty()) {
            tabelaViagens.setItems(FXCollections.observableArrayList(todasViagens));
            return;
        }

        List<Viagem> filtradas = todasViagens.stream()
            .filter(v -> v.getEstadoViagem().toLowerCase().contains(pesquisa))
            .toList();

        tabelaViagens.setItems(FXCollections.observableArrayList(filtradas));
    }

    @FXML
    private void handleAdicionarViagem() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdicionarViagemView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Adicionar Viagem");
            stage.setScene(new Scene(root));
            stage.showAndWait(); // espera fechar o modal antes de recarregar

            carregarViagens(); // atualiza a tabela depois de adicionar

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}