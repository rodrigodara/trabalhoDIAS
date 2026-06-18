package com.navios.controller;

import java.io.IOException;
import java.util.List;

import com.navios.DB.CargasDAO;
import com.navios.DB.PortoDAO;
import com.navios.DB.TipoCargaDAO;
import com.navios.model.Carga;
import com.navios.model.Porto;
import com.navios.model.TipoCarga;

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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class CargasController {

    private ObservableList<Carga> listaCargas;

    @FXML private TableView<Carga> tabelaCargas;
    @FXML private TableColumn<Carga, String>  colDesignacao;
    @FXML private TableColumn<Carga, Integer> colTipo;
    @FXML private TableColumn<Carga, Float>   colVolume;
    @FXML private TableColumn<Carga, Float>   colPeso;
    @FXML private TableColumn<Carga, Integer> colPortoCarga;
    @FXML private TableColumn<Carga, Integer> colPortoDescarga;
    @FXML private TableColumn<Carga, Void>    colAcoes;   // ← NOVO (adicionar no FXML)
    @FXML private TextField txtPesquisa;

    private List<Carga> todasCargas;
    private final CargasDAO   dao        = new CargasDAO();
    private final PortoDAO    portoDAO   = new PortoDAO();
    private final TipoCargaDAO tipoDAO   = new TipoCargaDAO();

    @FXML
    private void initialize() {
        colDesignacao.setCellValueFactory(new PropertyValueFactory<>("designacao"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colVolume.setCellValueFactory(new PropertyValueFactory<>("volume"));
        colPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));
        colPortoCarga.setCellValueFactory(new PropertyValueFactory<>("portoCarga"));
        colPortoDescarga.setCellValueFactory(new PropertyValueFactory<>("portoDescarga"));

        configurarColunaAcoes();

        listaCargas = FXCollections.observableArrayList(dao.listarCargas());

        FilteredList<Carga> filtrada = new FilteredList<>(listaCargas, p -> true);

        // Portos para as colunas
        List<Porto> portos = portoDAO.listarPortos();

        colPortoCarga.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Integer id, boolean empty) {
                super.updateItem(id, empty);
                if (empty || id == null) { setText(null); return; }
                portos.stream()
                    .filter(p -> p.getIdPorto() == id)
                    .findFirst()
                    .ifPresentOrElse(p -> setText(p.getNome()), () -> setText("Porto " + id));
            }
        });

        List<TipoCarga> tipos = tipoDAO.listarTipos();

        colTipo.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Integer id, boolean empty) {
                super.updateItem(id, empty);
                if (empty || id == null) { setText(null); return; }
                tipos.stream()
                    .filter(t -> t.getIdTipoCarga() == id)
                    .findFirst()
                    .ifPresentOrElse(t -> setText(t.getNome()), () -> setText("Tipo " + id));
            }
        });

        colPortoDescarga.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Integer id, boolean empty) {
                super.updateItem(id, empty);
                if (empty || id == null) { setText(null); return; }
                portos.stream()
                    .filter(p -> p.getIdPorto() == id)
                    .findFirst()
                    .ifPresentOrElse(p -> setText(p.getNome()), () -> setText("Porto " + id));
            }
        });

        txtPesquisa.textProperty().addListener((obs, antigo, novo) -> {
            filtrada.setPredicate(carga -> {
                if (novo == null || novo.isEmpty()) return true;
                return carga.getDesignacao().toLowerCase().contains(novo.toLowerCase());
            });
        });

        tabelaCargas.setItems(filtrada);
    }

    // ─── Janela de edição ─────────────────────────────────────────────────
    private void abrirJanelaEditar(Carga c) {
        Dialog<Carga> dialog = new Dialog<>();
        dialog.setTitle("Editar Carga");

        List<Porto>     portos = portoDAO.listarPortos();
        List<TipoCarga> tipos  = tipoDAO.listarTipos();

        TextField txtDesignacao = new TextField(c.getDesignacao());
        TextField txtVolume     = new TextField(String.valueOf(c.getVolume()));
        TextField txtPeso       = new TextField(String.valueOf(c.getPeso()));

        ComboBox<TipoCarga> cbTipo = new ComboBox<>(FXCollections.observableArrayList(tipos));
        tipos.stream().filter(t -> t.getIdTipoCarga() == c.getTipo()).findFirst().ifPresent(cbTipo::setValue);
        colPortoCarga.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Integer id, boolean empty) {
                super.updateItem(id, empty);
                if (empty || id == null) { setText(null); return; }
                portos.stream()
                    .filter(p -> p.getIdPorto() == id)
                    .findFirst()
                    .ifPresentOrElse(p -> setText(p.getNome()), () -> setText("Porto " + id));
            }
});
        ComboBox<Porto> cbPortoCarga    = new ComboBox<>(FXCollections.observableArrayList(portos));
        ComboBox<Porto> cbPortoDescarga = new ComboBox<>(FXCollections.observableArrayList(portos));
        portos.stream().filter(p -> p.getIdPorto() == c.getPortoCarga())   .findFirst().ifPresent(cbPortoCarga::setValue);
        portos.stream().filter(p -> p.getIdPorto() == c.getPortoDescarga()).findFirst().ifPresent(cbPortoDescarga::setValue);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.addRow(0, new Label("Designação:"),     txtDesignacao);
        grid.addRow(1, new Label("Tipo:"),           cbTipo);
        grid.addRow(2, new Label("Volume (m³):"),    txtVolume);
        grid.addRow(3, new Label("Peso (kg):"),      txtPeso);
        grid.addRow(4, new Label("Porto Carga:"),    cbPortoCarga);
        grid.addRow(5, new Label("Porto Descarga:"), cbPortoDescarga);
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                Carga atualizada = new Carga(
                    c.getIdCarga(),
                    txtDesignacao.getText(),
                    cbTipo.getValue().getIdTipoCarga(),
                    Float.parseFloat(txtVolume.getText()),
                    Float.parseFloat(txtPeso.getText()),
                    cbPortoCarga.getValue().getIdPorto(),
                    cbPortoDescarga.getValue().getIdPorto()
                );
                dao.atualizarCarga(atualizada);
                carregarCargas();
            }
            return null;
        });

        dialog.showAndWait();
    }

    // ─── Métodos existentes ───────────────────────────────────────────────
    private void carregarCargas() {
        todasCargas = dao.listarCargas();
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
            carregarCargas();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
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
                    Carga c = getTableView().getItems().get(getIndex());
                    abrirJanelaEditar(c);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnEditar);
            }
        });
    }
}