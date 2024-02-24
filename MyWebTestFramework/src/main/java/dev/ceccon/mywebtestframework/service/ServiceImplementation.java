package dev.ceccon.mywebtestframework.service;

import dev.ceccon.webframework.annotations.WebframeworkService;

@WebframeworkService
public class ServiceImplementation implements IService {

    @Override
    public String chamadaCustom(String mensagem) {
        return "Teste chamada servico: " + mensagem;
    }

}
