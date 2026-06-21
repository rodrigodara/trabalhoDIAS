package com.navios.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.navios.DB.CargaViagemDAO;
import com.navios.DB.CargasDAO;
import com.navios.DB.FuncaoDAO;
import com.navios.DB.NavioDAO;
import com.navios.DB.PortoDAO;
import com.navios.DB.TipoCargaNavioDAO;
import com.navios.DB.TripulacaoDAO;
import com.navios.DB.TripulanteDAO;
import com.navios.DB.ViagemDAO;
import com.navios.model.Carga;
import com.navios.model.Funcao;
import com.navios.model.Navio;
import com.navios.model.Porto;
import com.navios.model.Tripulante;
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
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ViagemController {

    private ObservableList<Viagem> listaViagens;

    @FXML private TableView<Viagem> tabelaViagens;
    @FXML private TableColumn<Viagem, Integer>  colNavio;
    @FXML private TableColumn<Viagem, LocalDate> colDataPartida;
    @FXML private TableColumn<Viagem, LocalDate> colDataChegada;
    @FXML private TableColumn<Viagem, Integer>  colOrigem;
    @FXML private TableColumn<Viagem, Integer>  colDestino;
    @FXML private TableColumn<Viagem, String>   colEstado;
    @FXML private TableColumn<Viagem, Void>     colAcoes;
    @FXML private TextField txtPesquisa;
    @FXML private Button btnAdicionarViagem;
    @FXML private Button btnSearchViagem;

    private List<Viagem> todasViagens;
    private final ViagemDAO dao     = new ViagemDAO();
    private final PortoDAO portoDAO = new PortoDAO();
    private final NavioDAO navioDAO = new NavioDAO();

    @FXML
    private void initialize() {

        // Carregados uma vez, para resolver IDs -> nomes nas colunas
        List<Navio> navios = navioDAO.listarNavios();
        List<Porto> portos = portoDAO.listarPortos();

        // Coluna Navio — mostra o nome em vez do ID
        colNavio.setCellValueFactory(new PropertyValueFactory<>("idNavio"));
        colNavio.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Integer id, boolean empty) {
                super.updateItem(id, empty);
                if (empty || id == null) {
                    setText(null);
                } else {
                    navios.stream()
                        .filter(n -> n.getIdNavio() == id)
                        .findFirst()
                        .ifPresentOrElse(
                            n -> setText(n.getNome()),
                            () -> setText("Navio " + id)
                        );
                }
            }
        });

        colDataPartida.setCellValueFactory(new PropertyValueFactory<>("dataPartida"));
        colDataChegada.setCellValueFactory(new PropertyValueFactory<>("dataChegadaPrevista"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estadoViagem"));

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

        configurarColunaAcoes();

        listaViagens = FXCollections.observableArrayList(dao.listarViagens());
        todasViagens = listaViagens; // ← antes nunca era inicializado (NPE em handleSearchViagem)

        FilteredList<Viagem> filtrada = new FilteredList<>(listaViagens, p -> true);

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

        List<Porto> portos = portoDAO.listarPortos();
        List<Navio> navios = navioDAO.listarNavios();
        Navio navioDaViagem = navios.stream()
            .filter(n -> n.getIdNavio() == v.getIdNavio())
            .findFirst()
            .orElse(null);

        DatePicker dpPartida  = new DatePicker(v.getDataPartida());
        DatePicker dpChegada  = new DatePicker(v.getDataChegadaPrevista());

        ComboBox<Porto> cbOrigem  = new ComboBox<>(FXCollections.observableArrayList(portos));
        ComboBox<Porto> cbDestino = new ComboBox<>(FXCollections.observableArrayList(portos));
        portos.stream().filter(p -> p.getIdPorto() == v.getPortoOrigem()) .findFirst().ifPresent(cbOrigem::setValue);
        portos.stream().filter(p -> p.getIdPorto() == v.getPortoDestino()).findFirst().ifPresent(cbDestino::setValue);

        ComboBox<String> cbEstado = new ComboBox<>();
        cbEstado.getItems().addAll("planeada", "em curso", "concluída", "cancelada");
        cbEstado.setValue(v.getEstadoViagem());

        ObservableList<Carga> cargasDisponiveis = FXCollections.observableArrayList(new CargasDAO().listarCargas());
        ObservableList<Carga> cargasViagem = FXCollections.observableArrayList();
        Set<Integer> idsCargasViagem = new CargaViagemDAO().listarIdsCargasPorViagem(v.getIdViagem());
        cargasDisponiveis.removeIf(carga -> {
            if (idsCargasViagem.contains(carga.getIdCarga())) {
                cargasViagem.add(carga);
                return true;
            }
            return false;
        });

        ListView<Carga> listCargasDisponiveis = new ListView<>(cargasDisponiveis);
        listCargasDisponiveis.setPrefHeight(120);
        ListView<Carga> listCargasViagem = new ListView<>(cargasViagem);
        listCargasViagem.setPrefHeight(120);
        Button btnAddCarga = new Button("Adicionar");
        Button btnRemCarga = new Button("Remover");
        btnAddCarga.setOnAction(event -> {
            Carga carga = listCargasDisponiveis.getSelectionModel().getSelectedItem();
            if (carga == null) return;
            if (navioDaViagem != null && cargasViagem.size() >= navioDaViagem.getNMaximoCargas()) return;
            if (navioDaViagem != null && !new TipoCargaNavioDAO().isCompativel(navioDaViagem.getTipo(), carga.getTipo())) {
                return; // ou mostrar um Alert
            }

            cargasDisponiveis.remove(carga);
            cargasViagem.add(carga);
        });
        btnRemCarga.setOnAction(event -> {
            Carga carga = listCargasViagem.getSelectionModel().getSelectedItem();
            if (carga == null) return;
            cargasViagem.remove(carga);
            cargasDisponiveis.add(carga);
        });

        ObservableList<Tripulante> tripulantesDisponiveis = FXCollections.observableArrayList(new TripulanteDAO().listarTripulantes());
        ObservableList<Tripulante> tripulantesViagem = FXCollections.observableArrayList();
        Map<Integer, Integer> funcoesPorTripulante = new HashMap<>(
            new TripulacaoDAO().listarFuncoesPorTripulante(v.getIdViagem())
        );
        tripulantesDisponiveis.removeIf(tripulante -> {
            if (funcoesPorTripulante.containsKey(tripulante.getIdTripulante())) {
                tripulantesViagem.add(tripulante);
                return true;
            }
            return false;
        });

        ListView<Tripulante> listTripulantesDisponiveis = new ListView<>(tripulantesDisponiveis);
        listTripulantesDisponiveis.setPrefHeight(120);
        ListView<Tripulante> listTripulantesViagem = new ListView<>(tripulantesViagem);
        listTripulantesViagem.setPrefHeight(120);
        ComboBox<Funcao> cbFuncao = new ComboBox<>(FXCollections.observableArrayList(new FuncaoDAO().listarFuncoes()));
        cbFuncao.setPromptText("Funcao");
        Button btnAddTripulante = new Button("Adicionar");
        Button btnRemTripulante = new Button("Remover");
        btnAddTripulante.setOnAction(event -> {
            Tripulante tripulante = listTripulantesDisponiveis.getSelectionModel().getSelectedItem();
            Funcao funcao = cbFuncao.getValue();
            if (tripulante == null || funcao == null) return;
            tripulantesDisponiveis.remove(tripulante);
            tripulantesViagem.add(tripulante);
            funcoesPorTripulante.put(tripulante.getIdTripulante(), funcao.getIdFuncao());
        });
        btnRemTripulante.setOnAction(event -> {
            Tripulante tripulante = listTripulantesViagem.getSelectionModel().getSelectedItem();
            if (tripulante == null) return;
            tripulantesViagem.remove(tripulante);
            tripulantesDisponiveis.add(tripulante);
            funcoesPorTripulante.remove(tripulante.getIdTripulante());
        });

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.addRow(0, new Label("Data Partida:"),         dpPartida);
        grid.addRow(1, new Label("Data Chegada Prevista:"), dpChegada);
        grid.addRow(2, new Label("Porto Origem:"),         cbOrigem);
        grid.addRow(3, new Label("Porto Destino:"),        cbDestino);
        grid.addRow(4, new Label("Estado:"),               cbEstado);

        VBox cargasBox = new VBox(6,
            new Label("Cargas desta viagem"),
            new HBox(10,
                new VBox(4, new Label("Disponiveis"), listCargasDisponiveis),
                new VBox(6, btnAddCarga, btnRemCarga),
                new VBox(4, new Label("Na viagem"), listCargasViagem)
            )
        );
        VBox tripulacaoBox = new VBox(6,
            new Label("Tripulacao desta viagem"),
            new HBox(10,
                new VBox(4, new Label("Disponiveis"), listTripulantesDisponiveis),
                new VBox(6, cbFuncao, btnAddTripulante, btnRemTripulante),
                new VBox(4, new Label("Na viagem"), listTripulantesViagem)
            )
        );

        VBox content = new VBox(14, grid, cargasBox, tripulacaoBox);
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {

                String novoEstado = cbEstado.getValue();
                boolean vaiFicarAtiva = "planeada".equals(novoEstado) || "em curso".equals(novoEstado);

                // Regra 2 — validar ANTES de gravar
                if (vaiFicarAtiva && new ViagemDAO().existeViagemAtivaParaNavio(v.getIdNavio(), v.getIdViagem())) {
                    new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR,
                        "Este navio já tem outra viagem ativa.").showAndWait();
                    return null;
                }

                // Regra 1 — também em falta aqui
                if (vaiFicarAtiva && navioDaViagem != null
                        && !"ativo".equalsIgnoreCase(navioDaViagem.getEstadoOperacional())) {
                    new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR,
                        "O navio \"" + navioDaViagem.getNome() + "\" está \"" + navioDaViagem.getEstadoOperacional()
                        + "\" e não pode ficar com uma viagem ativa.").showAndWait();
                    return null;
                }

                Viagem atualizada = new Viagem(
                    v.getIdViagem(), v.getIdNavio(),
                    dpPartida.getValue(), dpChegada.getValue(),
                    cbOrigem.getValue().getIdPorto(), cbDestino.getValue().getIdPorto(),
                    cbEstado.getValue()
                );
                dao.atualizarViagem(atualizada);

                Set<Integer> idsCargasAtualizadas = new HashSet<>();
                for (Carga carga : cargasViagem) idsCargasAtualizadas.add(carga.getIdCarga());
                new CargaViagemDAO().substituirCargasDaViagem(v.getIdViagem(), idsCargasAtualizadas);
                new TripulacaoDAO().substituirTripulacaoDaViagem(v.getIdViagem(), funcoesPorTripulante);
                carregarViagens();
            }
            return null;
        });

        dialog.showAndWait();
    }

    // ─── Métodos existentes ───────────────────────────────────────────────
    private void carregarViagens() {
        listaViagens.setAll(dao.listarViagens());
        todasViagens = listaViagens;
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
