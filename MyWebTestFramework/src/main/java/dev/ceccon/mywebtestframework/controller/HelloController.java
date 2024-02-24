package dev.ceccon.mywebtestframework.controller;

import dev.ceccon.mywebtestframework.model.Produto;
import dev.ceccon.webframework.annotations.WebframeworkBody;
import dev.ceccon.webframework.annotations.WebframeworkController;
import dev.ceccon.webframework.annotations.WebframeworkGetMethod;
import dev.ceccon.webframework.annotations.WebframeworkPostMethod;

@WebframeworkController
public class HelloController {

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
    public String cadastraProduto(@WebframeworkBody Produto produtoNovo) {
        System.out.println(produtoNovo);
        return "Produto cadastrado";
    }

    @WebframeworkGetMethod("/teste")
    public String teste() {
        return "Teste!";
    }

    @WebframeworkGetMethod("/teste2")
    private String teste2() {
        return "Teste2!";
    }
}
