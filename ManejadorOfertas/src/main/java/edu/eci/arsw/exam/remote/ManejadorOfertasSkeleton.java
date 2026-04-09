/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.exam.remote;

import edu.eci.arsw.exam.FachadaPersistenciaOfertas;
import edu.eci.arsw.exam.MainFrame;
import edu.eci.arsw.exam.events.OffertMessageProducer;

/**
 *
 * @author hcadavid
 */
public class ManejadorOfertasSkeleton implements ManejadorOfertasStub{

    private FachadaPersistenciaOfertas fpers=null;
    
    private MainFrame mainFrame;
    
    private OffertMessageProducer messageProducer;

    public void setFachadaPersistenciaOfertas(FachadaPersistenciaOfertas fpers) {
        this.fpers = fpers;
    }

    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public void setMessageProducer(OffertMessageProducer messageProducer) {
        this.messageProducer = messageProducer;
    }
            
    @Override
    public void agregarOferta(String codOferente,String codprod,int monto) {
        // Task 5: Prevenir inconsistencias usando sincronización fina (sobre el código del producto)
        // Usamos intern() para obtener el mismo objeto String para el mismo código y así sincronizar correctamente.
        synchronized(codprod.intern()) {
            if (!fpers.getMapaOfertasRecibidas().containsKey(codprod)){
                //se ha recibido la primera oferta 
                fpers.getMapaOfertasRecibidas().put(codprod, 1);
                //se asigna el monto propuesto como mejor oferta
                fpers.getMapaMontosAsignados().put(codprod, monto);
                //se asigna al oferente como ganador provisional
                fpers.getMapaOferentesAsignados().put(codprod, codOferente);
            }
            else{
                int ofertasActuales=fpers.getMapaOfertasRecibidas().get(codprod);
                
                // Si ya se recibieron 3 o más ofertas, ignorar (o manejar según se prefiera, 
                // pero el requerimiento dice "una vez se hayan recibido las tres primeras")
                if (ofertasActuales >= 3) return;

                fpers.getMapaOfertasRecibidas().put(codprod,ofertasActuales+1);
                
                // Corrección: En una subasta, el monto MAYOR gana.
                if (monto > fpers.getMapaMontosAsignados().get(codprod)){
                    fpers.getMapaMontosAsignados().put(codprod, monto);
                    fpers.getMapaOferentesAsignados().put(codprod, codOferente);
                }

                // Task 3b: Mostrar ganador tras la tercera propuesta
                if (fpers.getMapaOfertasRecibidas().get(codprod) == 3) {
                    String ganador = fpers.getMapaOferentesAsignados().get(codprod);
                    int montoGanador = fpers.getMapaMontosAsignados().get(codprod);
                    String msg = "GANADOR para " + codprod + ": " + ganador + " con " + montoGanador;
                    
                    // Mostrar en la ventana
                    mainFrame.addStatus(msg);
                    
                    // Task 3c: Informar al comprador ganador vía RabbitMQ
                    messageProducer.notifyWinner(ganador, codprod);
                }
            }
        }
    }
    
    
    
}
