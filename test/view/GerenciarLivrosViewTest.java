package view;

import dao.ConnectionFactory;
import dao.ConnectionFactory;
import dao.LivroDAO;
import dao.LivroDAO;
import models.Livro;
import org.junit.*;
import view.GerenciarLivrosView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.util.Date;

import static org.junit.Assert.*;

public class GerenciarLivrosViewTest {

    private static Connection connection;
    private LivroDAO livroDAO;
    private GerenciarLivrosView gerenciarLivrosView;

    @BeforeClass
    public static void setupClass() throws Exception {
        connection = ConnectionFactory.getConnection();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }

    @Before
    public void setup() throws Exception {
        livroDAO = new LivroDAO(connection);
        gerenciarLivrosView = new GerenciarLivrosView();
    }

    @After
    public void tearDown() throws Exception {
        connection.createStatement().executeUpdate("DELETE FROM livro");
    }

}
