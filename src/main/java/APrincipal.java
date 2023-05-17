import java.math.BigDecimal;
import java.sql.*;

public class APrincipal {
    public static void main(String[] args) {
        // Para ver como colocar a url, sempre olhar a documentação do driver JDBC
        try (Connection conexao = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/comercial","root", "admin");
             // Statement é o que representa um comando SQL, no JDBC existem diferentes tipos, aqui vamos usar a mais básica chamada
             // Statement
             Statement comando = conexao.createStatement()) {
            // Ao fechar um Statement, a documentação fala que o ResultSet é automaticamente fechado
            ResultSet resultado = comando.executeQuery("SELECT * FROM venda");
            // Já o Statement é boa prática fechar, pois existem algumas libs que sobrescrevem o close do Statement, correndo perigo de não ser fechado ao fechamendo da conexão

            while (resultado.next()) { // .next retorna um booleano, se tiver próximo retorna true
                Long id = resultado.getLong("id");
                String nome = resultado.getString("nome_cliente");
                BigDecimal valorTotal = resultado.getBigDecimal("valor_total");
                Date dataPagamento = resultado.getDate("data_pagamento"); //Atenção aqui, pois utilizamos o Date do sql e não do java util

                System.out.printf("%d - %s - R$%.2f - %s%n",
                        id, nome, valorTotal, dataPagamento);
            }
            // Aqui podemos ver que a interface é a mesma, mas a implementação é diferente dependendo do banco de dados que utilizar, com o getClass podemos ver as implementações
            System.out.println(conexao.getClass());
            System.out.println(comando.getClass());
            System.out.println(resultado.getClass());
        } catch (SQLException e) {
            System.out.println("Erro de banco de dados");
            e.printStackTrace();
        }


    }
}
