import java.math.BigDecimal;
import java.sql.*;
import java.util.Scanner;

public class CPrincipalPreparedStatementSolucaoParaSQLInjection {
    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        System.out.print("Pesquisa por nome: ");
        String nomePesquisa = scanner.nextLine();

        try (Connection conexao = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/comercial","root", "admin");
             PreparedStatement comando = conexao.prepareStatement(
                     "SELECT * FROM venda WHERE nome_cliente like ?")) {

            comando.setString(1, "%" + nomePesquisa + "%");
            ResultSet resultado = comando.executeQuery();

            while (resultado.next()) {
                Long id = resultado.getLong("id");
                String nome = resultado.getString("nome_cliente");
                BigDecimal valorTotal = resultado.getBigDecimal("valor_total");
                Date dataPagamento = resultado.getDate("data_pagamento"); //Atenção aqui, pois utilizamos o Date do sql e não do java util

                System.out.printf("%d - %s - R$%.2f - %s%n",
                        id, nome, valorTotal, dataPagamento);
            }
        } catch (SQLException e) {
            System.out.println("Erro de banco de dados");
            e.printStackTrace();
        }


    }
}
