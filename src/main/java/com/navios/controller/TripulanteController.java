package com.navios.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import com.navios.DB.TripulanteDAO;
import com.navios.model.Tripulante;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class TripulanteController {
    
    private ObservableList<Tripulante> listaTripulantes;

    @FXML private TableView<Tripulante> tabelaTripulantes;
    @FXML private TableColumn<Tripulante, String> colNome;
    @FXML private TableColumn<Tripulante, String> colSobrenome;
    @FXML private TableColumn<Tripulante, String> colEstadoDisponibilidade;
    @FXML private TableColumn<Tripulante, String> colNacionalidade;
    @FXML private TableColumn<Tripulante, String> colDataNascimento;
    @FXML private TableColumn<Tripulante, Void>   colAcoes;   // ← NOVO (adicionar no FXML)
    @FXML private TextField txtPesquisa;

    private List<Tripulante> todosTripulantes;
    private final TripulanteDAO dao = new TripulanteDAO();

    @FXML
    private void initialize() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colSobrenome.setCellValueFactory(new PropertyValueFactory<>("sobrenome"));
        colEstadoDisponibilidade.setCellValueFactory(new PropertyValueFactory<>("estadoDisponibilidade"));
        colNacionalidade.setCellValueFactory(new PropertyValueFactory<>("nacionalidade"));
        colDataNascimento.setCellValueFactory(new PropertyValueFactory<>("dataNascimento"));

        configurarColunaAcoes();   // ← NOVO
        carregarTripulantes();

        // ← usa "dao" não "navioDAO"
        listaTripulantes = FXCollections.observableArrayList(dao.listarTripulantes());

        FilteredList<Tripulante> filtrada = new FilteredList<>(listaTripulantes, p -> true);

        txtPesquisa.textProperty().addListener((obs, antigo, novo) -> {
            filtrada.setPredicate(tripulante -> {
                if (novo == null || novo.isEmpty()) return true;
                return tripulante.getNome().toLowerCase().contains(novo.toLowerCase());
            });
        });

        tabelaTripulantes.setItems(filtrada); // ← FALTA ISTO
    }

    // ─── Botão Editar por linha ───────────────────────────────────────────
    private void configurarColunaAcoes() {
        colAcoes.setCellFactory(col -> new TableCell<>() {
            private final Button btnEditar = new Button("✏ Editar");
            {
                btnEditar.setStyle(
                    "-fx-background-color: #1a73e8;" +
                    "-fx-text-fill: white;" +
                    "-fx-cursor: hand;" +
                    "-fx-background-radius: 4;"
                );
                btnEditar.setOnAction(e -> {
                    Tripulante t = getTableView().getItems().get(getIndex());
                    abrirJanelaEditar(t);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnEditar);
            }
        });
    }

    // ─── Janela de edição ─────────────────────────────────────────────────
    private void abrirJanelaEditar(Tripulante t) {
        Dialog<Tripulante> dialog = new Dialog<>();
        dialog.setTitle("Editar Tripulante");

        TextField txtNome       = new TextField(t.getNome());
        TextField txtSobrenome  = new TextField(t.getSobrenome());
        TextField txtNacional   = new TextField(t.getNacionalidade());
        DatePicker dpNasc       = new DatePicker(t.getDataNascimento());
        ComboBox<String> cbEstado = new ComboBox<>();
        cbEstado.getItems().addAll("disponivel", "indisponivel");
        cbEstado.setValue(t.getEstadoDisponibilidade());

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.addRow(0, new Label("Nome:"),          txtNome);
        grid.addRow(1, new Label("Sobrenome:"),     txtSobrenome);
        grid.addRow(2, new Label("Nacionalidade:"), txtNacional);
        grid.addRow(3, new Label("Nascimento:"),    dpNasc);
        grid.addRow(4, new Label("Estado:"),        cbEstado);
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                if (dpNasc.getValue() == null) {
                new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR,
                    "Por favor seleciona a data de nascimento.").showAndWait();
                return null;
            }
            if (dpNasc.getValue().isAfter(LocalDate.now().minusYears(16))) {
                new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR,
                    "O tripulante deve ter pelo menos 16 anos.").showAndWait();
                return null;
            }
                Tripulante atualizado = new Tripulante(
                    t.getIdTripulante(),
                    txtNome.getText(),
                    txtSobrenome.getText(),
                    cbEstado.getValue(),
                    txtNacional.getText(),
                    dpNasc.getValue()
                );
                dao.atualizarTripulante(atualizado);
                carregarTripulantes();
            }
            return null;
        });

        dialog.showAndWait();
    }

    // ─── Métodos existentes ───────────────────────────────────────────────
    private void carregarTripulantes() {
        todosTripulantes = dao.listarTripulantes();
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