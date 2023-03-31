/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package svok.dp;

import svok.dp.example.TimeWindowVRP;
import svok.dp.example.ResourcesVRP;
import svok.dp.example.PickupDeliveryVRP;
import svok.dp.example.DropNodeVRP;
import svok.dp.example.CapacityVRP;
import svok.dp.example.Vrp;

/**
 *
 * @author Petr
 */
public class Dp {

    public static void main(String[] args) {

//        Dp.printRunInfo(Kuk.class.getName());
//        Kuk kuk=new Kuk();
//        kuk.kukám();
//
//        Dp.printRunInfo(BasicExample.class.getName());
//        BasicExample basic = new BasicExample();
//        basic.run();

        Dp.printRunInfo(Vrp.class.getName());
        Vrp vrp = new Vrp();
        try {
            vrp.run();
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }

        Dp.printRunInfo(CapacityVRP.class.getName());
        CapacityVRP capacityVrp = new CapacityVRP();
        try {
            capacityVrp.run();
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }

        Dp.printRunInfo(PickupDeliveryVRP.class.getName());
        PickupDeliveryVRP PickupDeliveryVrp = new PickupDeliveryVRP();
        try {
            PickupDeliveryVrp.run();
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }        

        Dp.printRunInfo(TimeWindowVRP.class.getName());
        TimeWindowVRP timeWindowVRP = new TimeWindowVRP();
        try {
            timeWindowVRP.run();
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }                     

        Dp.printRunInfo(ResourcesVRP.class.getName());
        ResourcesVRP resourcesVRP = new ResourcesVRP();
        try {
            resourcesVRP.run();
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }     
        
        Dp.printRunInfo(DropNodeVRP.class.getName());
        DropNodeVRP dropNodeVRP = new DropNodeVRP();
        try {
            dropNodeVRP.run();
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }     
    }
    
    private static void printRunInfo(String classname) {
        String eol = System.getProperty("line.separator");
        System.out.println(eol + "=== run "+classname+" ===");
    }
}
