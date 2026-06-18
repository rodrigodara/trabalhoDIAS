package com.navios.controller;

import java.io.IOException;
import java.util.List;

import com.navios.DB.PortoDAO;
import com.navios.DB.ViagemDAO;
import com.navios.model.Porto;
import com.navios.model.Viagem;

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

public class ViagemController {

        private ObservableList<Viagem> listaViagens;

    @FXML private TableView<Viagem> tabelaViagens;
    @FXML private TableColumn<Viagem, String>  colNomeViagem;
    @FXML private TableColumn<Viagem, Integer> colOrigem;
    @FXML private TableColumn<Viagem, Integer> colDestino;
    @FXML private TableColumn<Viagem, String>  colObservacoes;
    @FXML private TableColumn<Viagem, Void>    colAcoes;   // ← NOVO (adicionar no FXML)
    @FXML private TextField txtPesquisa;
    @FXML private Button btnAdicionarViagem;
    @FXML private Button btnSearchViagem;

    private List<Viagem> todasViagens;
    private final ViagemDAO dao     = new ViagemDAO();
    private final PortoDAO portoDAO = new PortoDAO();

    @FXML
    private void initialize() {
        colNomeViagem.setCellValueFactory(new PropertyValueFactory<>("estadoViagem"));
        colOrigem.setCellValueFactory(new PropertyValueFactory<>("portoOrigem"));
        colDestino.setCellValueFactory(new PropertyValueFactory<>("portoDestino"));
        colObservacoes.setCellValueFactory(new PropertyValueFactory<>("dataPartida"));

        configurarColunaAcoes();

        listaViagens = FXCollections.observableArrayList(dao.listarViagens());

        FilteredList<Viagem> filtrada = new FilteredList<>(listaViagens, p -> true);

        // Carrega portos uma vez para o filtro
        List<Porto> portos = portoDAO.listarPortos();

// Coluna Origem — mostra nome em vez do ID
colOrigem.setCellValueFactory(new PropertyValueFactory<>("portoOrigem"));
colOrigem.setCellFactory(col -> new TableCell<>() {
    @Override
    protected void updateItem(Integer id, boolean empty) {
        super.updateItem(id, empty);
        if (empty || id == null) {
            setText(null);
        } else {
            portos.stream()
                .filter(p -> p.getIdPorto() == id)
                .findFirst()
                .ifPresentOrElse(
                    p -> setText(p.getNome()),
                    () -> setText("Porto " + id)
                );
        }
    }
});

// Coluna Destino — igual
colDestino.setCellValueFactory(new PropertyValueFactory<>("portoDestino"));
colDestino.setCellFactory(col -> new TableCell<>() {
    @Override
    protected void updateItem(Integer id, boolean empty) {
        super.updateItem(id, empty);
        if (empty || id == null) {
            setText(null);
        } else {
            portos.stream()
                .filter(p -> p.getIdPorto() == id)
                .findFirst()
                .ifPresentOrElse(
                    p -> setText(p.getNome()),
                    () -> setText("Porto " + id)
                );
        }
    }
});
        txtPesquisa.textProperty().addListener((obs, antigo, novo) -> {
            filtrada.setPredicate(viagem -> {
                if (novo == null || novo.isEmpty()) return true;
                String lower = novo.toLowerCase();
                // Procura o nome do porto de origem pelo ID
                return portos.stream()
                    .filter(p -> p.getIdPorto() == viagem.getPortoOrigem())
                    .findFirst()
                    .map(p -> p.getNome().toLowerCase().contains(lower))
                    .orElse(false);
            });

        });

        tabelaViagens.setItems(filtrada);
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
                    Viagem v = getTableView().getItems().get(getIndex());
                    abrirJanelaEditar(v);
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
    private void abrirJanelaEditar(Viagem v) {
        Dialog<Viagem> dialog = new Dialog<>();
        dialog.setTitle("Editar Viagem");

        // Carrega portos para os ComboBoxes
        List<Porto> portos = portoDAO.listarPortos();

        DatePicker dpPartida  = new DatePicker(v.getDataPartida());
        DatePicker dpChegada  = new DatePicker(v.getDataChegadaPrevista());

        ComboBox<Porto> cbOrigem  = new ComboBox<>(FXCollections.observableArrayList(portos));
        ComboBox<Porto> cbDestino = new ComboBox<>(FXCollections.observableArrayList(portos));
        // Seleciona o porto atual pelo ID
        portos.stream().filter(p -> p.getIdPorto() == v.getPortoOrigem()) .findFirst().ifPresent(cbOrigem::setValue);
        portos.stream().filter(p -> p.getIdPorto() == v.getPortoDestino()).findFirst().ifPresent(cbDestino::setValue);

        ComboBox<String> cbEstado = new ComboBox<>();
        cbEstado.getItems().addAll("planeada", "em curso", "concluida", "cancelada");
        cbEstado.setValue(v.getEstadoViagem());

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.addRow(0, new Label("Data Partida:"),         dpPartida);
        grid.addRow(1, new Label("Data Chegada Prevista:"), dpChegada);
        grid.addRow(2, new Label("Porto Origem:"),         cbOrigem);
        grid.addRow(3, new Label("Porto Destino:"),        cbDestino);
        grid.addRow(4, new Label("Estado:"),               cbEstado);
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                Viagem atualizada = new Viagem(
                    v.getIdViagem(),
                    v.getIdNavio(),
                    dpPartida.getValue(),
                    dpChegada.getValue(),
                    cbOrigem.getValue().getIdPorto(),
                    cbDestino.getValue().getIdPorto(),
                    cbEstado.getValue()
                );
                dao.atualizarViagem(atualizada);
                carregarViagens();
            }
            return null;
        });

        dialog.showAndWait();
    }

    // ─── Métodos existentes ───────────────────────────────────────────────
    private void carregarViagens() {
        listaViagens.setAll(dao.listarViagens());
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
            stage.showAndWait();
            carregarViagens();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}