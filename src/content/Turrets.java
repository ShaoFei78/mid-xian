package content;

import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.part.DrawPart;
import mindustry.entities.part.RegionPart;
import mindustry.entities.pattern.ShootAlternate;
import mindustry.gen.Sounds;
import mindustry.graphics.Pal;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.blocks.defense.turrets.ItemTurret;
import mindustry.world.draw.DrawTurret;

public class Turrets {

    //赤铜双管炮：以原版双管炮(Duo)为原型，伤害与弹容量翻倍
    public static Block ChiTong_Duo;

    public static void load(){
        ChiTong_Duo = new ItemTurret("ChiTong_Duo"){{
            requirements(Category.turret, ItemStack.with(ModItem.ChiTong, 35, ModItem.Lowest_LingShi, 5));
            alwaysUnlocked=false;

            //弹药类型与原版双管炮相同：铜、石墨、硅；伤害均为原版的2倍
            ammo(
                Items.copper, new BasicBulletType(2.5f, 18f){{
                    width = 7f;
                    height = 9f;
                    lifetime = 60f;
                    ammoMultiplier = 2f;
                    despawnEffect = Fx.hitBulletColor;
                    hitEffect = Fx.hitBulletColor;
                    trailColor = Pal.copperAmmoBack;
                    backColor = Pal.copperAmmoBack;
                    hitColor = Pal.copperAmmoBack;
                    frontColor = Pal.copperAmmoFront;
                }},
                Items.graphite, new BasicBulletType(3.5f, 36f){{
                    width = 9f;
                    height = 12f;
                    ammoMultiplier = 4f;
                    lifetime = 60f;
                    reloadMultiplier = 0.8f;
                    rangeChange = 16f;
                    despawnEffect = Fx.hitBulletColor;
                    hitEffect = Fx.hitBulletColor;
                    trailColor = Pal.graphiteAmmoBack;
                    backColor = Pal.graphiteAmmoBack;
                    hitColor = Pal.graphiteAmmoBack;
                    frontColor = Pal.graphiteAmmoFront;
                }},
                Items.silicon, new BasicBulletType(3f, 24f){{
                    width = 7f;
                    height = 9f;
                    homingPower = 0.2f;
                    reloadMultiplier = 1.5f;
                    ammoMultiplier = 5f;
                    lifetime = 60f;
                    trailLength = 5;
                    trailWidth = 1.5f;
                    despawnEffect = Fx.hitBulletColor;
                    hitEffect = Fx.hitBulletColor;
                    trailColor = Pal.siliconAmmoBack;
                    backColor = Pal.siliconAmmoBack;
                    hitColor = Pal.siliconAmmoBack;
                    frontColor = Pal.siliconAmmoFront;
                }}
            );

            //双管齐射，其余数值沿用原版双管炮
            shoot = new ShootAlternate(3.5f);
            recoils = 2;
            maxAmmo = 60; //原版默认30，翻倍
            shootSound = Sounds.shootDuo;
            recoil = 0.5f;
            shootY = 3f;
            reload = 20f;
            range = 160f;
            shootCone = 15f;
            ammoUseEffect = Fx.casing1;
            health = 250;
            inaccuracy = 2f;
            rotateSpeed = 10f;
            coolant = consumeCoolant(0.1f);
            coolantMultiplier = 10f;
            researchCostMultiplier = 0.05f;
            depositCooldown = 2f;
            limitRange(5f);

            //双炮管外观：左右两根炮管零件，与原版双管炮一致
            drawer = new DrawTurret(){{
                for(int i = 0; i < 2; i ++){
                    int f = i;
                    parts.add(new RegionPart("-barrel-" + (i == 0 ? "l" : "r")){{
                        progress = PartProgress.recoil;
                        recoilIndex = f;
                        under = true;
                        moveY = -1.5f;
                    }});
                }
            }};
        }};
    }
}
