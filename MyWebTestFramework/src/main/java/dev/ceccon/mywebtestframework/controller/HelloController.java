package dev.ceccon.mywebtestframework.controller;

import dev.ceccon.mywebtestframework.model.Produto;
import dev.ceccon.mywebtestframework.service.IService;
import dev.ceccon.mywebtestframework.service.ProdutoService;
import dev.ceccon.webframework.annotations.*;

import java.util.List;

@WebframeworkController
public class HelloController {

    @WebframeworkInject
    private IService iService;

    @WebframeworkInject
    private ProdutoService produtoService;

    @WebframeworkGetMethod("/hello")
    public String returnHelloWorld() {
        return "Hello World!!!";
    }

    @WebframeworkGetMethod("/produto")
    public List<Produto> listarProdutos() {
        return produtoService.listar();
    }

    @WebframeworkPostMethod("/produto")
    public Produto cadastraProduto(@WebframeworkBody Produto produtoNovo) {
        produtoNovo = produtoService.criar(produtoNovo);
        return produtoNovo;
    }

    @WebframeworkGetMethod("/teste")
    public String teste() {
        return "Teste!";
    }

    @WebframeworkGetMethod("/teste2")
    private String teste2() {
        return "Teste2!";
    }

    @WebframeworkGetMethod("/injected")
    public String chamadaCustom() {
        return iService.chamadaCustom("Hello injected");
    }

    // localhost://localhost:8080/retornavalor/22222 == Retornando o valor de parametro: 22222
    @WebframeworkGetMethod("/retornavalor/{valor}")
    public String retornoValor(@WebframeworkPathVariable Double valor) {
        return "Retornando o valor de parametro: " + valor;
    }

    // =======================
    // ENDPOINTS DA AVALIACAO:
    // =======================

    @WebframeworkDeleteMethod("/produto/{id}")
    public String deletarProduto(@WebframeworkPathVariable Integer id) {
        produtoService.deletar(id);
        return "Produto com id " + id + " deletado com sucesso.";
    }
}
