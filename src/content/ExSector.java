package content;

import mindustry.content.Planets;
import mindustry.type.Planet;
import mindustry.type.Sector;
import mindustry.type.SectorPreset;

public class ExSector {
    public static SectorPreset XianYi;
    public static void load(){
        XianYi=new SectorPreset("XianYi", Planets.serpulo,175){{
            //必须为 false：由科技树控制“占领零号地区(groundZero)后解锁”；若为 true 则开局即可降落
            this.alwaysUnlocked=false;
            this.addStartingItems=true;
            this.captureWave=15;
            this.difficulty=1.5F;
            this.overrideLaunchDefaults=true;
            this.noLighting=true;
            this.startWaveTimeMultiplier=3F;
        }};

    }
}
