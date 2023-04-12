package com.github.shannieann.wyrmroost.entities.dragon.ai.goals;

import com.github.shannieann.wyrmroost.entities.dragon.WRDragonEntity;
import com.github.shannieann.wyrmroost.entities.dragon.ai.movement.swimming.WRSwimmingLookControl;
import com.github.shannieann.wyrmroost.entities.dragon.ai.movement.walking.WRGroundLookControl;
import net.minecraft.world.entity.ai.control.LookControl;

public class WRSleepGoal extends AnimatedGoal{

    private final WRDragonEntity entity;
    private boolean underwaterSleeping;
    public WRSleepGoal(WRDragonEntity entity) {
        super(entity);
        this.entity = entity;
    }

    @Override
    public boolean canUse() {
        //If it can swim, only sleep underwater
        if (entity.speciesCanSwim() && (!entity.isUnderWater() && !entity.isOnGround())) {
            return false;
        }
        //If it cannot swim, only sleep on ground
        if (!entity.speciesCanSwim() && !entity.isOnGround()) {
            return false;
        }
        //Sleep only if dragon did not recently sleep / woke up
        if (entity.sleepCooldown > 0) {
            return false;
        }
        //Sleep only at night
        if (entity.level.getDayTime() < 14000 || entity.level.getDayTime() > 23500) {
            return false;
        }
        //Sleep only if not doing any other activities...
        if (!isIdling())  {
            return false;
        }
        //If tamed, sleep only if at home and at reasonable health...
        //Or if set to sit down and all previous conditions are met...
        if (entity.isTame())
        {
            if (entity.isAtHome()) {
                if (entity.defendsHome()) {
                    return entity.getHealth() < entity.getMaxHealth() * 0.25;
                }
            }
            else if (!entity.isInSittingPose()) {
                return false;
            }
        }
        return true;
    }


    public boolean isIdling() {
        return entity.getNavigation().isDone() && entity.getTarget() == null && !entity.isVehicle() && (entity.speciesCanSwim() || !entity.isInWaterOrBubble()) && !entity.isUsingFlyingNavigator();
    }

    @Override
    public void start(){
        entity.setSleeping(true);
        if (entity.speciesCanSwim() && entity.isUnderWater()) {
            underwaterSleeping = true;
        }
        entity.clearAI();
        entity.setXRot(0);
    }

    @Override
    public void tick() {
        LookControl lookControl = entity.getLookControl();
        if (lookControl instanceof WRGroundLookControl) {
            ((WRGroundLookControl) lookControl).stopLooking();
        }
        if (lookControl instanceof WRSwimmingLookControl) {
            ((WRSwimmingLookControl) lookControl).stopLooking();
        }
        //ToDo: Flying look Control
        // Heal while sleeping
        if (entity.getHealth() < entity.getMaxHealth() && entity.getRandom().nextDouble() < 0.005) {
            entity.heal(1);
        }
        super.start(underwaterSleeping ? "sleep_water" : "sleep", 1, 20);
    }



    @Override
    public boolean canContinueToUse(){
        //If daytime, wake up
        if (entity.level.getDayTime() < 14000 || entity.level.getDayTime() > 23500) {
            return false;
        }
        //If it's a water sleeping entity, and it somehow gets out of water, wake up
        if (underwaterSleeping && !entity.isUnderWater()) {
            return false;
        }
        //Allows us to check for other methods, elsewhere, that might have set the DataParameter to false
        //For instance, the hurt method...
        if (!entity.isSleeping()){
            return false;
        }
        return true;
    }

    @Override
    public void stop(){
        entity.setSleeping(false);
        underwaterSleeping = false;
        entity.sleepCooldown = 350;
        super.stop();
    }
}
