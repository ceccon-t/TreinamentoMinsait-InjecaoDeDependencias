package dev.ceccon.mywebtestframework.controller;

import dev.ceccon.mywebtestframework.model.Produto;
import dev.ceccon.mywebtestframework.service.IService;
import dev.ceccon.webframework.annotations.*;

@WebframeworkController
public class HelloController {

    @WebframeworkInject
    private IService iService;

    @WebframeworkGetMethod("/hello")
    public String returnHelloWorld() {
        return "Hello World!!!";
    }

    @WebframeworkGetMethod("/produto")
    public Produto exibirProduto() {
        Produto p = new Produto(1, "Nome1", 2000.0, "teste.jpg");
        return p;
    }

    @WebframeworkPostMethod("/produto")
    public Produto cadastraProduto(@WebframeworkBody Produto produtoNovo) {
        System.out.println(produtoNovo);
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
    public String retornoValor(@WebframeworkPathVariable String valor) {
        return "Retornando o valor de parametro: " + valor;
    }
}
