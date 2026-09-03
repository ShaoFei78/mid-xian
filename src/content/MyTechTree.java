package content;

import mindustry.content.Items;
import mindustry.content.SectorPresets;
import mindustry.content.TechTree;
import mindustry.game.Objectives;
import mindustry.type.ItemStack;

public class MyTechTree {

    public static void load(){

        //1. 下品灵石：与铜并列（父节点 = 铜的父节点 = 核心根节点）
        //   挖矿产出该物品时游戏会自动解锁；研究费用为 0
        TechTree.TechNode lingShi = new TechTree.TechNode(
            Items.copper.techNode.parent,
            ModItem.Lowest_LingShi,
            ItemStack.empty
        );
        lingShi.objectives.add(new Objectives.Produce(ModItem.Lowest_LingShi));

        //2. 精炼厂：与石墨压缩机并列（父节点 = 石墨压缩机的父节点 = 机械钻头）
        //   构造函数会自动为建造材料（铜、铅、下品灵石）附加研究目标，
        //   灵石解锁后即可免费研究
        new TechTree.TechNode(
            mindustry.content.Blocks.graphitePress.techNode.parent,
            Blocks.JingLianChang,
            ItemStack.empty
        );

        //3. 赤铜：与石墨并列（父节点 = 石墨的父节点 = 煤）
        //   额外前置目标：精炼厂解锁后，该节点即可免费研究
        TechTree.TechNode chiTong = new TechTree.TechNode(
            Items.graphite.techNode.parent,
            ModItem.ChiTong,
            ItemStack.empty
        );
        chiTong.objectives.add(new Objectives.Research(Blocks.JingLianChang));

        //4. 赤铜双管炮：位于双管炮之后（父节点 = 双管炮）
        //   构造函数会自动为建造材料（赤铜、下品灵石）附加研究目标，
        //   赤铜解锁后即可免费研究
        new TechTree.TechNode(
            mindustry.content.Blocks.duo.techNode,
            Turrets.ChiTong_Duo,
            ItemStack.empty
        );

        //5. 仙遗地区(XianYi)：位于零号地区(groundZero)之后（父节点 = 零号地区）
        //   目标：占领零号地区后自动解锁（与原版 frozenForest 紧随 groundZero 的机制一致）
        TechTree.TechNode xianYi = new TechTree.TechNode(
            SectorPresets.groundZero.techNode,
            ExSector.XianYi,
            ItemStack.empty
        );
        xianYi.objectives.add(new Objectives.SectorComplete(SectorPresets.groundZero));

        //6. 灵石燃烧发电机 BurnLingShi：与涡轮发电机(蒸汽发电机, steamGenerator)并列
        //   父节点 = 蒸汽发电机的父节点（燃烧发电机）；占领仙遗(XianYi)后可用，研究需消耗灵石/赤铜/钛
        TechTree.TechNode burnLingShi = new TechTree.TechNode(
            mindustry.content.Blocks.steamGenerator.techNode.parent,
            Blocks.BurnLingShi,
            ItemStack.with(ModItem.Lowest_LingShi, 500, ModItem.ChiTong, 1000, Items.titanium, 1000)
        );
        burnLingShi.objectives.add(new Objectives.SectorComplete(ExSector.XianYi));
    }
}
