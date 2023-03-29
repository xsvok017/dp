/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package svok.dp;

import svok.dp.Kuk;
import svok.dp.BasicExample;
import svok.dp.Vrp;
import svok.dp.CapacityVRP;
import svok.dp.PckupDeliveryVRP;

/**
 *
 * @author Petr
 */
public class Dp {

    public static void main(String[] args) {
        System.out.println("=== run "+Kuk.class.getName()+" ===");
        Kuk kuk=new Kuk();
        kuk.kukám();

        System.out.println("=== run "+BasicExample.class.getName()+" ===");
        BasicExample basic = new BasicExample();
        basic.run();

        System.out.println("=== run "+Vrp.class.getName()+" ===");
        Vrp vrp = new Vrp();
        try {
            vrp.run();
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("=== run "+CapacityVRP.class.getName()+" ===");
        CapacityVRP capacityVrp = new CapacityVRP();
        try {
            capacityVrp.run();
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("=== run "+PckupDeliveryVRP.class.getName()+" ===");
        PckupDeliveryVRP PickupDeliveryVrp = new PckupDeliveryVRP();
        try {
            PickupDeliveryVrp.run();
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }        
    }
}
