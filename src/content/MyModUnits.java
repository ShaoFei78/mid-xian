package content;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.math.Angles;
import arc.math.Mathf;
import arc.util.Time;
import mindustry.content.Fx;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.units.WeaponMount;
import mindustry.gen.MechUnit;
import mindustry.gen.Sounds;
import mindustry.gen.Unit;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.type.Weapon;

/**
 * T1 近战剑士单位：剑修(JianXiu)
 * 特色：武器（剑）绕挂点做“挥砍”动作——挥出→末端迸出两道月牙剑气→归位，循环即一次发射。
 * 需要美术资源（均已放在 assets/sprites 对应目录）：
 *   unit:  sprites/unit/ground-unit/JianXiu/JianXiu[-base,-leg,-cell,-weapon].png
 *   bullet:sprites/bullet/lunar-slash-bullet(-back).png
 */
public class MyModUnits{

    public static UnitType JianXiu;

    public static void load(){

        //============================================================
        // 1. 月牙剑气子弹：BasicBulletType，扁平月牙 + 穿透 + 短拖尾
        //============================================================
        BasicBulletType slashBullet = new BasicBulletType(4f, 16f){{
            //美术区域名必须带 mod 前缀(xian-time-)，与贴图打包后的名字一致
            sprite = "xian-time-lunar-slash-bullet";          //月牙主体贴图
            backSprite = "xian-time-lunar-slash-bullet-back"; //月牙底层(营造厚度感)

            width = 18f;   //月牙宽度：扁而宽
            height = 8f;   //月牙高度：扁平

            lifetime = 21f;         //飞行寿命(tick)：足够月牙飞满 82 距离
            rangeOverride = 82.2f;  //攻击范围：18.2 + 8格(64单位) ≈ 82.2

            //穿透：建筑+单位都挡不住，且每个目标只吃一次伤害（官方 BulletComp 用 collided 集合防重复）
            pierce = true;          //穿透单位
            pierceBuilding = true;  //穿透建筑（默认只穿单位，建筑会挡下子弹）
            pierceCap = -1;         //-1 = 不限穿透次数
            collidesTiles = true;   //开启建筑碰撞判定（命中结算一次后继续飞行）

            hitSize = 6f;    //命中判定体积
            drag = 0f;       //无空气阻力，保持较快飞行速度

            //剑气配色：白青色调 + 较短拖尾
            frontColor = Color.valueOf("eaf6ff");
            backColor  = Color.valueOf("9fd8ff");
            trailColor = Color.valueOf("bfe9ff");
            trailLength = 12;   //较短拖尾
            trailWidth = 2.6f;  //拖尾宽度

            //发光：与拖尾同色的光晕（代码实现，无需改贴图）
            lightColor = Color.valueOf("bfe9ff"); //与拖尾同色
            lightRadius = 26f;   //光晕半径(世界单位)，0或负则关闭
            lightOpacity = 0.55f;//光晕强度

            //把月牙贴图混白提亮，让它像拖尾一样发亮（不改贴图也能实现）
            mixColorFrom = Color.valueOf("e8fbff").a(0.55f);
            mixColorTo = Color.valueOf("e8fbff").a(0.55f);

            //不需要复杂命中特效
            hitEffect = Fx.none;
            despawnEffect = Fx.none;
            shootEffect = Fx.none;
            smokeEffect = Fx.none;
            hitSound = Sounds.none;
            despawnSound = Sounds.none;
        }};

        //============================================================
        // 2. 挥剑武器：绕挂点旋转挥砍，动作末端才发射，随后归位
        //============================================================
        Weapon sword = new SwordWeapon("xian-time-JianXiu-weapon"){{
            bullet = slashBullet;

            reload = 13f;   //双持：官方生成镜像时会把每把剑装填翻倍(13→26)，左右交替 ≈ 2×2.3 发/秒
            mirror = true;  //双持：官方自动生成第二把(翻转)武器，左右各一把
            alternate = true; //左右交替挥砍
            rotate = true;  //允许旋转瞄准
            rotateSpeed = 8f;

            //武器(剑)挂在身体右侧，镜像副本自动放到左侧
            x = 4f;
            y = 0f;
            shootX = 0f;   //剑气横向偏移
            shootY = 9f;   //剑气从剑尖(身前9格? 实际9单位≈1格出头)处产生

            recoil = 1f;   //挥砍后坐(视觉)
            recoilPow = 1.5f;

            shootSound = Sounds.shoot; //换任意音效即可
            ejectEffect = Fx.none;
        }};

        //============================================================
        // 3. 单位本体：T1 地面机甲（走腿动画）
        //============================================================
        JianXiu = new UnitType("JianXiu"){{
            constructor = MechUnit::create; //关键：地面机甲实体，才能走路并播放 -leg 腿部动画

            health = 300f;  //基础生命
            speed = 0.8f;   //移动速度
            hitSize = 7f;   //碰撞体积

            //该单位的“生产消耗”（供参考）：
            //  20 下品灵石 + 20 赤铜 —— 在产兵建筑(UnitFactory/Reconstructor)的 plan 中填写，例如：
            //  factory.plans.add(new UnitFactory.UnitPlan(JianXiu, 60f, ItemStack.with(ModItem.Lowest_LingShi,20, ModItem.ChiTong,20)));

            weapons.add(sword); //装上挥剑武器
        }};
    }

