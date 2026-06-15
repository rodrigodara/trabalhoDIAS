package com.navios.controller;

import java.time.LocalDate;
import java.util.List;

import com.navios.DB.NavioDAO;
import com.navios.DB.PortoDAO;
import com.navios.DB.ViagemDAO;
import com.navios.model.Navio;
import com.navios.model.Porto;
import com.navios.model.Viagem;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AdicionarViagemController {

    @FXML private ComboBox<String> navioCombo;
    @FXML private ComboBox<Porto> portoOrigemCombo;
    @FXML private ComboBox<Porto> portoDestinoCombo;
    @FXML private DatePicker dataPartidaPicker;
    @FXML private DatePicker dataChegadaPrevistaPicker;
    @FXML private ComboBox<String> estadoViagemCombo;
    @FXML private Label erroLabel;

    private List<Navio> navios;

    @FXML
    private void initialize() {
        // Popula o combo de navios a partir da BD
        navios = new NavioDAO().listarNavios();
        navioCombo.setItems(FXCollections.observableArrayList(
            navios.stream().map(Navio::getNome).toList()
        ));

        // Popula os combos de portos com dados reais da BD
        List<Porto> portos = new PortoDAO().listarPortos();
        var listaPortos = FXCollections.observableArrayList(portos);
        portoOrigemCombo.setItems(listaPortos);
        portoDestinoCombo.setItems(listaPortos);
    }

    @FXML
    private void handleFecharModal() {
        Stage stage = (Stage) navioCombo.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleGuardarViagem() {
        if (navioCombo.getValue() == null) {
            mostrarErro("Por favor seleciona o navio.");
            return;
        }
        if (portoOrigemCombo.getValue() == null) {
            mostrarErro("Por favor seleciona o porto de origem.");
            return;
        }
        if (portoDestinoCombo.getValue() == null) {
            mostrarErro("Por favor seleciona o porto de destino.");
            return;
        }
        if (dataPartidaPicker.getValue() == null) {
            mostrarErro("Por favor seleciona a data de partida.");
            return;
        }
        if (dataChegadaPrevistaPicker.getValue() == null) {
            mostrarErro("Por favor seleciona a data de chegada prevista.");
            return;
        }
        if (estadoViagemCombo.getValue() == null) {
            mostrarErro("Por favor seleciona o estado da viagem.");
            return;
        }

        LocalDate partida = dataPartidaPicker.getValue();
        LocalDate chegada = dataChegadaPrevistaPicker.getValue();

        if (chegada.isBefore(partida)) {
            mostrarErro("A data de chegada prevista não pode ser anterior à data de partida.");
            return;
        }

        int idNavio        = navios.get(navioCombo.getSelectionModel().getSelectedIndex()).getIdNavio();
        int portoOrigemId  = portoOrigemCombo.getValue().getIdPorto();
        int portoDestinoId = portoDestinoCombo.getValue().getIdPorto();

        Viagem viagem = new Viagem(
            0,
            idNavio,
            partida,
            chegada,
            portoOrigemId,
            portoDestinoId,
            estadoViagemCombo.getValue()
        );

        new ViagemDAO().inserirViagem(viagem);

        Stage stage = (Stage) navioCombo.getScene().getWindow();
        stage.close();
    }

    private void mostrarErro(String mensagem) {
        erroLabel.setText(mensagem);
        erroLabel.setVisible(true);
        erroLabel.setManaged(true);
    }
}