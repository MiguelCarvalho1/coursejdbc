import db.DB;
import db.DbException;
import db.DbIntegrityException;


import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Main {
    public static void main(String[] args) {


        //Demo de Transações.
        // Declarar variáveis para conexão com o banco de dados e Statement
        Connection conn = null;
        Statement st = null;

        try {
            // Obter uma conexão com o banco de dados usando um método personalizado (DB.getConnection())
            conn = DB.getConnection();

            // Desativar o autocommit para permitir o controle manual da transação
            conn.setAutoCommit(false);

            // Criar um Statement para executar comandos SQL
            st = conn.createStatement();

            // Atualizar os salários base em duas operações de atualização diferentes
            int rows1 = st.executeUpdate("UPDATE  seller SET BaseSalary = 2090 WHERE DepartmentID = 1");

    /* Simular um erro durante a transação (comentado para evitar exceções)
    int x = 1;
    if (x < 2) {
        throw new SQLException("Fake error");
    }
    */

            int rows2 = st.executeUpdate("UPDATE  seller SET BaseSalary = 3090 WHERE DepartmentID = 2");

            // Confirmar a transação se todas as operações foram bem-sucedidas
            conn.commit();

            // Imprimir o número de linhas afetadas em cada operação
            System.out.println("Rows 1 = " + rows1);
            System.out.println("Rows 2 = " + rows2);

        } catch (SQLException e) {
            // Lidar com exceções SQL durante a transação
            try {
                // Reverter a transação se ocorrer uma exceção
                conn.rollback();
                throw new DbException("Transaction rolled back! Caused by: " + e.getMessage());
            } catch (SQLException e1) {
                // Lidar com exceções ao tentar reverter a transação
                throw new DbException("Error trying to rollback! Caused by: " + e1.getMessage());
            }

        } finally {
            // Fechar o Statement e a conexão com o banco de dados no bloco finally
            DB.closeStatement(st);
            DB.closeConnection();
        }




        /*
        //Demo de Eliminar dados.
        // Declarar variáveis para conexão com o banco de dados e PreparedStatement
        Connection conn = null;
        PreparedStatement st = null;

        try {
            // Obter uma conexão com o banco de dados usando um método personalizado (DB.getConnection())
            conn = DB.getConnection();

            // Preparar uma instrução SQL para excluir um registro da tabela "department" onde o Id é igual a 5
            st = conn.prepareStatement(
                    "DELETE FROM department "
                            + "WHERE "
                            + "Id = ?");

            // Definir o parâmetro da instrução SQL
            st.setInt(1, 5); // Excluir o registro com Id igual a 5

            // Executar a instrução SQL e obter o número de linhas afetadas
            int rowsAffected = st.executeUpdate();

            // Imprimir a mensagem indicando que a operação foi concluída e o número de linhas afetadas
            System.out.println("Done! Rows Affected: " + rowsAffected);

        } catch (SQLException e) {
            // Lidar com exceções SQL, neste caso, lançar uma exceção personalizada (DbIntegrityException)
            throw new DbIntegrityException(e.getMessage());

        } finally {
            // Fechar o PreparedStatement e a conexão com o banco de dados no bloco finally
            DB.closeStatement(st);
            DB.closeConnection();
        }


        //Demo de Atualizar dados na base dados.
        // Declarar variáveis para conexão com o banco de dados e PreparedStatement
        Connection conn = null;
        PreparedStatement st = null;

        try {
            // Obter uma conexão com o banco de dados usando um método personalizado (DB.getConnection())
            conn = DB.getConnection();

            // Preparar uma instrução SQL para atualizar o salário base em todos os registros de "seller" com DepartmentId igual a 2
            st = conn.prepareStatement(
                    "UPDATE  seller "
                            + "SET BaseSalary = BaseSalary + ? "
                            + "WHERE "
                            + "(DepartmentId = ?)");

            // Definir os parâmetros da instrução SQL
            st.setDouble(1, 200.0); // Aumento de salário de 200.0
            st.setInt(2, 2); // Condição: DepartmentId igual a 2

            // Executar a instrução SQL e obter o número de linhas afetadas
            int rowsAffected = st.executeUpdate();

            // Imprimir a mensagem indicando que a operação foi concluída e o número de linhas afetadas
            System.out.println("Done! Rows Affected: " + rowsAffected);

        } catch (SQLException e) {
            // Lidar com quaisquer exceções SQL que possam ocorrer
            e.printStackTrace();

        } finally {
            // Fechar o PreparedStatement e a conexão com o banco de dados no bloco finally
            DB.closeStatement(st);
            DB.closeConnection();
        }


        // Importar classe SimpleDateFormat para formatação de datas
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

// Declarar variáveis para conexão com o banco de dados e PreparedStatement
        Connection conn = null;
        PreparedStatement st = null;

        try {
            // Obter uma conexão com o banco de dados usando um método personalizado (DB.getConnection())
            conn = DB.getConnection();


    // Exemplo de preparação de uma instrução SQL para inserir dados em uma tabela "seller"
    st = conn.prepareStatement(
            "INSERT INTO seller"
            + "(Name, Email, BirthDate, BaseSalary, DepartmentId)"
            + "VALUES "
            + "(?,?,?,?,?)",
            Statement.RETURN_GENERATED_KEYS);
    st.setString(1, "Carl Purple");
    st.setString(2, "carl@gmail.com");
    st.setDate(3, new java.sql.Date(sdf.parse("28/04/1985").getTime()));
    st.setDouble(4, 3000.0);
    st.setInt(5, 4);


            // Preparar uma instrução SQL para inserir dados em uma tabela "department"
            st = conn.prepareStatement("insert into department (Name) values ('D1'), ('D2')",
                    Statement.RETURN_GENERATED_KEYS);

            // Executar a instrução SQL e obter o número de linhas afetadas
            int rowsAffected = st.executeUpdate();

            // Verificar se houve linhas afetadas
            if (rowsAffected > 0) {
                // Se sim, obter as chaves geradas (IDs) e imprimir
                ResultSet rs = st.getGeneratedKeys();
                while (rs.next()) {
                    int id = rs.getInt(1);
                    System.out.println("Done! ID = " + id);
                }
            } else {
                // Se não houve linhas afetadas
                System.out.println("No rows affected!");
            }

        } catch (SQLException e) {
            // Lidar com quaisquer exceções SQL que possam ocorrer
            e.printStackTrace();

        } /*catch (ParseException e){
    // Lidar com quaisquer exceções de análise de data que possam ocorrer
    e.printStackTrace();
}

        finally {
            // Fechar o PreparedStatement e a conexão com o banco de dados no bloco finally
            DB.closeStatement(st);
            DB.closeConnection();
        }

                //Mudança de Demo de baixo é primeiro só de conexão enquanto de cima de inserir dados.


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