package com.navios.controller;

import com.navios.DB.NavioDAO;
import com.navios.model.Navio;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AdicionarNavioController {

    @FXML private TextField nomeNavioField;
    @FXML private TextField identificadorImoField;
    @FXML private TextField bandeiraField;
    @FXML private TextField anoFabricoField;       // era "anoField" → não existia no FXML
    @FXML private ComboBox<String> tipoNavioCombo;
    @FXML private ComboBox<String> estadoOperacionalCombo;
    @FXML private Spinner<Integer> compartimentosSpinner; // era "capacidadeField" → não existia
    @FXML private Spinner<Integer> maxCargasSpinner;      // novo
    @FXML private Label erroLabel;

    // "observacoesArea" removida — não existe no FXML

    @FXML
    private void handleFecharModal() {
        Stage stage = (Stage) nomeNavioField.getScene().getWindow();
        stage.close();
    }

    @FXML
   private void handleGuardarNavio() {
        if (nomeNavioField.getText().isBlank()) {
            mostrarErro("O campo 'Nome do Navio' é obrigatório.");
            return;
        }
        if (identificadorImoField.getText().isBlank()) {
            mostrarErro("O campo 'Identificador IMO' é obrigatório.");
            return;
        }
        if (bandeiraField.getText().isBlank()) {
            mostrarErro("O campo 'Bandeira' é obrigatório.");
            return;
        }
        if (tipoNavioCombo.getValue() == null) {
            mostrarErro("Por favor seleciona o tipo de navio.");
            return;
        }
        if (estadoOperacionalCombo.getValue() == null) {
            mostrarErro("Por favor seleciona o estado do navio.");
            return;
        }

        try {
            // Tipo é int na BD — usa o índice selecionado + 1
            int tipo = tipoNavioCombo.getSelectionModel().getSelectedIndex() + 1;

            Navio navio = new Navio(
                0,
                nomeNavioField.getText().trim(),
                identificadorImoField.getText().trim(),
                tipo,
                compartimentosSpinner.getValue(),
                maxCargasSpinner.getValue(),
                bandeiraField.getText().trim(),
                Integer.parseInt(anoFabricoField.getText().trim()),
                estadoOperacionalCombo.getValue()
            );

            new NavioDAO().inserirNavio(navio);

            Stage stage = (Stage) nomeNavioField.getScene().getWindow();
            stage.close();

        } catch (NumberFormatException e) {
            mostrarErro("O campo 'Ano de Fabrico' tem de ser um número válido.");
        }
    }


    private void mostrarErro(String mensagem) {
        erroLabel.setText(mensagem);
        erroLabel.setVisible(true);
        erroLabel.setManaged(true);
    }
}