package edu.eci.arsw.exam.events;

import edu.eci.arsw.exam.IdentityGenerator;
import edu.eci.arsw.exam.Product;
import edu.eci.arsw.exam.remote.ManejadorOfertasStub;
import java.util.Random;
import java.io.*;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

public class OffertMessageListener implements MessageListener {

    Random rand = new Random(System.currentTimeMillis());
    
    private ManejadorOfertasStub stub;

    public void setStub(ManejadorOfertasStub stub) {
        this.stub = stub;
    }

    public OffertMessageListener() {
        super();
        System.out.println("Comprador #"+IdentityGenerator.actualIdentity+" esperando eventos...");
    }

    @Override
    public void onMessage(Message message) {
        try {
            String routingKey = message.getMessageProperties().getReceivedRoutingKey();
            
            if (routingKey.startsWith("my.")) {
                Product receivedProduct = new Product(message.getBody());
                System.out.println("Comprador #"+IdentityGenerator.actualIdentity+" recibió: "+receivedProduct.getCode());
                
                // Task 3a: Realizar oferta >= startPrice
                int startPrice = receivedProduct.setStartPrice(); // Guessing this is the getter despite the name
                // Actually let's check Product.java again. 
                // Line 44: public int setStartPrice() { return startPrice; }
                // That's a getter!
                
                int montoOferta = startPrice + rand.nextInt(100000); // Random bid above startPrice

                System.out.println("Comprador #"+IdentityGenerator.actualIdentity+" ofertando "+montoOferta+" por "+receivedProduct.getCode());
                stub.agregarOferta(IdentityGenerator.actualIdentity, receivedProduct.getCode(), montoOferta);
                
            } else if (routingKey.startsWith("winner.")) {
                String payload = new String(message.getBody());
                // Format: "BUYER_ID:PRODUCT_CODE"
                String[] parts = payload.split(":");
                if (parts.length == 2) {
                    String winnerId = parts[0];
                    String productCode = parts[1];
                    
                    if (winnerId.equals(IdentityGenerator.actualIdentity)) {
                        // Task 3c: Imprimir mensaje de ganador
                        System.out.println("El comprador "+winnerId+" compro el producto "+productCode);
                    }
                }
            }
            
        } catch (Exception e) {
            throw new RuntimeException("An exception occured while trying to get a AMQP object:" + e.getMessage(), e);
        }

    }

}
