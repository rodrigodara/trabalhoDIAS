package com.navios.controller;

import java.util.List;

import com.navios.DB.CargasDAO;
import com.navios.DB.PortoDAO;
import com.navios.DB.TipoCargaDAO;
import com.navios.model.Carga;
import com.navios.model.Porto;
import com.navios.model.TipoCarga;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AdicionarCargasController {

    @FXML private TextField txtDesignacao;
    @FXML private ComboBox<TipoCarga> tipoCombo;
    @FXML private TextField txtVolume;
    @FXML private TextField txtPeso;
    @FXML private ComboBox<Porto> portoCargaCombo;
    @FXML private ComboBox<Porto> portoDescargaCombo;
    @FXML private Label erroLabel;

    @FXML
    private void initialize() {
        // Popula tipos de carga
        List<TipoCarga> tipos = new TipoCargaDAO().listarTipos();
        tipoCombo.setItems(FXCollections.observableArrayList(tipos));

        // Popula portos
        List<Porto> portos = new PortoDAO().listarPortos();
        var listaPortos = FXCollections.observableArrayList(portos);
        portoCargaCombo.setItems(listaPortos);
        portoDescargaCombo.setItems(listaPortos);
    }

    @FXML
    private void handleFecharModal() {
        Stage stage = (Stage) txtDesignacao.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleGuardarCarga() {
        if (txtDesignacao.getText().isBlank()) {
            mostrarErro("Por favor insere a designação.");
            return;
        }
        if (tipoCombo.getValue() == null) {
            mostrarErro("Por favor seleciona o tipo de carga.");
            return;
        }
        if (txtVolume.getText().isBlank()) {
            mostrarErro("Por favor insere o volume.");
            return;
        }
        if (txtPeso.getText().isBlank()) {
            mostrarErro("Por favor insere o peso.");
            return;
        }
        if (portoCargaCombo.getValue() == null) {
            mostrarErro("Por favor seleciona o porto de carga.");
            return;
        }
        if (portoDescargaCombo.getValue() == null) {
            mostrarErro("Por favor seleciona o porto de descarga.");
            return;
        }

        float volume, peso;
        try {
            volume = Float.parseFloat(txtVolume.getText().trim());
            peso   = Float.parseFloat(txtPeso.getText().trim());
        } catch (NumberFormatException e) {
            mostrarErro("Volume e Peso devem ser números.");
            return;
        }

        Carga carga = new Carga(
            0,
            txtDesignacao.getText().trim(),
            tipoCombo.getValue().getIdTipoCarga(), // ✅ ID real da tabela Tipo_Carga
            volume,
            peso,
            portoCargaCombo.getValue().getIdPorto(),
            portoDescargaCombo.getValue().getIdPorto()
        );

        new CargasDAO().inserirCarga(carga);

        Stage stage = (Stage) txtDesignacao.getScene().getWindow();
        stage.close();
    }

    private void mostrarErro(String mensagem) {
        erroLabel.setText(mensagem);
        erroLabel.setVisible(true);
        erroLabel.setManaged(true);
    }
}