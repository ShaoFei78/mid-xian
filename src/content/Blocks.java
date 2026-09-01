package content;

import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.blocks.production.GenericCrafter;

public class Blocks {
    //炼器材料处理
    public static Block JingLianChang;
    public static void load(){
        JingLianChang=new GenericCrafter("JingLianChang"){{
            requirements(Category.crafting, ItemStack.with(Items.copper,50,Items.lead,50,ModItem.Lowest_LingShi,10));
            alwaysUnlocked=true;
            craftEffect= Fx.pulverizeMedium;
            outputItem=new ItemStack(ModItem.ChiTong,1);
            consumeItem(Items.copper,2);
            size=2;
            craftTime=50f;
            hasItems=true;

        }};
    }
}
