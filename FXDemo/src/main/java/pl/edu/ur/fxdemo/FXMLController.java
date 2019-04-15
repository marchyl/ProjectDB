package pl.edu.ur.fxdemo;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import pl.edu.ur.fxdemo.database.DatabaseHelper;

public class FXMLController implements Initializable {

    @FXML
    private Label label;

    @FXML
    private Button loadButton;

    @FXML
    private Button cleanButton;

    @FXML
    private Button executeButton;

    @FXML
    private TextArea sqlQuery;

    @FXML
    private TableView<ObservableList> tableView;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnCreate;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtLastname;

    DatabaseHelper dbHelper = new DatabaseHelper();
    private ObservableList data = FXCollections.observableArrayList();
    
    
    /**
     * 
     * Database function example
     * @param event
     * @throws SQLException 
     */
    @FXML
    private void execDBFunction(ActionEvent event) throws SQLException {
        Connection conn = dbHelper.getConnection();
        //ResultSet rs = conn.prepareCall("SELECT test(1,1,2)").executeQuery();
        ResultSet rs = conn.prepareStatement("SELECT test(1,1,2)").executeQuery();
        loadData(rs);
        conn.close();
        
        
    }
    
    /*
CREATE OR REPLACE FUNCTION test(person_id_1 NUMERIC, person_id_2 NUMERIC, place_id NUMERIC) 
 RETURNS NUMERIC AS $$
 DECLARE max_id NUMERIC;
BEGIN
	INSERT INTO dates 
        VALUES (NEXTVAL('studseq'), person_id_1, person_id_2, 1);

	SELECT max(id) 
          INTO max_id 
          FROM dates;

	RETURN max_id;
END;
$$ LANGUAGE plpgsql;
     */

    @FXML
    void loadStudentsData(ActionEvent event) throws SQLException {

        Connection conn = dbHelper.getConnection();
        ResultSet rs = conn.prepareStatement("SELECT * FROM student").executeQuery();
        loadData(rs);
        conn.close();

    }

    @FXML
    void cleanStudentsData(ActionEvent event) {
        if (!tableView.getItems().isEmpty()) {
            tableView.getItems().clear();
        }
    }

    @FXML
    void executeQuery(ActionEvent event) throws SQLException {
        Connection conn = dbHelper.getConnection();
        tableView.getItems().clear();
        tableView.getColumns().clear();
        ResultSet rs = conn.prepareStatement(sqlQuery.getText()).executeQuery();
        loadData(rs);
        conn.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void loadData(ResultSet rs) throws SQLException {

        if (!tableView.getItems().isEmpty()) {
            tableView.getItems().clear();
        }

        if (!tableView.getColumns().isEmpty()) {
            tableView.getColumns().clear();
        }

        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
            //We are using non property style for making dynamic table
            final int j = i;
            TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
            col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(param.getValue().get(j).toString());
                }
            });

            tableView.getColumns().addAll(col);
            System.out.println("Column [" + i + "] ");
        }

        while (rs.next()) {
            //Iterate Row
            ObservableList<String> row = FXCollections.observableArrayList();
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                //Iterate Column
                row.add(String.valueOf(rs.getObject(i)));

            }

            //System.out.println("Row [1] added " + row.toString());
            data.add(row);

        }

        //FINALLY ADDED TO TableView
        tableView.setItems(data);
    }

    @FXML
    void cancel(ActionEvent event) {
        txtLastname.setText("");
        txtName.setText("");
    }

    @FXML
    void create(ActionEvent event) {
        try {

            String name = txtName.getText();
            String lastname = txtLastname.getText();

            if (!name.equals("") || !lastname.equals("")) {

                Connection conn = dbHelper.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO student (name, lastname) VALUES (?, ?)");
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, lastname);
                preparedStatement.execute();
                
                txtLastname.setText("");
                txtName.setText("");
                conn.close();

                AlertUtil.showAlertInfo("Succes!", "New student added.", "");
            } else {
                AlertUtil.showAlertWarnion("Warning!", "Empty fields!", "");
            }
        } catch (SQLException e) {
            AlertUtil.showAlertError("Error!", "SQLException!", e.getMessage());
        }

    }
}