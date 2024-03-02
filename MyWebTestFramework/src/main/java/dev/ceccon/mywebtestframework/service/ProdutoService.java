package dev.ceccon.mywebtestframework.service;

import dev.ceccon.mywebtestframework.model.Produto;

import java.util.List;

public interface ProdutoService {

    Produto criar(Produto novo);

    List<Produto> listar();

    Produto atualizar(Integer id, Produto atualizado);

    void deletar(Integer id);

}
