import db.DB;
import db.DbException;


import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Main {
    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        Connection conn = null;
        PreparedStatement st = null;

        try{
            conn = DB.getConnection();

            /*
            st = conn.prepareStatement(
                    "INSERT INTO seller"
                    + "(Name, Email, BirthDate, BaseSalary, DepartmentId)"
                    + "VALUES "
                    + "(?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            st.setString(1, "Carl Purple");
            st.setString(2, "carl@gmail.com");
            st.setDate(3, new java.sql.Date(sdf.parse("28/04/1985").getTime()));
            st.setDouble(4,3000.0);
            st.setInt(5, 4);


             */

            st = conn.prepareStatement("insert into department (Name) values ('D1'), ('D2')",
                    Statement.RETURN_GENERATED_KEYS);
            int rowsAffected = st.executeUpdate();

            if(rowsAffected > 0){
                ResultSet rs = st.getGeneratedKeys();
                while (rs.next()){
                    int id = rs.getInt(1);
                    System.out.println("Done! ID = " + id);
                }

            }else {
                System.out.println("No rown affected!");
            }

        }catch (SQLException e){
            e.printStackTrace();

        } /*catch (ParseException e){
            e.printStackTrace();

        }*/

        finally {
           DB.closeStatement(st);
           DB.closeConnection();

        }


/*
        // Declarar variáveis para a conexão com o banco de dados, statement e resultado
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            // Obter uma conexão com o banco de dados usando um método personalizado (DB.getConnection())
            conn = DB.getConnection();

            // Criar um statement para executar consultas SQL
            st = conn.createStatement();

            // Executar uma consulta SELECT na tabela "department"
            rs = st.executeQuery("select * from department");

            // Iterar sobre o resultado e imprimir os valores das colunas "Id" e "Name"
            while (rs.next()) {
                System.out.println(rs.getInt("Id") + ", " + rs.getString("Name"));
            }
        } catch (SQLException e) {
            // Lidar com quaisquer exceções SQL que possam ocorrer
            e.printStackTrace();
        } finally {
            // Fechar o resultado, o statement e a conexão com o banco de dados no bloco finally
            DB.closeResultSet(rs);
            DB.closeStatement(st);
            DB.closeConnection();
        }
*/
    }
}