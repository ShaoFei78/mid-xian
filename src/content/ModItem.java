package content;

import arc.graphics.Color;
import mindustry.type.Item;

public class ModItem {
    //各种灵石
    public static Item
            Lowest_LingShi,Secondary_LingShi,Highest_LingShi,ChiTong;
    public static void load(){
        Lowest_LingShi = new Item("Lowest_LingShi", Color.lightGray){{
            hardness=1;
            cost=0.5f;
            alwaysUnlocked=false;
        }};
        Secondary_LingShi = new Item("Secondary_LingShi", Color.blue){{
            hardness=2;
            cost=1f;
            alwaysUnlocked=false;
        }};
        Highest_LingShi = new Item("Highest_LingShi", Color.red){{
            hardness=3;
            cost=1.5f;
            alwaysUnlocked=false;
        }};
        //炼器材料
        ChiTong = new Item("ChiTong", Color.red){{
            hardness=1;
            cost=0.5f;
            alwaysUnlocked=false;
        }};

    }

}
