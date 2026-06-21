package com.navios.controller;

import java.time.LocalDate;

import com.navios.DB.TripulanteDAO;
import com.navios.model.Tripulante;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AdicionarTripulanteController {

    @FXML private TextField txtNome;
    @FXML private TextField txtSobrenome;
    @FXML private ComboBox<String> estadoCombo;
    @FXML private TextField txtNacionalidade;
    @FXML private DatePicker dataNascimentoPicker;
    @FXML private Label erroLabel;

    @FXML
    private void initialize() {
        estadoCombo.setItems(FXCollections.observableArrayList(
            "Disponível", "Em Viagem", "De Licença", "Inativo"
        ));
    }

    @FXML
    private void handleFecharModal() {
        Stage stage = (Stage) txtNome.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleGuardarTripulante() {
        if (txtNome.getText().isBlank()) {
            mostrarErro("Por favor insere o nome.");
            return;
        }
        if (txtSobrenome.getText().isBlank()) {
            mostrarErro("Por favor insere o sobrenome.");
            return;
        }
        if (estadoCombo.getValue() == null) {
            mostrarErro("Por favor seleciona o estado de disponibilidade.");
            return;
        }
        if (txtNacionalidade.getText().isBlank()) {
            mostrarErro("Por favor insere a nacionalidade.");
            return;
        }
        if (dataNascimentoPicker.getValue() == null) {
            mostrarErro("Por favor seleciona a data de nascimento.");
            return;
        }

        LocalDate dataNascimento = dataNascimentoPicker.getValue();
        if (dataNascimento.isAfter(LocalDate.now().minusYears(16))) {
            mostrarErro("O tripulante deve ter pelo menos 16 anos.");
            return;
        }

        Tripulante t = new Tripulante(
            0,
            txtNome.getText().trim(),
            txtSobrenome.getText().trim(),
            estadoCombo.getValue(),
            txtNacionalidade.getText().trim(),
            dataNascimento
        );

        new TripulanteDAO().inserirTripulante(t);

        Stage stage = (Stage) txtNome.getScene().getWindow();
        stage.close();
    }

    private void mostrarErro(String mensagem) {
        erroLabel.setText(mensagem);
        erroLabel.setVisible(true);
        erroLabel.setManaged(true);
    }
}