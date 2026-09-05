package content;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.math.Angles;
import arc.math.Mathf;
import arc.util.Time;
import mindustry.content.Fx;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.pattern.ShootPattern;
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
        BasicBulletType slashBullet = new BasicBulletType(3.5f, 16f){{
            //美术区域名必须带 mod 前缀(xian-time-)，与贴图打包后的名字一致
            sprite = "xian-time-lunar-slash-bullet";          //月牙主体贴图
            backSprite = "xian-time-lunar-slash-bullet-back"; //月牙底层(营造厚度感)

            width = 18f;   //月牙宽度：扁而宽
            height = 8f;   //月牙高度：扁平

            lifetime = 6f;         //飞行寿命(tick)
            rangeOverride = 18.2f; //攻击范围 18.2（覆盖自动计算，单位会走到该距离内挥砍）

            pierce = true;   //开启穿透：一道剑气可同时命中多个敌人

            hitSize = 6f;    //命中判定体积
            drag = 0f;       //无空气阻力，保持较快飞行速度

            //剑气配色：白青色调 + 较短拖尾
            frontColor = Color.valueOf("eaf6ff");
            backColor  = Color.valueOf("9fd8ff");
            trailColor = Color.valueOf("bfe9ff");
            trailLength = 12;   //较短拖尾
            trailWidth = 2.6f;  //拖尾宽度

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

            reload = 26f;   //装填 26 tick → 每次挥出 2 道 × 约 2.3 次/秒 ≈ 2×2.3 发/秒
            mirror = false; //单手单剑，不生成镜像武器
            rotate = true;  //允许旋转瞄准
            rotateSpeed = 8f;

            //武器(剑)挂在单位中心，剑尖方向由 -weapon 贴图决定
            x = 0f;
            y = 0f;
            shootX = 0f;   //剑气横向偏移
            shootY = 9f;   //剑气从剑尖(身前9格? 实际9单位≈1格出头)处产生

            recoil = 1f;   //挥砍后坐(视觉)
            recoilPow = 1.5f;

            shootSound = Sounds.shoot; //换任意音效即可
            ejectEffect = Fx.none;

            //每次挥砍出 2 道月牙，略微扇形散开
            shoot = new ShootPattern(){
                @Override
                public void shoot(int totalShots, ShootPattern.BulletHandler handler, Runnable barrelIncrementer){
                    handler.shoot(0f, 0f, -3.5f, 0f);
                    handler.shoot(0f, 0f, 3.5f, 0f);
                }
            };
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
            float off;
            if(p < swingFrac){
                //挥出阶段：0 → swingAngle
                off = swingAngle * (p / swingFrac);
            }else{
                //归位阶段：swingAngle → 0
                off = swingAngle * (1f - (p - swingFrac) / (1f - swingFrac));
            }

            Draw.rect(region, wx, wy, aimRot + off);
        }
    }
}
