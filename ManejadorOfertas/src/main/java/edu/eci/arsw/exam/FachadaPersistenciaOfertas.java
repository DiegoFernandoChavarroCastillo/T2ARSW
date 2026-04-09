/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.exam;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 *
 * @author hcadavid
 */
public class FachadaPersistenciaOfertas {

    //mapa <codigo,producto>
    final private Map<String, Product> mapaProductosSolicitados = new ConcurrentHashMap<>();

    //mapa <codigo,codigo del cliente con la mejor oferta>
    final private Map<String, String> mapaOferentesAsignados = new ConcurrentHashMap<>();

    //mapa <codigo, monto de la mejor oferta>
    final private Map<String, Integer> mapaMontosAsignados = new ConcurrentHashMap<>();

    //mapa <codigo, numero de ofertas recibidas>
    final private Map<String, Integer> mapaOfertasRecibidas = new ConcurrentHashMap<>();

    public Map<String, Product> getMapaProductosSolicitados() {
        return mapaProductosSolicitados;
    }

    public Map<String, Integer> getMapaOfertasRecibidas() {
        return mapaOfertasRecibidas;
    }

    public Map<String, String> getMapaOferentesAsignados() {
        return mapaOferentesAsignados;
    }

    public Map<String, Integer> getMapaMontosAsignados() {
        return mapaMontosAsignados;
    }

}
