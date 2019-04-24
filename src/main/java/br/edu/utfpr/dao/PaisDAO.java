package br.edu.utfpr.dao;

import java.sql.Connection;
import java.sql.DriverManager;

import br.edu.utfpr.dto.PaisDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.extern.java.Log;

@Log
public class PaisDAO extends DAO<PaisDTO> {

    // Responsável por criar a tabela País no banco
    public PaisDAO() {
        try (Connection conn = DriverManager.getConnection("jdbc:derby:memory:database;create=true")) {

            log.info("Criando tabela pais ...");
            conn.createStatement().executeUpdate(
                    "CREATE TABLE pais ("
                    + "id int NOT NULL GENERATED ALWAYS AS IDENTITY CONSTRAINT id_pais_pk PRIMARY KEY,"
                    + "nome varchar(255),"
                    + "sigla varchar(3),"
                    + "codigoTelefone int)");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getStrigIncluirSql() {
        return "INSERT INTO pais (nome, sigla, codigoTelefone) VALUES (?, ?, ?)";
    }

    @Override
    protected String getStrigExcluirSql() {
        return "DELETE FROM pais WHERE id=?";
    }

    @Override
    protected String getStrigAlterarSql() {
        return "UPDATE pais SET nome=?, sigla=?, codigoTelefone=? WHERE id=?";
    }

    @Override
    protected String getStrigListarSql() {
        return "SELECT * FROM pais";
    }

    @Override
    protected PreparedStatement getPrepStmtIncluir(PreparedStatement stm, PaisDTO pais) {
        try {
            stm.setString(1, pais.getNome());
            stm.setString(2, pais.getSigla());
            stm.setInt(3, pais.getCodigoTelefone());

            return stm;
        } catch (SQLException ex) {
            Logger.getLogger(PaisDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return stm; //posso retornar uma exceção aqui
    }

    @Override
    protected PaisDTO getFilho(ResultSet result) {
        PaisDTO resultado = null;
        try {
            resultado.builder()
                    .codigoTelefone(result.getInt("codigoTelefone"))
                    .id(result.getInt("id"))
                    .nome(result.getString("nome"))
                    .sigla(result.getString("sigla"))
                    .build();

        } catch (SQLException ex) {
            Logger.getLogger(PaisDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultado;
    }

    @Override
    protected PreparedStatement getPrepStmtAlterar(PreparedStatement stm, PaisDTO pais) {
        try {
            stm.setString(1, pais.getNome());
            stm.setString(2, pais.getSigla());
            stm.setInt(3, pais.getCodigoTelefone());
            stm.setInt(4, pais.getId());
            return stm;
        } catch (SQLException ex) {
            Logger.getLogger(PaisDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return stm; //posso retornar uma exceção aqui
    }
    
    public PaisDTO listarPorId (int id) {
        return this.listarTodos().stream().filter(p -> p.getId() == id).findAny().orElseThrow(RuntimeException::new);
    }
}
