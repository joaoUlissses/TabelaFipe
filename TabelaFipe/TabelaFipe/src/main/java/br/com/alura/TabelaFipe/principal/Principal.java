package br.com.alura.TabelaFipe.principal;

import br.com.alura.TabelaFipe.model.Dados;
import br.com.alura.TabelaFipe.model.Modelos;
import br.com.alura.TabelaFipe.model.Veiculo;
import br.com.alura.TabelaFipe.service.ConsumoApi;
import br.com.alura.TabelaFipe.service.ConverteDados;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private final Scanner leitura= new Scanner(System.in);
    private final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";
    private final ConsumoApi consumo = new ConsumoApi();
    private final ConverteDados conversor = new ConverteDados();
    
    
    public void exibeMenu(){
        var menu = """
                   
                   OPCOES
                   
                   1. carro
                   2. moto
                   3. caminhao
                   
                   digite uma das opcoes para consulta: 
                   """;
        System.out.println(menu);
        var opcao =  leitura.nextLine();
        
        String endereco;
        if(opcao.toLowerCase().contains("1")){
            endereco = URL_BASE+ "carros/marcas";
        }else if (opcao.toLowerCase().contains("2")){
        endereco = URL_BASE+"motos/marcas";
        }else {
            endereco=URL_BASE+"caminhoes/marcas";
        }
        var json = consumo.obterDados(endereco);
        System.out.println(json);
         var marcas = conversor.obterLista(json, Dados.class);
         marcas.stream()
                 .sorted(Comparator.comparing(Dados::codigo))
                 .forEach(System.out::println);
         
         System.out.println("""
                            
                            informe o codigo da marca para filtro 
                            
                            """);
         
         var codigoMarca=leitura.nextLine();
         endereco = endereco+"/" + codigoMarca +"/modelos";
         System.out.println(endereco);
         json = consumo.obterDados(endereco);
         
         var modeloLista  = conversor.obterDados(json, Modelos.class);
         
         System.out.println("modelos dessa marca: ");
         
         modeloLista.modelos().stream()
                 .sorted(Comparator.comparing(Dados::codigo))
                 .forEach(System.out::println);
         
        System.out.println("digite um treco do nome do carro a ser buscado:");
        var nomeVeiculo = leitura.nextLine();
        
        List<Dados> modelosFiltrados = modeloLista.modelos().stream()
                .filter(m->m.nome().toLowerCase().contains(nomeVeiculo.toLowerCase()))
                .collect(Collectors.toList());
        System.out.println("""
                           modelos filtrados
                           
                           """);
        
        modelosFiltrados.forEach(System.out::println);
        
        System.out.println("digite o codigo do modelo: ");
        var codigoModelo = leitura.nextLine();
        
        endereco = endereco + "/" +codigoModelo + "/anos";
        json = consumo.obterDados(endereco);
        List<Dados> anos = conversor.obterLista(json, Dados.class);
        
        List<Veiculo> automoveis= new ArrayList<>();
        

for (int i = 0; i < anos.size(); i++) {
    
    var enderecoAnos = endereco + "/" + anos.get(i).codigo();
    json = consumo.obterDados(enderecoAnos);
    Veiculo veiculo = conversor.obterDados(json, Veiculo.class);
    automoveis.add(veiculo);
    }

System.out.println("\n Todos os veiculos filtrados com avaliações por ano: ");
automoveis.forEach(System.out::println);

}
}
