package content;

import arc.audio.Sound;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.entities.bullet.ArtilleryBulletType;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.part.RegionPart;
import mindustry.entities.pattern.ShootAlternate;
import mindustry.entities.pattern.ShootPattern;
import mindustry.gen.Sounds;
import mindustry.graphics.Pal;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.blocks.defense.turrets.ItemTurret;
import mindustry.world.draw.DrawDefault;
import mindustry.world.draw.DrawMulti;
import mindustry.world.draw.DrawPlasma;
import mindustry.world.draw.DrawTurret;

public class Turrets {

    //赤铜双管炮：以原版双管炮(Duo)为原型，伤害与弹容量翻倍
    public static Block ChiTong_Duo;
    public static Block MinSpellcaster;
    //自定义射击音效：官方做法，自动从 assets/sounds/ 目录加载 shootChiTong_Duo.ogg（.ogg/.mp3 自动识别，带缓存）
    public static Sound shootChiTong_Duo = Vars.tree.loadSound("shootChiTong_Duo");

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
            shootSound = shootChiTong_Duo; //使用自定义射击音效
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
        MinSpellcaster=new ItemTurret("MinSpellcaster"){{
            requirements(Category.turret,ItemStack.with(ModItem.ChiTong, 80, ModItem.Lowest_LingShi, 20,Items.titanium,50));
            alwaysUnlocked=false;

            ammo(
                    ModItem.ChiTong, new ArtilleryBulletType(8f,80f){{
                        width=10f;
                        height=10f;
                        collidesTiles = false;

                        splashDamage=50f;
                        splashDamageRadius = 50f * 0.75f;
                        ammoMultiplier = 4f;
                        // 4. 视觉与爆炸特效
                        hitEffect = Fx.hitFlamePlasma; // 使用榴弹专属的爆炸特效
                        despawnEffect = Fx.hitFlamePlasma; // 落地未命中也会爆炸
                        hitSound = Sounds.explosion; // 落地时的爆炸音效
                        backColor = Pal.lightOrange;
                        frontColor = Pal.lightishOrange;
                        trailColor = Pal.lightishOrange;
                    }}
            );
            shoot=new ShootPattern();
            size = 2;
            reload = 120f;
            range =250f;
            limitRange(5f);
            recoils = 2;
            recoil = 3f;
            maxAmmo = 60;
            ammoUseEffect = Fx.casing1;
            coolant = consumeCoolant(0.3f);
            coolantMultiplier = 10f;
            health = 400;
            rotateSpeed = 8f;
            shootY = 3f;
            shootSound = Sounds.shootRipple;
            drawer = new DrawMulti(

                    new DrawPlasma(),
                    new DrawDefault()

            );

        }};
    }
}
