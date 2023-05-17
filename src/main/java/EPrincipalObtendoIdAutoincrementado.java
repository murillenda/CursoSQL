import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class EPrincipalObtendoIdAutoincrementado {
    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("Valor total: ");
        BigDecimal valorTotal = new BigDecimal(scanner.nextLine());

        System.out.print("Data de pagamento: ");
        LocalDate dataPagamento = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        String dml = """
                INSERT INTO venda (
                    nome_cliente,
                    valor_total,
                    data_pagamento
                ) values (?, ?, ?)
                """;

        try (Connection conexao = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/comercial","root", "admin");
             // Ao adicionarmos o RETURN_GENERATED_KEYS, ele retornará o autoincremento para nós após o executeUpdate
             PreparedStatement comando = conexao.prepareStatement(dml, Statement.RETURN_GENERATED_KEYS)) {
            comando.setString(1, nome);
            comando.setBigDecimal(2, valorTotal);
            comando.setDate(3, Date.valueOf(dataPagamento));
            comando.executeUpdate();

            // Assim pegamos os autoincrementos
            ResultSet codigoResultSet = comando.getGeneratedKeys();
            codigoResultSet.next(); //Vamos para o primeiro registro
            // Pegamos o valor da primeira coluna
            long codigoGerado = codigoResultSet.getLong(1);

            System.out.println("Venda cadastrada com código " + codigoGerado + "!");
        } catch (SQLException e) {
            System.out.println("Erro cadastrando venda");
            e.printStackTrace();
        }
    }
}
