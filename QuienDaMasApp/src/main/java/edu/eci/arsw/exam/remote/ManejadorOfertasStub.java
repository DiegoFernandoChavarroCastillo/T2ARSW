/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.exam.remote;

/**
 *
 * @author hcadavid
 */
public interface ManejadorOfertasStub {
    

    /**
     * Agregar una propuesta económica para el producto propuesta cuyo código
     * es 'codProducto'
     * @param codOferente código de quien realiza la oferta
     * @param codProducto
     * @param monto 
     */
    public void agregarOferta(String codOferente,String codProducto,int monto);
    
}
