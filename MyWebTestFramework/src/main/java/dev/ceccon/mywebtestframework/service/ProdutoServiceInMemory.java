package dev.ceccon.mywebtestframework.service;

import dev.ceccon.mywebtestframework.model.Produto;
import dev.ceccon.webframework.annotations.WebframeworkService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebframeworkService
public class ProdutoServiceInMemory implements ProdutoService {

    Map<Integer, Produto> produtos = new HashMap<>();
    Integer proximoId = 1;

    public ProdutoServiceInMemory() {
        inicializaDadosDeTeste();
    }

    private void inicializaDadosDeTeste() {
        produtos.put(1, new Produto(1, "Nome1", 1000.0, "teste1.jpg"));
        produtos.put(2, new Produto(2, "Nome2", 2000.0, "teste2.jpg"));
        produtos.put(3, new Produto(3, "Nome3", 3000.0, "teste3.jpg"));
        produtos.put(4, new Produto(4, "Nome4", 4000.0, "teste4.jpg"));
        produtos.put(5, new Produto(5, "Nome5", 5000.0, "teste5.jpg"));
        produtos.put(6, new Produto(6, "Nome6", 6000.0, "teste6.jpg"));
        produtos.put(7, new Produto(7, "Nome7", 7000.0, "teste7.jpg"));
        proximoId = 8;
    }

    @Override
    public Produto criar(Produto novo) {
        novo.setId(proximoId);
        proximoId += 1;

        produtos.put(novo.getId(), novo);
        return novo;
    }

    @Override
    public List<Produto> listar() {
        return new ArrayList<>(produtos.values());
    }

    @Override
    public Produto atualizar(Integer id, Produto atualizado) {
        atualizado.setId(id);
        produtos.put(id, atualizado);

        return atualizado;
    }

    @Override
    public void deletar(Integer id) {
        produtos.remove(id);
    }
}
