package content;

import arc.graphics.Color;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.blocks.environment.OreBlock;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.draw.DrawDefault;
import mindustry.world.draw.DrawFlame;
import mindustry.world.draw.DrawMulti;

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
            warmupSpeed=0.01f;
            updateEffect=Fx.explosion;
            updateEffectChance=0.1f;
            //生产动画：仿原版硅冶炼炉(Silicon Smelter)——主体贴图 + 随运行热度(warmup)闪烁的炉火，开炉时还会发光
            drawer = new DrawMulti(new DrawDefault(), new DrawFlame(Color.valueOf("ffb35c")){{
                //火焰调小：缩小脉动火焰圆与其抖动幅度，并略微降低发光强度
                flameRadius = 1.7f;
                flameRadiusIn = 1f;
                flameRadiusMag = 1.1f;
                flameRadiusInMag = 0.5f;
                lightAlpha = 0.45f;
                lightRadius = 48f;
            }});

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
