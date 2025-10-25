package cybercat5555.faunus.core.entity.ai.goals;

import cybercat5555.faunus.core.entity.livingEntity.CapuchinEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;

import java.util.EnumSet;

public class CapuchinBringItemGoal extends Goal {
    private final CapuchinEntity target;

    public CapuchinBringItemGoal(CapuchinEntity capuchinEntity) {
        this.target = capuchinEntity;
        this.setControls(EnumSet.of(Control.MOVE, Control.JUMP, Control.TARGET, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        return !this.target.getMainHandStack().isEmpty() && target.getOwner() != null && !this.target.isSitting();
    }

    public void start() {
        var owner = this.target.getOwner();

        this.target.getNavigation().startMovingTo(owner, 1);
    }

    public void tick() {
        this.target.getWorld().getServer().getPlayerManager().getPlayerList().forEach(p-> p.sendMessage(Text.literal("Tick bring")));
        if (this.target.distanceTo(this.target.getOwner()) <= 2) {
            var stack = this.target.getMainHandStack();
            this.target.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
            target.getWorld().spawnEntity(new ItemEntity(target.getWorld(), target.getOwner().getX(), target.getOwner().getY(), target.getOwner().getZ(), stack));
        } else if (this.target.getNavigation().getCurrentPath() == null || this.target.getNavigation().getCurrentPath().isFinished()) {
            if(target.getOwner()!=null){
                this.target.getNavigation().startMovingTo(this.target.getOwner(),1);
            }
        }
    }

    public boolean shouldContinue() {
        return !this.target.getMainHandStack().isEmpty() && !this.target.isSitting();
    }

    @Override
    public boolean canStop() {
        return super.canStop();
    }

    private int sortItemsByDistance(ItemEntity itemEntity, ItemEntity itemEntity1) {
        return this.target.getPos().distanceTo(itemEntity.getPos()) > this.target.getPos().distanceTo(itemEntity1.getPos()) ? 1 : 0;
    }
}
