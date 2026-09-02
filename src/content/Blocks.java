package content;

import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.blocks.environment.OreBlock;
import mindustry.world.blocks.production.GenericCrafter;

public class Blocks {
    //下品灵石矿：机械钻头（tier 2）即可开采
    public static Block oreLowestLingShi;
    //炼器材料处理
    public static Block JingLianChang;
    public static void load(){
        JingLianChang=new GenericCrafter("JingLianChang"){{
            requirements(Category.crafting, ItemStack.with(Items.copper,50,Items.lead,50,ModItem.Lowest_LingShi,10));
            alwaysUnlocked=false;
            craftEffect= Fx.pulverizeMedium;
            outputItem=new ItemStack(ModItem.ChiTong,1);
            consumeItem(Items.copper,2);
            size=2;
            craftTime=50f;
            hasItems=true;
            warmupSpeed=0.03f;
            updateEffect=Fx.smeltsmoke;
            updateEffectChance=1f;

        }};

        oreLowestLingShi = new OreBlock("ore-Lowest_LingShi"){{
            //mod 物品的 name 已带 mod 前缀，必须用 OreBlock(String) 构造器并手动指定掉落物，
            //否则会生成 "xian-time-ore-xian-time-Lowest_LingShi" 这种双重前缀名字，导致贴图找不到
            itemDrop = ModItem.Lowest_LingShi;
            //参与默认地图生成（自建游戏、地图编辑器），机械钻头即可采集
            oreDefault = true;
            oreThreshold = 0.81f;
            oreScale = 23.5f;

        }};
    }
}
