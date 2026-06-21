package com.navios.controller;

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
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class AdicionarViagemController {

    @FXML private ComboBox<String> navioCombo;
    @FXML private ComboBox<Porto> portoOrigemCombo;
    @FXML private ComboBox<Porto> portoDestinoCombo;
    @FXML private DatePicker dataPartidaPicker;
    @FXML private DatePicker dataChegadaPrevistaPicker;
    @FXML private ComboBox<String> estadoViagemCombo;
    @FXML private ListView<Carga> cargasDisponiveisList;
    @FXML private ListView<Carga> cargasViagemList;
    @FXML private ListView<Tripulante> tripulantesDisponiveisList;
    @FXML private ListView<Tripulante> tripulantesViagemList;
    @FXML private ComboBox<Funcao> funcaoTripulanteCombo;
    @FXML private Label erroLabel;

    private List<Navio> navios;
    private final ObservableList<Carga> cargasDisponiveis = FXCollections.observableArrayList();
    private final ObservableList<Carga> cargasViagem = FXCollections.observableArrayList();
    private final ObservableList<Tripulante> tripulantesDisponiveis = FXCollections.observableArrayList();
    private final ObservableList<Tripulante> tripulantesViagem = FXCollections.observableArrayList();
    private final Map<Integer, Integer> funcoesPorTripulante = new HashMap<>();

    @FXML
    private void initialize() {
        navios = new NavioDAO().listarNavios();
        navioCombo.setItems(FXCollections.observableArrayList(
            navios.stream().map(Navio::getNome).toList()
        ));

        List<Porto> portos = new PortoDAO().listarPortos();
        portoOrigemCombo.setItems(FXCollections.observableArrayList(portos));
        portoDestinoCombo.setItems(FXCollections.observableArrayList(portos));

        cargasDisponiveis.setAll(new CargasDAO().listarCargas());
        cargasDisponiveisList.setItems(cargasDisponiveis);
        cargasViagemList.setItems(cargasViagem);

        tripulantesDisponiveis.setAll(new TripulanteDAO().listarTripulantes());
        tripulantesDisponiveisList.setItems(tripulantesDisponiveis);
        tripulantesViagemList.setItems(tripulantesViagem);

        funcaoTripulanteCombo.setItems(FXCollections.observableArrayList(
            new FuncaoDAO().listarFuncoes()
        ));
    }

    @FXML
    private void handleAdicionarCarga() {
        Carga carga = cargasDisponiveisList.getSelectionModel().getSelectedItem();
        if (carga == null) {
            mostrarErro("Seleciona uma carga para adicionar.");
            return;
        }

        Navio navio = getNavioSelecionado();
        if (navio != null && cargasViagem.size() >= navio.getNMaximoCargas()) {
            mostrarErro("O navio selecionado nao permite mais cargas nesta viagem.");
            return;
        }

        cargasDisponiveis.remove(carga);
        cargasViagem.add(carga);
        limparErro();
    }

    @FXML
    private void handleRemoverCarga() {
        Carga carga = cargasViagemList.getSelectionModel().getSelectedItem();
        if (carga == null) {
            mostrarErro("Seleciona uma carga para remover.");
            return;
        }
        cargasViagem.remove(carga);
        cargasDisponiveis.add(carga);
        limparErro();
    }

    @FXML
    private void handleAdicionarTripulante() {
        Tripulante tripulante = tripulantesDisponiveisList.getSelectionModel().getSelectedItem();
        Funcao funcao = funcaoTripulanteCombo.getValue();
        if (tripulante == null) {
            mostrarErro("Seleciona um tripulante para adicionar.");
            return;
        }
        if (funcao == null) {
            mostrarErro("Seleciona a funcao do tripulante nesta viagem.");
            return;
        }

        tripulantesDisponiveis.remove(tripulante);
        tripulantesViagem.add(tripulante);
        funcoesPorTripulante.put(tripulante.getIdTripulante(), funcao.getIdFuncao());
        limparErro();
    }

    @FXML
    private void handleRemoverTripulante() {
        Tripulante tripulante = tripulantesViagemList.getSelectionModel().getSelectedItem();
        if (tripulante == null) {
            mostrarErro("Seleciona um tripulante para remover.");
            return;
        }
        tripulantesViagem.remove(tripulante);
        tripulantesDisponiveis.add(tripulante);
        funcoesPorTripulante.remove(tripulante.getIdTripulante());
        limparErro();
    }

    @FXML
    private void handleFecharModal() {
        Stage stage = (Stage) navioCombo.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleGuardarViagem() {
        if (!validarCampos()) {
            return;
        }

        LocalDate partida = dataPartidaPicker.getValue();
        LocalDate chegada = dataChegadaPrevistaPicker.getValue();

        if (chegada.isBefore(partida)) {
            mostrarErro("A data de chegada prevista nao pode ser anterior a data de partida.");
            return;
        }

        Navio navio = getNavioSelecionado();
        if (navio == null) {
            mostrarErro("Por favor seleciona o navio.");
            return;
        }
        if (cargasViagem.size() > navio.getNMaximoCargas()) {
            mostrarErro("O numero de cargas desta viagem excede o maximo permitido pelo navio.");
            return;
        }

        Viagem viagem = new Viagem(
            0,
            navio.getIdNavio(),
            partida,
            chegada,
            portoOrigemCombo.getValue().getIdPorto(),
            portoDestinoCombo.getValue().getIdPorto(),
            estadoViagemCombo.getValue()
        );

        int idViagem = new ViagemDAO().inserirViagem(viagem);
        if (idViagem == 0) {
            mostrarErro("Nao foi possivel guardar a viagem.");
            return;
        }

        CargaViagemDAO cargaViagemDAO = new CargaViagemDAO();
        for (Carga carga : cargasViagem) {
            cargaViagemDAO.inserirLigacao(idViagem, carga.getIdCarga());
        }

        TripulacaoDAO tripulacaoDAO = new TripulacaoDAO();
        for (Map.Entry<Integer, Integer> entry : funcoesPorTripulante.entrySet()) {
            tripulacaoDAO.inserirLigacao(idViagem, entry.getKey(), entry.getValue());
        }

        Stage stage = (Stage) navioCombo.getScene().getWindow();
        stage.close();
    }

    private boolean validarCampos() {
        if (navioCombo.getValue() == null) {
            mostrarErro("Por favor seleciona o navio.");
            return false;
        }
        if (portoOrigemCombo.getValue() == null) {
            mostrarErro("Por favor seleciona o porto de origem.");
            return false;
        }
        if (portoDestinoCombo.getValue() == null) {
            mostrarErro("Por favor seleciona o porto de destino.");
            return false;
        }
        if (portoOrigemCombo.getValue().getIdPorto() == portoDestinoCombo.getValue().getIdPorto()) {
            mostrarErro("O porto de origem e destino tem de ser diferente.");
            return false;
        }
        if (dataPartidaPicker.getValue() == null) {
            mostrarErro("Por favor seleciona a data de partida.");
            return false;
        }
        if (dataChegadaPrevistaPicker.getValue() == null) {
            mostrarErro("Por favor seleciona a data de chegada prevista.");
            return false;
        }
        if (estadoViagemCombo.getValue() == null) {
            mostrarErro("Por favor seleciona o estado da viagem.");
            return false;
        }
        return true;
    }

    private Navio getNavioSelecionado() {
        int index = navioCombo.getSelectionModel().getSelectedIndex();
        if (index < 0 || index >= navios.size()) {
            return null;
        }
        return navios.get(index);
    }

    private Set<Integer> idsCargasSelecionadas() {
        Set<Integer> ids = new HashSet<>();
        for (Carga carga : cargasViagem) {
            ids.add(carga.getIdCarga());
        }
        return ids;
    }

    private void mostrarErro(String mensagem) {
        erroLabel.setText(mensagem);
        erroLabel.setVisible(true);
        erroLabel.setManaged(true);
    }

    private void limparErro() {
        erroLabel.setText("");
        erroLabel.setVisible(false);
        erroLabel.setManaged(false);
    }
}