    /**
     * 挥剑武器：发射逻辑 = 挥出(swingFrac×reload tick)→末端发射→归位
     * 通过覆写 shoot() 把“真正开火”延后到挥剑末端，通过覆写 draw() 让剑身按装填相位摆动。
     */
    public static class SwordWeapon extends Weapon{

        /** 一次挥砍总共转过的角度（度） */
        public float swingAngle = 75f;
        /** 挥出阶段占整个装填周期的比例（剩余时间为归位阶段） */
        public float swingFrac = 0.55f;

        public SwordWeapon(String name){
            super(name);
        }

        /** 挥砍到末端时才真正开火（延迟发射，仍走原版音效/弹道逻辑） */
        @Override
        protected void shoot(Unit unit, WeaponMount mount, float shootX, float shootY, float rotation){
            float delay = reload * swingFrac; //挥出所需 tick
            Time.run(delay, () -> {
                if(unit.isAdded()){
                    SwordWeapon.super.shoot(unit, mount, shootX, shootY, rotation);
                }
            });
        }

        /** 绘制剑身：随装填相位先挥出、末端达 swingAngle、再归位 */
        @Override
        public void draw(Unit unit, WeaponMount mount){
            float
            rotation = unit.rotation - 90, //单位朝向（标准角转绘制角）
            realRecoil = Mathf.pow(mount.recoil, recoilPow) * recoil,
            //瞄准旋转（rotate=true 时 mount.rotation 由原版 update 负责追踪目标）
            aimRot = unit.rotation - 90 + (rotate ? mount.rotation : baseRotation),
            wx = unit.x + Angles.trnsx(rotation, x, y) + Angles.trnsx(aimRot, 0, -realRecoil),
            wy = unit.y + Angles.trnsy(rotation, x, y) + Angles.trnsy(aimRot, 0, -realRecoil);

            //装填相位：0=刚挥出开始，1=即将下一次挥砍（相位=1 时偏移归零）
            float p = Mathf.clamp(1f - mount.reload / reload);
            float curve;
            if(p < swingFrac){
                //挥出阶段：0 → 1
                curve = p / swingFrac;
            }else{
                //归位阶段：1 → 0
                curve = 1f - (p - swingFrac) / (1f - swingFrac);
            }

            //取负号：右侧(原版侧)顺时针挥砍；左侧镜像副本经 xscl 翻转后自动呈逆时针
            float swing = -swingAngle * curve;

            float prev = Draw.xscl;
            //与原版 Weapon.draw 一致：镜像侧把剑身贴图水平翻转(剑刃朝向一致)
            Draw.xscl *= -Mathf.sign(flipSprite);
            Draw.rect(region, wx, wy, aimRot + swing);
            Draw.xscl = prev;
        }
    }
}
