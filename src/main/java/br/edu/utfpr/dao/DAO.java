/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.dao;

import br.edu.utfpr.dto.PaisDTO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Uildson
 */
public abstract class DAO<T> {

    protected abstract String getStrigExcluirSql();

    protected abstract String getStrigIncluirSql();

    protected abstract String getStrigAlterarSql();

    protected abstract String getStrigListarSql();

    protected abstract T getFilho(ResultSet result);

    protected abstract PreparedStatement getPrepStmtIncluir(PreparedStatement stm, T entidade);

    protected abstract PreparedStatement getPrepStmtAlterar(PreparedStatement stm, T entidade);

    public boolean incluir(T entidade) {
        try (Connection conn = DriverManager.getConnection("jdbc:derby:memory:database")) {

            PreparedStatement statement = conn.prepareStatement(getStrigIncluirSql());

            int rowsInserted = getPrepStmtIncluir(statement, entidade).executeUpdate();

            if (rowsInserted > 0) {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean alterar(T entidade) {
        try (Connection conn = DriverManager.getConnection("jdbc:derby:memory:database")) {

            PreparedStatement statement = conn.prepareStatement(getStrigAlterarSql());

            int rowsUpdated = getPrepStmtAlterar(statement, entidade).executeUpdate();
            if (rowsUpdated > 0) {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean excluir(int id) {
        try (Connection conn = DriverManager.getConnection("jdbc:derby:memory:database")) {

            PreparedStatement statement = conn.prepareStatement(getStrigExcluirSql());
            statement.setInt(1, id);

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<T> listarTodos() {

        List<T> resultado = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection("jdbc:derby:memory:database")) {

            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(getStrigListarSql());

            while (result.next()) {

                resultado.add(getFilho(result));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultado;

    }

}
