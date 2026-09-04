package content;

import arc.math.Angles;
import arc.math.Mathf;
import mindustry.entities.Lightning;
import mindustry.entities.bullet.BulletType;
import mindustry.graphics.Pal;
import mindustry.world.blocks.defense.turrets.ItemTurret;

/** 法术炮台：每次开火时，炮口会迸出几道小闪电（纯视觉+少量伤害） */
public class SpellcasterTurret extends ItemTurret{

    /** 每次射击迸出的小闪电数量 */
    public int lightningBolts = 5;
    /** 每道闪电的伤害 */
    public float lightningDamage = 8f;
    /** 闪电长度（越小越短小） */
    public int lightningLength = 6;
    /** 闪电相对射击方向的散开角度（度） */
    public float lightningSpread = 35f;

    public SpellcasterTurret(String name){
        super(name);
    }

    public class SpellcasterTurretBuild extends ItemTurretBuild{

        @Override
        protected void shoot(BulletType type){
            super.shoot(type);

            //炮口世界坐标（与官方 bullet() 的炮口计算一致）
            float mX = x + Angles.trnsx(rotation - 90, shootX, shootY);
            float mY = y + Angles.trnsy(rotation - 90, shootX, shootY);

            SpellcasterTurret t = (SpellcasterTurret)block;

            //朝射击方向散开迸出几道小闪电
            for(int i = 0; i < t.lightningBolts; i++){
                Lightning.create(team, Pal.lancerLaser, t.lightningDamage, mX, mY, rotation + Mathf.range(t.lightningSpread), t.lightningLength);
            }
        }
    }
}
