package com.navios.controller;

import java.io.IOException;

import com.navios.DB.NavioDAO;
import com.navios.DB.TipoNavioDAO;
import com.navios.model.Navio;
import com.navios.model.TipoNavio;

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
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class NavioController {
    
    private ObservableList<Navio> listaNavios;

    @FXML private TableView<Navio> tabelaNavios;
    @FXML private TableColumn<Navio, Integer> colId;
    @FXML private TableColumn<Navio, String>  colNome;
    @FXML private TableColumn<Navio, String>  colIMO;
    @FXML private TableColumn<Navio, String>  colBandeira;
    @FXML private TableColumn<Navio, Integer> colAno;
    @FXML private TableColumn<Navio, String>  colEstado;
    @FXML private TableColumn<Navio, Void>    colAcoes;  // ← NOVO

    private final NavioDAO dao = new NavioDAO(); // ← partilhado entre métodos

   @FXML private TextField txtSearchNavio; // ← FALTA DECLARAR

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idNavio"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colIMO.setCellValueFactory(new PropertyValueFactory<>("identificadorIMO"));
        colBandeira.setCellValueFactory(new PropertyValueFactory<>("bandeira"));
        colAno.setCellValueFactory(new PropertyValueFactory<>("anoFabrico"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estadoOperacional"));

        configurarColunaAcoes();

        // ← usa "dao" não "navioDAO"
        listaNavios = FXCollections.observableArrayList(dao.listarNavios());

        FilteredList<Navio> filtrada = new FilteredList<>(listaNavios, p -> true);

        txtSearchNavio.textProperty().addListener((obs, antigo, novo) -> {
            filtrada.setPredicate(navio -> {
                if (novo == null || novo.isEmpty()) return true;
                return navio.getNome().toLowerCase().contains(novo.toLowerCase());
            });
        });

        tabelaNavios.setItems(filtrada); // ← FALTA ISTO
    }

    // ─── Botão "Editar" por linha ──────────────────────────────────────────
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

                btnEditar.setOnAction(event -> {
                    Navio navio = getTableView().getItems().get(getIndex());
                    abrirJanelaEditar(navio);
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
    private void abrirJanelaEditar(Navio navio) {
        Dialog<Navio> dialog = new Dialog<>();
        dialog.setTitle("Editar Navio");

        // Campos com os valores atuais do navio
        TextField txtNome      = new TextField(navio.getNome());
        TextField txtIMO       = new TextField(navio.getIdentificadorIMO());
        TextField txtBandeira  = new TextField(navio.getBandeira());
        TextField txtAno       = new TextField(String.valueOf(navio.getAnoFabrico()));
        ComboBox<TipoNavio> cbTipo = new ComboBox<>(FXCollections.observableArrayList(
            new TipoNavioDAO().listarTipos()
        ));
        cbTipo.getItems().stream()
            .filter(tipo -> tipo.getIdTipoNavio() == navio.getTipo())
            .findFirst()
            .ifPresent(cbTipo::setValue);
        Spinner<Integer> spCompartimentos = new Spinner<>();
        spCompartimentos.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999, navio.getNCompartimentos()));
        spCompartimentos.setEditable(true);
        Spinner<Integer> spMaxCargas = new Spinner<>();
        spMaxCargas.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9999, navio.getNMaximoCargas()));
        spMaxCargas.setEditable(true);
        ComboBox<String> cbEstado = new ComboBox<>();
        cbEstado.getItems().addAll("ativo", "em manutenção", "inativo");
        cbEstado.setValue(navio.getEstadoOperacional());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.addRow(0, new Label("Nome:"),     txtNome);
        grid.addRow(1, new Label("IMO:"),      txtIMO);
        grid.addRow(2, new Label("Bandeira:"), txtBandeira);
        grid.addRow(3, new Label("Ano:"),      txtAno);
        grid.addRow(4, new Label("Tipo:"),     cbTipo);
        grid.addRow(5, new Label("Compartimentos:"), spCompartimentos);
        grid.addRow(6, new Label("Max. cargas:"), spMaxCargas);
        grid.addRow(7, new Label("Estado:"),   cbEstado);
        dialog.getDialogPane().setContent(grid);

        dialog.getDialogPane().getButtonTypes()
              .addAll(ButtonType.OK, ButtonType.CANCEL);

       dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                navio.setNome(txtNome.getText());
                navio.setIdentificadorIMO(txtIMO.getText());
                navio.setTipo(cbTipo.getValue().getIdTipoNavio());
                navio.setNCompartimentos(spCompartimentos.getValue());
                navio.setNMaximoCargas(spMaxCargas.getValue());
                navio.setBandeira(txtBandeira.getText());
                navio.setAnoFabrico(Integer.parseInt(txtAno.getText()));
                navio.setEstadoOperacional(cbEstado.getValue());

                dao.atualizarNavio(navio);
                carregarNavios(); // ← em vez de tabelaNavios.refresh()
            }
            return null;
        });

        dialog.showAndWait();
    }

    // ─── Métodos existentes ───────────────────────────────────────────────
    private void carregarNavios() {
        listaNavios.setAll(dao.listarNavios());
    }

    @FXML
    private void handleSearchNavio() {
        System.out.println("Search Navio clicado");
    }

    @FXML
    private void handleAdicionarNavio() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/AdicionarNavioView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Adicionar Navio");
            stage.setScene(new Scene(root));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.showAndWait();
            carregarNavios();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
