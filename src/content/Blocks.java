package content;

import arc.graphics.Color;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.blocks.environment.OreBlock;
import mindustry.world.blocks.power.ConsumeGenerator;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.draw.DrawDefault;
import mindustry.world.draw.DrawFlame;
import mindustry.world.draw.DrawMulti;
import mindustry.world.draw.DrawPlasma;
import mindustry.world.draw.DrawRegion;

public class Blocks {
    //下品灵石矿：机械钻头（tier 2）即可开采
    public static Block oreLowestLingShi;
    //炼器材料处理
    public static Block JingLianChang;
    //灵石燃烧发电机：燃烧下品灵石发电（等离子体动画，参照原版冲击反应堆的画法）
    public static Block BurnLingShi;
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

        BurnLingShi = new ConsumeGenerator("BurnLingShi"){{
            requirements(Category.power, ItemStack.with(
                ModItem.ChiTong, 30,
                ModItem.Lowest_LingShi, 20,
                Items.titanium, 30,
                Items.silicon, 30
            ));
            //powerProduction 单位是“每 tick”，界面显示 ×60：7 × 60 = 420 电/秒
            powerProduction = 7f;
            //每 60 tick（1 秒）消耗 1 个下品灵石
            itemDuration = 60f;
            size = 2;
            hasItems = true;
            generateEffect = Fx.generatespark;

            //只烧下品灵石（普通 consumeItem，无需物品 flammability）
            consumeItem(ModItem.Lowest_LingShi, 1);

            //等离子体动画：底座 → 主体 → 顶部 → 等离子辉光（叠加发光在最上层）
            drawer = new DrawMulti(
                new DrawRegion("-bottom"),
                new DrawPlasma(),
                new DrawDefault(),
                new DrawRegion("-top")

            );
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
