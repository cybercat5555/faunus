package cybercat5555.faunus.core.entity.ai.goals;

import cybercat5555.faunus.core.entity.livingEntity.CapuchinEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;

import java.util.EnumSet;

public class CapuchinCollectItemGoal extends Goal {
    private final CapuchinEntity target;
    private ItemEntity targetItem;

    public CapuchinCollectItemGoal(CapuchinEntity capuchinEntity) {
        this.target = capuchinEntity;
        this.setControls(EnumSet.of(Control.MOVE, Control.JUMP, Control.TARGET, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        return this.target.getMainHandStack().isEmpty() && !this.target.getTargetItems().isEmpty() && !this.target.isSitting();
    }

    public void start() {
        var items = target.getTargetItems();

        var closestItem = items.stream().sorted(this::sortItemsByDistance).filter(ItemEntity::isAlive).findFirst().orElse(null);
        if (closestItem == null) {
            return;
        }
        this.targetItem = closestItem;
        this.target.getNavigation().startMovingTo(targetItem, 1);
    }

    public void tick() {
        this.target.getWorld().getServer().getPlayerManager().getPlayerList().forEach(p-> p.sendMessage(Text.literal("Tick collect")));

        if(targetItem != null){
            if (this.target.distanceTo(this.targetItem) <= 1.5 || (this.target.getNavigation().getCurrentPath() != null && this.target.getNavigation().getCurrentPath().isFinished())) {
                this.target.setStackInHand(Hand.MAIN_HAND, targetItem.getStack());
                this.targetItem.kill();
                this.targetItem = null;
            }
        }

        if (this.target.getNavigation().getCurrentPath() == null && this.targetItem != null) {
            this.target.getNavigation().startMovingTo(targetItem, 1);
        }
    }

    public boolean shouldContinue() {
        return this.targetItem != null && this.targetItem.isAlive() && !this.target.isSitting();
    }

    @Override
    public boolean canStop() {
        return super.canStop();
    }

    private int sortItemsByDistance(ItemEntity itemEntity, ItemEntity itemEntity1) {
        return this.target.getPos().distanceTo(itemEntity.getPos()) > this.target.getPos().distanceTo(itemEntity1.getPos()) ? 1 : 0;
    }
}
